/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco.cats.effect

import cats.effect.IO
import fiasco.syntax._
import fiasco.{Convert, Fail}

object syntax {
  implicit class IOOps[A](io: IO[A]) {
    def attemptFail: IO[Either[Fail, A]] =
      io.attempt.map(_.leftToFail)
  }

  implicit class IOObjOps(ioObj: IO.type) {
    def catchNonFatalAsFail[A](f: => A): IO[Either[Fail, A]] =
      IO(f).attemptFail
  }

  implicit class IOConvertOps[E, A](io: IO[Either[E, A]]) {
    def leftConvert[To](implicit convert: Convert[E, To]): IO[Either[To, A]] =
      io.map(_.leftConvert)
  }
}
