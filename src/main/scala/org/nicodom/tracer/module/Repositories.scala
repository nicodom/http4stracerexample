package org.nicodom.tracer.module

import cats.effect.Sync
import cats.syntax.all._
import org.nicodom.tracer.repository.algebra.UserRepository
import org.nicodom.tracer.repository.interpreter.MemUserRepository

trait Repositories[F[_]] {
  def users: UserRepository[F]
}

class LiveRepositories[F[_]](usersRepo: UserRepository[F]) extends Repositories[F] {
  val users: UserRepository[F] = usersRepo
}

object LiveRepositories {
  def apply[F[_]: Sync]: F[Repositories[F]] =
    MemUserRepository.create[F].map(new LiveRepositories[F](_))
}