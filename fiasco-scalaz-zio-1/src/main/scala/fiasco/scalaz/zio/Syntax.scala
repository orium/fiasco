/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco.scalaz.zio

import fiasco.{Convert, Failure}
import scalaz.zio.ZIO

object syntax {
  implicit class ZIOOps[R, E <: Throwable, A](zio: ZIO[R, E, A]) {
    def toFailureZIO: ZIO[R, Failure, A] =
      zio.mapError(Failure.fromThrowable)
  }

  implicit class ZIOConvertOps[R, E, A](zio: ZIO[R, E, A]) {
    def leftConvert[To](implicit convert: Convert[E, To]): ZIO[R, To, A] =
      zio.mapError(convert.convert)
  }
}
