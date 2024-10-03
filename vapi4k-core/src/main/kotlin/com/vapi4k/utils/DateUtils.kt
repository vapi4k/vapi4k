/*
 * Copyright © 2024 Matthew Ambrose
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.vapi4k.utils

import com.vapi4k.common.Utils.capitalizeFirstChar
import com.vapi4k.common.Utils.isNotNull
import com.vapi4k.common.Utils.lpad
import com.vapi4k.common.Utils.rpad
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.TimeZone.Companion.UTC
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.DurationUnit.DAYS
import kotlin.time.DurationUnit.HOURS
import kotlin.time.DurationUnit.MILLISECONDS
import kotlin.time.DurationUnit.MINUTES
import kotlin.time.DurationUnit.SECONDS

object DateUtils {
  private const val ZONE_ID = "America/Los_Angeles"
  val TIME_ZONE: TimeZone = TimeZone.of(ZONE_ID)

  fun String.parseToLocalDate() = LocalDate.Formats.ISO.parse(this)

  fun String.parseToLocalTime() = LocalTime.Formats.ISO.parse(this)

  fun String.parseToLocalDateTime() = LocalDateTime.Formats.ISO.parse(this)

  fun instantNow(): Instant = Clock.System.now()

  fun localDateNow(): LocalDate = Clock.System.todayIn(TIME_ZONE)

  fun localDateTimeNow(): LocalDateTime = instantNow().toLocalDateTime(TIME_ZONE)

  // Set it to the 12:59:00 PM of the current day
  fun LocalDate.toUTCDateTime(): LocalDateTime =
    atStartOfDayIn(UTC).plus(1.days).minus(1.minutes).plus(1.milliseconds).toLocalDateTime(UTC)

  fun LocalDateTime.toISO8601(): String =
    toString().let { str ->
      (if (str.contains(".")) str.substringBefore(".") else str) + "Z"
    }

  fun LocalDate.abbrevDayOfWeek(): String = dayOfWeek.name.lowercase().capitalizeFirstChar().substring(0, 3)

  fun LocalDateTime.abbrevDayOfWeek(): String = dayOfWeek.name.lowercase().capitalizeFirstChar().substring(0, 3)

  fun LocalDateTime.toFullDateString(): String =
    "${abbrevDayOfWeek()} ${monthNumber.lpad(2)}/${dayOfMonth.lpad(2)}/${(year - 2000).lpad(2)} " +
      "${hour.lpad(2)}:${minute.lpad(2)}:${second.lpad(2)} PST"

  fun LocalDateTime.toLogString(): String =
    "${monthNumber.lpad(2)}/${dayOfMonth.lpad(2)}/${(year - 2000).lpad(2)} ${hour.lpad(2)}:${
      minute.lpad(2)
    }:${second.lpad(2)}.${(nanosecond / 1000000).rpad(3)} PST"

  fun LocalDate.toMMDDYYYY(): String = "${monthNumber.lpad(2)}/${dayOfMonth.lpad(2)}/${year.lpad(4)}"

  fun LocalDate.toMMDDYY(): String = "${monthNumber.lpad(2)}/${dayOfMonth.lpad(2)}/${(year - 2000).lpad(2)}"

  fun LocalDate.toMMDD(): String = "${monthNumber.lpad(2)}/${dayOfMonth.lpad(2)}"

  fun LocalDate.toDashedYYYYMMDD(): String = "${year.lpad(4)}-${monthNumber.lpad(2)}-${dayOfMonth.lpad(2)}"

  fun LocalDateTime.toMMDDYYYYHHMM(): String =
    "${monthNumber.lpad(2)}/${dayOfMonth.lpad(2)}/${year.lpad(4)} $hour:${minute.lpad(2)}"

  val Instant?.age get() = if (isNotNull()) instantNow() - this else Duration.ZERO

  fun LocalDateTime?.age(timeZone: TimeZone): Duration = if (isNotNull()) toInstant(timeZone).age else Duration.ZERO

  fun Duration.toAdjustedString(unit: DurationUnit = SECONDS): String =
    when (unit) {
      MILLISECONDS -> inWholeMilliseconds.milliseconds
      SECONDS -> inWholeSeconds.seconds
      MINUTES -> inWholeMinutes.minutes
      HOURS -> inWholeHours.hours
      DAYS -> inWholeDays.days
      else -> error("Unsupported duration unit: $unit")
    }.toString()
}
