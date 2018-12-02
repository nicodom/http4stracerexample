package org.nicodom.tracer.module

import cats.effect.Sync
import com.github.gvolpe.tracer.Trace.Trace
import com.github.gvolpe.tracer.{Tracer, TracerLog}
import org.http4s.implicits._
import org.http4s.{HttpApp, HttpRoutes}
import org.nicodom.tracer.http.UserRoutes

class HttpApi[F[_]: Sync: Tracer](programs: Programs[Trace[F, ?]])(implicit L: TracerLog[Trace[F, ?]]) {

  private val httpRoutes: HttpRoutes[F] =
    new UserRoutes[F](programs.users).routes

  val httpApp: HttpApp[F] =
    Tracer[F].middleware(httpRoutes.orNotFound)
}
