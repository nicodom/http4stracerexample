package org.nicodom.tracer.tracer

import cats.effect.Sync
import com.github.gvolpe.tracer.Trace.Trace
import com.github.gvolpe.tracer.TracerLog
import org.nicodom.tracer.algebra.UserAlgebra
import org.nicodom.tracer.model.user.{User, Username}
import org.nicodom.tracer.module.{Programs, Repositories}
import org.nicodom.tracer.program.UserProgram
import org.nicodom.tracer.repository.algebra.UserRepository


class UserTracer[F[_]: Sync](repo: UserRepository[Trace[F, ?]])(implicit L: TracerLog[Trace[F, ?]]) extends UserProgram[Trace[F, ?]](repo) {

  override def find(username: Username): Trace[F, User] =
    for {
      _ <- L.info[UserAlgebra[F]](s"Find user by username: ${username.value}")
      u <- super.find(username)
    } yield u

  override def persist(user: User): Trace[F, Unit] =
    for {
      _  <- L.info[UserAlgebra[F]](s"About to persist user: ${user.username.value}")
      rs <- super.persist(user)
    } yield rs
}


class TracedPrograms[F[_]: Sync](repos: Repositories[Trace[F, ?]])(implicit L: TracerLog[Trace[F, ?]]) extends Programs[Trace[F, ?]] {
  override val users: UserAlgebra[Trace[F, ?]] = new UserTracer[F](repos.users)
}