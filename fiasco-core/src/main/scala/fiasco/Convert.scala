/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

trait Convert[-From, +To] {
  def convert(a: From): To
}

object Convert {
  def apply[From, To](implicit convert: Convert[From, To]): Convert[From, To] = convert

  def instance[From, To](convert: From => To): Convert[From, To] = (a: From) => convert(a)
}
