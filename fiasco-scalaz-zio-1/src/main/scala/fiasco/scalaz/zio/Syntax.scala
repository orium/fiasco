/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco.scalaz.zio

import fiasco.{Convert, Fail}
import scalaz.zio.ZIO

object syntax {
  implicit class ZIOOps[R, E <: Throwable, A](zio: ZIO[R, E, A]) {
    def toFailZIO: ZIO[R, Fail, A] =
      zio.mapError(Fail.fromThrowable)
  }

  implicit class ZIOConvertOps[R, E, A](zio: ZIO[R, E, A]) {
    def errorConvert[To](implicit convert: Convert[E, To]): ZIO[R, To, A] =
      zio.mapError(Convert[E, To].convert)
  }
}
