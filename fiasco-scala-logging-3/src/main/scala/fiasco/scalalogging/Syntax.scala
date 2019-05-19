/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fiasco.scalalogging

import com.typesafe.scalalogging.Logger
import fiasco.Fail
import org.slf4j.Marker

object syntax {
  implicit class LoggerOps(logger: Logger) {
    def error(message: String, cause: Fail): Unit =
      logger.error(s"$message\n${cause.fullToString}")

    def error(marker: Marker, message: String, cause: Fail): Unit =
      logger.error(marker, s"$message\n${cause.fullToString}")

    def warn(message: String, cause: Fail): Unit =
      logger.warn(s"$message\n${cause.fullToString}")

    def warn(marker: Marker, message: String, cause: Fail): Unit =
      logger.warn(marker, s"$message\n${cause.fullToString}")

    def info(message: String, cause: Fail): Unit =
      logger.info(s"$message\n${cause.fullToString}")

    def info(marker: Marker, message: String, cause: Fail): Unit =
      logger.info(marker, s"$message\n${cause.fullToString}")

    def debug(message: String, cause: Fail): Unit =
      logger.debug(s"$message\n${cause.fullToString}")

    def debug(marker: Marker, message: String, cause: Fail): Unit =
      logger.debug(marker, s"$message\n${cause.fullToString}")

    def trace(message: String, cause: Fail): Unit =
      logger.trace(s"$message\n${cause.fullToString}")

    def trace(marker: Marker, message: String, cause: Fail): Unit =
      logger.trace(marker, s"$message\n${cause.fullToString}")
  }
}
