/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

/**
 * A stacktrace.
 */
final case class Stacktrace(stackframes: Seq[StackTraceElement]) extends Traversable[StackTraceElement] {
  override def foreach[U](f: StackTraceElement => U): Unit =
    stackframes.foreach(f)

  /**
   * Drops the stacktrace on top of the stack.
   */
  def dropFirst: Stacktrace = Stacktrace(stackframes.drop(1))

  override def toString: String =
    stackframes.map(_.toString).mkString("\n")
}

object Stacktrace {
  /**
   * Gets the current stacktrace.
   *
   * The stacktrace will not include this function's stackframe.
   */
  def current(): Stacktrace =
    Stacktrace(Thread.currentThread().getStackTrace().toSeq).dropFirst.dropFirst
}
