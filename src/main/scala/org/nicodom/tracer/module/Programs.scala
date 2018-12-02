package org.nicodom.tracer.module

import org.nicodom.tracer.algebra.UserAlgebra

trait Programs[F[_]] {
  def users: UserAlgebra[F]
}
