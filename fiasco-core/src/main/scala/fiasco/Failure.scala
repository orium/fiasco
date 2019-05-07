/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

trait Failure {
  def description: String
  def cause: Option[Failure] = None

  val threadInfo: Option[ThreadInfo] = Some(ThreadInfo.current())
  val stacktrace: Stacktrace = Stacktrace.current()

  def fullToString: String = {
    val threadInfoString =
      threadInfo match {
        case Some(t) => s"${t.name} (id=${t.id}, priority=${t.priority})"
        case None    => "?"
      }
    val thisFailure =
      s"""Failure: $description
         |Thread: $threadInfoString
         |Stacktrace:
         |${stacktrace.map(_.toString).map("  " + _).mkString("\n")}""".stripMargin

    cause match {
      case Some(c) =>
        s"""$thisFailure
           |Caused by:
           |${c.fullToString.lines.map("  " + _).mkString("\n")}""".stripMargin

      case None => thisFailure
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

object Failure {
  def make(description: String): Failure = {
    val d = description

    new Failure {
      override def description: String = d
      override val stacktrace: Stacktrace = Stacktrace.current().dropFirst
    }
  }

  def make(description: String, cause: Failure): Failure = {
    val d = description
    val c = cause

    new Failure {
      override def description: String = d
      override def cause: Option[Failure] = Some(c)
      override val stacktrace: Stacktrace = Stacktrace.current().dropFirst
    }
  }

  def fromThrowable(t: Throwable): Failure = new Failure {
    override def description: String = t.getMessage
    override def cause: Option[Failure] = Option(t.getCause).map(fromThrowable)
    override val stacktrace: Stacktrace = Stacktrace(t.getStackTrace.toSeq)
    override val threadInfo: Option[ThreadInfo] = None
  }
}




// WIP! remove vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv


object Bar extends App {
  import syntax._

  // WIP! use this as example in README

  sealed trait TcpError
  object TcpError {
    case object Timeout extends TcpError
  }

  sealed trait DatabaseError
  object DatabaseError {
    case class NetworkError(tcpError: TcpError) extends DatabaseError
  }

  sealed trait HttpError
  object HttpError {
    case class NetworkError(tcpError: TcpError) extends HttpError
  }

  implicit val convertTcpErrorToDatabaseError: Convert[TcpError, DatabaseError] = Convert.instance(DatabaseError.NetworkError.apply)
  implicit val convertTcpErrorToHttpError: Convert[TcpError, HttpError] = Convert.instance(HttpError.NetworkError.apply)

  val underlyingError: Either[TcpError, Int] = Left(TcpError.Timeout)

  val databaseError: Either[DatabaseError, Int] = underlyingError.leftConvert
  val httpError: Either[HttpError, Int] = underlyingError.leftConvert
}

object Foo extends App {


  // WIP! Err: stacktrace and threadinfo is wrong because object
  object AnFailure extends Failure {
    override def description: String = "this is an error"
  }

  object OtherFailure extends Failure {
    override def description: String = "other error"
    override def cause: Option[Failure] = Some(AnFailure)
  }


  // println(Stacktrace.current())

  println("-")
  println(AnFailure.fullToString)
  println("-")
  println(OtherFailure.fullToString)
  println("-")
  println("-")
  println("-")

  object LoopFailure extends Failure {
    override def description: String = "loop error"
    override def cause: Option[Failure] = Some(Failure.make("hello", Failure.make("foo", this)))
  }

  println(LoopFailure.fullToString)
}
