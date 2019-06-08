/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco.cats

import cats.Functor
import cats.data.EitherT
import fiasco.syntax._
import fiasco.{Convert, Fail}

import scala.language.higherKinds

package object syntax {
  implicit class EitherTOps[F[_], E <: Throwable, A](eitherT: EitherT[F, E, A]) {
    /**
     * Convert the left value from a [[Throwable]] to a [[fiasco.Fail Fail]].
     */
    def leftToFail(implicit F: Functor[F]): EitherT[F, Fail, A] =
      eitherT.leftMap(Fail.fromThrowable)
  }

  implicit class EitherTConvertOps[F[_], E, A](eitherT: EitherT[F, E, A]) {
    /**
     * Convert the left value to `To`.
     */
    def leftConvert[To](implicit F: Functor[F], convert: Convert[E, To]): EitherT[F, To, A] =
      eitherT.leftMap(_.convert)
  }
}
