package org.nicodom.tracer.repository.interpreter

import cats.effect.Sync
import cats.effect.concurrent.Ref
import cats.syntax.all._
import org.nicodom.tracer.model.user.{User, Username}
import org.nicodom.tracer.repository.algebra.UserRepository


class MemUserRepository[F[_]: Sync](
  state: Ref[F, Map[Username, User]]
) extends UserRepository[F] {
  override def find(username: Username): F[Option[User]] =
    state.get.map(_.get(username))

  override def persist(user: User): F[Unit] =
    state.update(_.updated(user.username, user))
}

object MemUserRepository {
  def create[F[_]: Sync]: F[UserRepository[F]] =
    Ref.of[F, Map[Username, User]](Map.empty).map(new MemUserRepository[F](_))
}