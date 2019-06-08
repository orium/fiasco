/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

/**
 * A trait for errors.  Errors extending this trait will have contextual information for free.
 *
 * WIP! mini readme here.
 */
trait Fail {
  /**
   * A human-readable description of this error.
   */
  def description: String

  /**
   * The cause this error.
   */
  def cause: Option[Fail] = None

  /**
   * The thread information at the moment the error was created.
   */
  val threadInfo: Option[ThreadInfo] = Some(ThreadInfo.current())

  /**
   * The stacktrace at the moment the error was created.
   */
  val stacktrace: Stacktrace = Stacktrace.current()

  /**
   * A pretty-print string representing all the information about this error.
   */
  def fullToString: String = {
    val threadInfoString =
      threadInfo match {
        case Some(t) => s"${t.name} (id=${t.id}, priority=${t.priority})"
        case None    => "?"
      }

    val thisFail =
      s"""Fail: $description
         |Thread: $threadInfoString
         |Stacktrace:
         |${stacktrace.map(_.toString).map("  " + _).mkString("\n")}""".stripMargin

    cause match {
      case Some(c) =>
        s"""$thisFail
           |Caused by:
           |${c.fullToString.lines.map("  " + _).mkString("\n")}""".stripMargin

      case None => thisFail
    }
  }

  /**
   * Creates an exception with as much information about this error as possible.
   *
   * Note that some information will be lost in the exception.
   */
  def toException: Exception = {
    val e = new Exception(description)

    cause.map(_.toException).foreach(e.initCause(_))
    e.setStackTrace(stacktrace.stackframes.toArray)

    e
  }

  override def toString: String = description
}

object Fail {
  /**
   * Creates an opaque [[fiasco.Fail Fail]] with the given description.
   */
  def make(description: String): Fail = {
    val d = description
    val s = Stacktrace.current().dropFirst

    new Fail {
      override def description: String = d
      override val stacktrace: Stacktrace = s
    }
  }

  /**
   * Creates an opaque [[fiasco.Fail Fail]] with the given description and cause.
   */
  def make(description: String, cause: Fail): Fail = {
    val d = description
    val c = cause
    val s = Stacktrace.current().dropFirst

    new Fail {
      override def description: String = d
      override def cause: Option[Fail] = Some(c)
      override val stacktrace: Stacktrace = s
    }
  }

  /**
   * Creates a [[fiasco.Fail Fail]] for the given [[Throwable]].
   */
  def fromThrowable(t: Throwable): Fail = new Fail {
    override def description: String =
      Option(t.getMessage)
        .getOrElse(s"""<No description from Throwable "${Option(t.getClass().getCanonicalName()).getOrElse("?")}">""")

    override def cause: Option[Fail] = Option(t.getCause).map(fromThrowable)
    override val stacktrace: Stacktrace = Stacktrace(t.getStackTrace.toSeq)
    override val threadInfo: Option[ThreadInfo] = None
  }
}
