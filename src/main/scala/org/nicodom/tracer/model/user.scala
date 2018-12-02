package org.nicodom.tracer.model

object user {
  final case class Username(value: String) extends AnyVal
  final case class User(username: Username)
}
