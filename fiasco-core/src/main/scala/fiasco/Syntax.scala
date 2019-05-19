/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import scala.util.control.NonFatal

object syntax {
  implicit class ThrowableOps[E <: Throwable](throwable: E) {
    def toFail: Fail = Fail.fromThrowable(throwable)
  }

  implicit class FutureOps[A](future: Future[A]) {
    def attemptFail(implicit executionContext: ExecutionContext): Future[Either[Fail, A]] =
      future.transformWith(t => Future.successful(t.toFailEither))

    def failedFail(implicit executionContext: ExecutionContext): Future[Fail] =
      future.failed.map(_.toFail)
  }

  implicit class FutureObjOps(futureObj: Future.type) {
    def catchNonFatalFail[A](f: => A)(implicit executionContext: ExecutionContext): Future[Either[Fail, A]] =
      Future(f).attemptFail
  }

  implicit class TryOps[A](t: Try[A]) {
    def toFailEither: Either[Fail, A] = t.toEither.toFailEither
  }

  implicit class EitherOps[A, E <: Throwable](either: Either[E, A]) {
    def toFailEither: Either[Fail, A] = either.left.map(_.toFail)
  }

  implicit class EitherObjOps(eitherObj: Either.type) {
    def catchNonFatalFail[A](f: => A): Either[Fail, A] =
      try {
        Right(f)
      } catch {
        case NonFatal(t) => Left(t.toFail)
      }
  }

  implicit class AllConvertOps[A](a: A) {
    def convert[To](implicit convert: Convert[A, To]): To = Convert[A, To].convert(a)
  }

  implicit class EitherConvertOps[A, E](either: Either[E, A]) {
    def leftConvert[To](implicit convert: Convert[E, To]): Either[To, A] = either.left.map(Convert[E, To].convert)
  }
}
