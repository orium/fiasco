/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

import org.scalatest.{FlatSpec, Matchers}

class FailSpec extends FlatSpec with Matchers {
  private def stacktraceToMethodNameList(stacktrace: Stacktrace): Seq[String] =
    stacktrace.map(_.getMethodName).toSeq

  behavior of "Fail"

  it should "capture the description" in {
    val fail = Fail.make("a description")

    fail.description shouldBe "a description"
  }

  it should "capture the description and cause" in {
    val cause   = Fail.make("cause")
    val fail = Fail.make("a description", cause)

    fail.description shouldBe "a description"
    fail.cause.map(_.description) shouldBe Some("cause")
  }

  it should "capture the thread info correctly" in {
    val fail = Fail.make("a description")

    fail.threadInfo shouldBe Some(ThreadInfo.current())
  }

  it should "capture the stacktrace correctly" in {
    val fail = Fail.make("a description")

    stacktraceToMethodNameList(fail.stacktrace) shouldBe stacktraceToMethodNameList(Stacktrace.current())
  }

  it should "capture the stacktrace correctly when there is a cause" in {
    def stacktraceToMethodNameList(stacktrace: Stacktrace): Seq[String] =
      stacktrace.map(_.getMethodName).toSeq

    val fail = Fail.make("a description", Fail.make("cause"))

    stacktraceToMethodNameList(fail.stacktrace) shouldBe stacktraceToMethodNameList(Stacktrace.current())
  }

  it should "convert a throwable to a fail" in {
    val exception = new Exception("a description")
    val fail = Fail.fromThrowable(exception)

    fail.description shouldBe "a description"
    fail.cause       shouldBe None
    stacktraceToMethodNameList(fail.stacktrace) shouldBe stacktraceToMethodNameList(Stacktrace.current())
  }

  it should "convert a throwable to a fail when there is a cause" in {
    val cause = new Exception("cause")
    val exception = new Exception("a description", cause)
    val fail = Fail.fromThrowable(exception)

    fail.description              shouldBe "a description"
    fail.cause.map(_.description) shouldBe Some("cause")
    stacktraceToMethodNameList(fail.stacktrace) shouldBe stacktraceToMethodNameList(Stacktrace.current())
  }

  it should "convert a throwable to a fail when there is no description" in {
    val exception = new IllegalArgumentException()
    val fail = Fail.fromThrowable(exception)

    fail.description shouldBe """<No description from Throwable "java.lang.IllegalArgumentException">"""
  }

  it should "convert itself into an exception" in {
    val fail = Fail.make("a description", Fail.make("cause"))
    val exception = fail.toException

    exception.getMessage shouldBe "a description"
    Option(exception.getCause).map(_.getMessage) shouldBe Option("cause")
    stacktraceToMethodNameList(Stacktrace(exception.getStackTrace)) shouldBe stacktraceToMethodNameList(Stacktrace.current())
  }
}
