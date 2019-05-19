/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco.cats

import cats.data.EitherT
import cats.instances.option._
import fiasco.cats.syntax._
import fiasco.{Convert, Fail}
import org.scalatest.{FlatSpec, Matchers}

class SyntaxSpec extends FlatSpec with Matchers {
  behavior of "Cats EitherT extension methods"

  it should "be able to convert a `Throwable` to a `Fail`" in {
    val eitherT: EitherT[Option, Exception, Unit] = EitherT.left(Some(new Exception("this is a test")))
    val eitherTFail: EitherT[Option, Fail, Unit] = eitherT.leftToFail

    eitherTFail.leftMap(_.description) shouldBe EitherT.left(Option("this is a test"))
  }

  it should "be able to convert the error type" in {
    implicit val intStringConvert: Convert[Int, String] = Convert.instance(_.toString)

    val eitherT: EitherT[Option, Int, Unit] = EitherT.left(Some(42))

    eitherT.leftConvert shouldBe EitherT.left(Option("42"))
  }
}
