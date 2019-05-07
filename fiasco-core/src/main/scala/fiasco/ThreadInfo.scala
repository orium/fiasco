/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

final case class ThreadInfo(id: Long, name: String, priority: Int)

object ThreadInfo {
  def apply(thread: Thread): ThreadInfo =
    ThreadInfo(thread.getId(), thread.getName(), thread.getPriority())

  def current(): ThreadInfo =
    ThreadInfo(Thread.currentThread())
}
