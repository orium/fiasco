/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco.zio

import fiasco.zio.syntax._
import fiasco.{Convert, Fail}
import org.scalatest.{FlatSpec, Matchers}
import scalaz.zio.{DefaultRuntime, ZIO}

class SyntaxSpec extends FlatSpec with Matchers {
  private val runtime: DefaultRuntime = new DefaultRuntime {}

  behavior of "ZIO extension methods"

  it should "be able to convert a `Throwable` to a `Fail`" in {
    val effect: ZIO[Any, Throwable, Nothing] = ZIO(throw new Exception("this is a test"))
    val effectFail: ZIO[Any, Fail, Nothing] = effect.errorToFail

    runtime.unsafeRun(effectFail.either).left.get.description shouldBe "this is a test"
  }

  it should "be able to convert the error type" in {
    implicit val intStringConvert: Convert[Int, String] = Convert.instance(_.toString)

    val effect: ZIO[Any, Int, Nothing] = ZIO.fail(42)

    runtime.unsafeRun(effect.errorConvert.either) shouldBe Left("42")
  }
}
