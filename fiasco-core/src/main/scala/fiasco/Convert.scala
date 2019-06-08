/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

/**
 * A typeclass for a type that can be converted to another.
 */
trait Convert[-From, +To] {
  /**
   * Convert type `From` to `To`.
   */
  def convert(a: From): To
}

object Convert {
  /**
   * Summon the typeclass instance for `Convert[From, To]`.
   */
  def apply[From, To](implicit convert: Convert[From, To]): Convert[From, To] = convert

  /**
   * Creates an instance of the typeclass [[fiasco.Convert Convert]].
   */
  def instance[From, To](convert: From => To): Convert[From, To] = (a: From) => convert(a)
}
