package org.nicodom.tracer.tracer

import cats.FlatMap
import com.github.gvolpe.tracer.Trace.Trace
import com.github.gvolpe.tracer.{Trace, TracerLog}
import org.nicodom.tracer.model.user.{User, Username}
import org.nicodom.tracer.module.Repositories
import org.nicodom.tracer.repository.algebra.UserRepository

class UserTracerRepository[F[_]: FlatMap](repo: UserRepository[F])(implicit L: TracerLog[Trace[F, ?]]) extends UserRepository[Trace[F, ?]] {

  override def find(username: Username): Trace[F, Option[User]] =
    for {
      _ <- L.info[UserRepository[F]](s"Find user by username: ${username.value}")
      u <- Trace(_ => repo.find(username))
    } yield u

  override def persist(user: User): Trace[F, Unit] =
    for {
      _ <- L.info[UserRepository[F]](s"Persisting user: ${user.username.value}")
      _ <- Trace(_ => repo.persist(user))
    } yield ()

}

class TracedRepositories[F[_]: FlatMap](repos: Repositories[F])(implicit L: TracerLog[Trace[F, ?]]) extends Repositories[Trace[F, ?]] {
  val users: UserRepository[Trace[F, ?]] = new UserTracerRepository[F](repos.users)
}
