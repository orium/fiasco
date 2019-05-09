/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco.scalaz

import fiasco.{Convert, Failure}
import scalaz.{EitherT, Functor}

import scala.language.higherKinds

object syntax {
  implicit class EitherTOps[F[_], E <: Throwable, A](eitherT: EitherT[F, E, A]) {
    def toFailureEitherT(implicit F: Functor[F]): EitherT[F, Failure, A] =
      eitherT.leftMap(Failure.fromThrowable)
  }

  implicit class EitherTConvertOps[F[_], E, A](eitherT: EitherT[F, E, A]) {
    def leftConvert[To](implicit F: Functor[F], convert: Convert[E, To]): EitherT[F, To, A] =
      eitherT.leftMap(convert.convert)
  }
}
