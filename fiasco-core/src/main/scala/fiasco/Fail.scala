/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

trait Fail {
  def description: String
  def cause: Option[Fail] = None

  val threadInfo: Option[ThreadInfo] = Some(ThreadInfo.current())
  val stacktrace: Stacktrace = Stacktrace.current()

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

  def toException: Exception = {
    val e = new Exception(description)

    cause.map(_.toException).foreach(e.initCause(_))
    e.setStackTrace(stacktrace.stackframes.toArray)

    e
  }

  override def toString: String = description
}

object Fail {
  def make(description: String): Fail = {
    val d = description
    val s = Stacktrace.current().dropFirst

    new Fail {
      override def description: String = d
      override val stacktrace: Stacktrace = s
    }
  }

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

  def fromThrowable(t: Throwable): Fail = new Fail {
    override def description: String =
      Option(t.getMessage)
        .getOrElse(s"""<No description from Throwable "${Option(t.getClass().getCanonicalName()).getOrElse("?")}">""")

    override def cause: Option[Fail] = Option(t.getCause).map(fromThrowable)
    override val stacktrace: Stacktrace = Stacktrace(t.getStackTrace.toSeq)
    override val threadInfo: Option[ThreadInfo] = None
  }
}
