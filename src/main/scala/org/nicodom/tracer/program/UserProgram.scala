package org.nicodom.tracer.program

import cats.MonadError
import cats.implicits._
import org.nicodom.tracer.algebra.UserAlgebra
import org.nicodom.tracer.model.errors.UserError.{UserAlreadyExists, UserNotFound}
import org.nicodom.tracer.model.user.{User, Username}
import org.nicodom.tracer.repository.algebra.UserRepository

class UserProgram[F[_]](repo: UserRepository[F])(implicit F: MonadError[F, Throwable]) extends UserAlgebra[F] {
  override def find(username: Username): F[User] =
    for {
      mu <- repo.find(username)
      rs <- mu.fold(F.raiseError[User](UserNotFound(username)))(F.pure)
    } yield rs

  def persist(user: User): F[Unit] =
    for {
      mu <- repo.find(user.username)
      rs <- mu.fold(repo.persist(user))(_ => F.raiseError(UserAlreadyExists(user.username)))
    } yield rs
}
