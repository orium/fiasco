/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

import fiasco.syntax._
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.Try

class SyntaxSpec extends FlatSpec with Matchers {
  behavior of "Throwable extension methods"

  it should "convert itself to a fail" in {
    val exception = new Exception("a description")

    exception.toFail.description shouldBe "a description"
  }

  behavior of "Future extension methods"

  it should "convert a failed future into a future of an either" in {
    val future: Future[Either[Fail, Nothing]] = Future(throw new Exception("a description")).liftFail
    val result: Either[Fail, Nothing] = Await.result(future, Duration.Inf)

    result should matchPattern {
      case Left(f: Fail) if f.description == "a description" =>
    }
  }

  it should "convert a successful future into a future of an either" in {
    val future: Future[Either[Fail, Int]] = Future(42).liftFail
    val result: Either[Fail, Int] = Await.result(future, Duration.Inf)

    result should matchPattern {
      case Right(42) =>
    }
  }

  it should "capture an exception and convert it to a fail" in {
    val future: Future[Either[Fail, Nothing]] = Future.catchNonFatalAsFail {
      throw new Exception("a description")
    }
    val result: Either[Fail, Int] = Await.result(future, Duration.Inf)

    result should matchPattern {
      case Left(f: Fail) if f.description == "a description" =>
    }
  }

  it should "be able to convert the error type" in {
    implicit val intStringConvert: Convert[RuntimeException, String] = Convert.instance(_.getMessage)

    val future: Future[Left[RuntimeException, Nothing]] = Future.successful(Left(new RuntimeException("42")))
    val result: Either[String, Nothing] = Await.result(future.leftConvert, Duration.Inf)

    result shouldBe Left("42")
  }

  behavior of "Try extension methods"

  it should "convert a failed try into a either" in {
    val t: Either[Fail, Nothing] = Try(throw new Exception("a description")).toFailEither

    t should matchPattern {
      case Left(f: Fail) if f.description == "a description" =>
    }
  }

  it should "convert a successful try into a either" in {
    val t: Either[Fail, Int] = Try(42).toFailEither

    t shouldBe Right(42)
  }

  behavior of "Either extension methods"

  it should "convert a left either into a either" in {
    val t: Either[Fail, Nothing] = Left(new Exception("a description")).leftToFail

    t should matchPattern {
      case Left(f: Fail) if f.description == "a description" =>
    }
  }

  it should "convert a right either into a either" in {
    val t: Either[Fail, Int] = Right(42).leftToFail

    t shouldBe Right(42)
  }

  it should "capture an exception and convert it to a fail" in {
    val either: Either[Fail, Nothing] = Either.catchNonFatalAsFail {
      throw new Exception("a description")
    }

    either should matchPattern {
      case Left(f: Fail) if f.description == "a description" =>
    }
  }

  behavior of "Convert extension methods"

  it should "be able to convert the errors" in {
    implicit val intStringConvert: Convert[RuntimeException, String] = Convert.instance(_.getMessage)

    new RuntimeException("42").convert[String] shouldBe "42"
  }

  it should "be able to convert the error in eithers" in {
    implicit val intStringConvert: Convert[RuntimeException, String] = Convert.instance(_.getMessage)

    val either: Either[RuntimeException, String] = Left(new RuntimeException("42"))

    either.leftConvert shouldBe Left("42")
  }
}
