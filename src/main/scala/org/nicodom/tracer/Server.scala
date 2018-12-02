package org.nicodom.tracer

import cats.effect._
import cats.syntax.all._
import com.github.gvolpe.tracer.Tracer
import com.github.gvolpe.tracer.instances.tracerlog._
import org.http4s.server.blaze.BlazeServerBuilder
import org.nicodom.tracer.module.{HttpApi, LiveRepositories}
import org.nicodom.tracer.tracer.{TracedPrograms, TracedRepositories}

object Server extends IOApp {
  implicit val tracer: Tracer[IO] = Tracer.create[IO]("Flow-Id")

  override def run(args: List[String]): IO[ExitCode] =
    new Main[IO].server.as(ExitCode.Success)
}

class Main[F[_]: ConcurrentEffect: Timer] {

  implicit val tracer: Tracer[F] = Tracer.create[F]("Dom-Trace-Id")

  val server: F[Unit] =
    LiveRepositories[F].flatMap { repositories =>
      val tracedRepos    = new TracedRepositories[F](repositories)
      val tracedPrograms = new TracedPrograms[F](tracedRepos)
      val httpApi        = new HttpApi[F](tracedPrograms)

      BlazeServerBuilder[F]
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(httpApi.httpApp)
          .serve
          .compile
          .drain

    }
}
