/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

import org.scalatest.{FlatSpec, Matchers}

class StacktraceSpec extends FlatSpec with Matchers {
  behavior of "Stacktrace"

  it should "correctly capture the information about the current call stack" in {
    def theFunctionThatShouldBeOnTopOfTheStack(): Stacktrace =
      Stacktrace.current()

    val stacktrace = theFunctionThatShouldBeOnTopOfTheStack()

    stacktrace.head.getMethodName.contains("theFunctionThatShouldBeOnTopOfTheStack") shouldBe true
  }
}
