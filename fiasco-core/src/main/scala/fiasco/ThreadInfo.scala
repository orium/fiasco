/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

/**
 * Information about a thread.
 */
final case class ThreadInfo(id: Long, name: String, priority: Int)

object ThreadInfo {
  /**
   * Creates a [[fiasco.ThreadInfo ThreadInfo]] of the given thread.
   */
  def apply(thread: Thread): ThreadInfo =
    ThreadInfo(thread.getId(), thread.getName(), thread.getPriority())

  /**
   * Obtains the [[fiasco.ThreadInfo ThreadInfo]] of the current thread.
   */
  def current(): ThreadInfo =
    ThreadInfo(Thread.currentThread())
}
