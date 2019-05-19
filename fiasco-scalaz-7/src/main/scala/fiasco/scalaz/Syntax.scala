/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco.scalaz

import fiasco.{Convert, Fail}
import scalaz.{EitherT, Functor}

import scala.language.higherKinds

object syntax {
  implicit class EitherTOps[F[_], E <: Throwable, A](eitherT: EitherT[F, E, A]) {
    def toFailEitherT(implicit F: Functor[F]): EitherT[F, Fail, A] =
      eitherT.leftMap(Fail.fromThrowable)
  }

  implicit class EitherTConvertOps[F[_], E, A](eitherT: EitherT[F, E, A]) {
    def leftConvert[To](implicit F: Functor[F], convert: Convert[E, To]): EitherT[F, To, A] =
      eitherT.leftMap(Convert[E, To].convert)
  }
}
