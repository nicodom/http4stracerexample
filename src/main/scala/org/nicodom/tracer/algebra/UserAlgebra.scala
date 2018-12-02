package org.nicodom.tracer.algebra

import org.nicodom.tracer.model.user.{User, Username}

trait UserAlgebra[F[_]] {
  def find(username: Username): F[User]
  def persist(user: User): F[Unit]
}
