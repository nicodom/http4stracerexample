package org.nicodom.tracer.model

import org.nicodom.tracer.model.user.Username

object errors {
  sealed trait UserError extends Exception
  object UserError {
    case class UserAlreadyExists(username: Username) extends UserError
    case class UserNotFound(username: Username)      extends UserError
  }
}
