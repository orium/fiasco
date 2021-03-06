/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import scala.util.control.NonFatal

package object syntax {
  implicit class ThrowableOps[E <: Throwable](throwable: E) {
    /**
     * Convert the [[Throwable]] to a [[fiasco.Fail Fail]].
     */
    def toFail: Fail = Fail.fromThrowable(throwable)
  }

  implicit class TryOps[A](t: Try[A]) {
    /**
     * Convert the [[scala.util.Try Try]] to an [[scala.util.Either Either]] with an error represented by a
     * [[fiasco.Fail Fail]].
     */
    def toEitherFail: Either[Fail, A] = t.toEither.leftToFail
  }

  implicit class EitherOps[A, E <: Throwable](either: Either[E, A]) {
    /**
     * Convert the left value from a [[Throwable]] to a [[fiasco.Fail Fail]].
     */
    def leftToFail: Either[Fail, A] = either.left.map(_.toFail)
  }

  implicit class EitherObjOps(eitherObj: Either.type) {
    /**
     * Creates an [[scala.util.Either Either]] with an error represented by a [[fiasco.Fail Fail]].
     */
    def catchNonFatalAsFail[A](f: => A): Either[Fail, A] =
      try {
        Right(f)
      } catch {
        case NonFatal(t) => Left(t.toFail)
      }
  }

  implicit class FutureOps[A](future: Future[A]) {
    /**
      * Materializes a possible exception in the [[scala.concurrent.Future Future]] as a [[fiasco.Fail Fail]].
      */
    def attemptFail(implicit executionContext: ExecutionContext): Future[Either[Fail, A]] =
      future.transformWith(t => Future.successful(t.toEitherFail))
  }

  implicit class FutureObjOps(futureObj: Future.type) {
    /**
     * Creates a [[scala.concurrent.Future Future]] with any possible exception already materialized as
     * a [[fiasco.Fail Fail]].
     */
    def catchNonFatalAsFail[A](f: => A)(implicit executionContext: ExecutionContext): Future[Either[Fail, A]] =
      Future(f).attemptFail
  }

  implicit class FutureConvertOps[E, A](future: Future[Either[E, A]]) {
    /**
     * Convert the left value to `To`.
     */
    def leftConvert[To](implicit executionContext: ExecutionContext, convert: Convert[E, To]): Future[Either[To, A]] =
      future.map(_.leftConvert)
  }

  implicit class AllConvertOps[A](a: A) {
    /**
     * Convert the value to `To`.
     */
    def convert[To](implicit convert: Convert[A, To]): To = Convert[A, To].convert(a)
  }

  implicit class EitherConvertOps[A, E](either: Either[E, A]) {
    /**
     * Convert the left value to `To`.
     */
    def leftConvert[To](implicit convert: Convert[E, To]): Either[To, A] = either.left.map(_.convert)
  }
}
