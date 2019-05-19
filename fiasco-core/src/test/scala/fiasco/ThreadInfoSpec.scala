/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

import java.util.concurrent.Executors

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class ThreadInfoSpec extends FlatSpec with Matchers {
  behavior of "Thread info"

  it should "correctly capture the information about the current thread" in {
    implicit val executionContext: ExecutionContext = ExecutionContext.fromExecutorService(
      Executors.newFixedThreadPool(1)
    )

    val threadInfoFuture: Future[ThreadInfo] = Future {
      Thread.currentThread().setName("this-is-the-name-of-the-thread")
      Thread.currentThread().setPriority(2)
      ThreadInfo.current()
    }

    val threadInfo: ThreadInfo = Await.result(threadInfoFuture, Duration.Inf)

    threadInfo.name     shouldBe "this-is-the-name-of-the-thread"
    threadInfo.priority shouldBe 2
  }
}
