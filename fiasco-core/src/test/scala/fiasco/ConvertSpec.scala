/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco

import fiasco.syntax._
import org.scalatest.{FlatSpec, Matchers}

class ConvertSpec extends FlatSpec with Matchers {
  sealed trait ConnectionError extends Fail
  object ConnectionError {
    case object Timeout extends ConnectionError {
      override def description: String = "connection timeout"
    }
  }

  sealed trait DatabaseError extends Fail
  object DatabaseError {
    case class NetworkError(tcpError: ConnectionError) extends ConnectionError {
      override def description: String = "database error: network error"
      override def cause: Option[Fail] = Some(tcpError)
    }
  }

  implicit val convertTcpErrorToDatabaseError: Convert[ConnectionError, ConnectionError] =
    Convert.instance(DatabaseError.NetworkError.apply)

  behavior of "Fail convertion"

  it should "correctly convert between fails" in {
    val connectionError: Either[ConnectionError, Int] = Left(ConnectionError.Timeout)

    val databaseError: Either[ConnectionError, Int] = connectionError.leftConvert

    databaseError.left.map(_.description) shouldBe Left("database error: network error")
    databaseError.left.map(_.cause.map(_.description)) shouldBe Left(Some("connection timeout"))
  }
}
