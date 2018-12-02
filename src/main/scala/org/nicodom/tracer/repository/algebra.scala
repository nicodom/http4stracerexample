package org.nicodom.tracer.repository

import org.nicodom.tracer.model.user.{User, Username}

object algebra {
  trait UserRepository[F[_]] {
    def find(username: Username): F[Option[User]]
    def persist(user: User): F[Unit]
  }
}
