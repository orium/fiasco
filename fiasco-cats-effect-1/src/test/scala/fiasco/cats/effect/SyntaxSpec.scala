/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco.cats.effect

import cats.effect.IO
import fiasco.cats.effect.syntax._
import fiasco.{Convert, Fail}
import org.scalatest.{FlatSpec, Matchers}

class SyntaxSpec extends FlatSpec with Matchers {
  behavior of "Cats Effect IO extension methods"

  it should "convert a captured exception into a either with a fail" in {
    val effect: IO[Nothing] = IO(throw new Exception("this is a test"))
    val effectFail: IO[Either[Fail, Nothing]] = effect.attemptFail

    effectFail.unsafeRunSync().left.get.description shouldBe "this is a test"
  }

  it should "capture an exception and convert it to a fail" in {
    val io: IO[Either[Fail, Nothing]] = IO.catchNonFatalFail {
      throw new Exception("a description")
    }

    io.unsafeRunSync() should matchPattern {
      case Left(f: Fail) if f.description == "a description" =>
    }
  }

  it should "be able to convert the error type" in {
    implicit val intStringConvert: Convert[RuntimeException, String] = Convert.instance(_.getMessage)

    val effect: IO[Left[RuntimeException, Nothing]] = IO(Left(new RuntimeException("42")))

    effect.leftConvert.unsafeRunSync().left.get shouldBe "42"
  }
}
