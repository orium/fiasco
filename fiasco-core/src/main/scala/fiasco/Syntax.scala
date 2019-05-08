/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

import scala.util.Try
import scala.util.control.NonFatal

object syntax {
  implicit class ThrowableOps[E <: Throwable](throwable: E) {
    def toFailure: Failure = Failure.fromThrowable(throwable)
  }

  implicit class TryOps[A](t: Try[A]) {
    def toFailureEither: Either[Failure, A] = t.toEither.toFailureEither
  }

  implicit class EitherOps[A, E <: Throwable](either: Either[E, A]) {
    def toFailureEither: Either[Failure, A] = either.left.map(_.toFailure)
  }

  implicit class EitherObjOps(eitherObj: Either.type) {
    def catchNonFatalFailure[A](f: => A): Either[Failure, A] =
      try {
        Right(f)
      } catch {
        case NonFatal(t) => Left(t.toFailure)
      }
  }

  implicit class AllConvertOps[A](a: A) {
    def convert[To](implicit convert: Convert[A, To]): To = convert.convert(a)
  }

  implicit class EitherConvertOps[A, E](either: Either[E, A]) {
    def leftConvert[To](implicit convert: Convert[E, To]): Either[To, A] = either.left.map(convert.convert)
  }
}
