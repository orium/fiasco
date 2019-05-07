/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco.cats.effect

import cats.effect.IO
import fiasco.Failure
import fiasco.syntax._

object syntax {
  implicit class IOOps[A](io: IO[A]) {
    def attemptFailure: IO[Either[Failure, A]] =
      io.attempt.map(_.toFailureEither)
  }
}
