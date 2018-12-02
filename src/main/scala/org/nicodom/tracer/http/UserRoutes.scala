package org.nicodom.tracer.http

import cats.effect.Sync
import cats.syntax.all._
import com.github.gvolpe.tracer.Trace._
import com.github.gvolpe.tracer.{Http4sTracerDsl, TracedHttpRoute, Tracer}
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.nicodom.tracer.algebra.UserAlgebra
import org.nicodom.tracer.model.errors.UserError.{UserAlreadyExists, UserNotFound}
import org.nicodom.tracer.model.user.{User, Username}

class UserRoutes[F[_]: Sync: Tracer](users: UserAlgebra[Trace[F, ?]]) extends  Http4sTracerDsl[F] {

  private val PathPrefix = "/users"

  private val httpRoutes: HttpRoutes[F] = TracedHttpRoute[F] {
    case GET -> Root / username using traceId =>
      users
          .find(Username(username))
          .run(traceId)
          .flatMap(user => Ok(user))
          .handleErrorWith {
            case UserNotFound(u) => NotFound(u.value)
          }

    case tr @ POST -> Root using traceId =>
      tr.request.decode[User] { user =>
        users
            .persist(user)
            .run(traceId)
            .flatMap(_ => Created())
            .handleErrorWith {
              case UserAlreadyExists(u) => Conflict(u.value)
            }
      }
  }

  val routes: HttpRoutes[F] = Router {
    PathPrefix -> httpRoutes
  }
}
