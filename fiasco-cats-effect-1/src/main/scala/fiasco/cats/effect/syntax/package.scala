/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco.cats.effect

import cats.effect.IO
import fiasco.syntax._
import fiasco.{Convert, Fail}

package object syntax {
  implicit class IOOps[A](io: IO[A]) {
    /**
     * Materializes a possible exception in the [[cats.effect.IO IO]] as a [[fiasco.Fail Fail]].
     */
    def attemptFail: IO[Either[Fail, A]] =
      io.attempt.map(_.leftToFail)
  }

  implicit class IOObjOps(ioObj: IO.type) {
    /**
     * Creates a [[cats.effect.IO IO]] with any possible exception already materialized as a [[fiasco.Fail Fail]].
     */
    def catchNonFatalAsFail[A](f: => A): IO[Either[Fail, A]] =
      IO(f).attemptFail
  }

  implicit class IOConvertOps[E, A](io: IO[Either[E, A]]) {
    /**
     * Convert the left value to `To`.
     */
    def leftConvert[To](implicit convert: Convert[E, To]): IO[Either[To, A]] =
      io.map(_.leftConvert)
  }
}
