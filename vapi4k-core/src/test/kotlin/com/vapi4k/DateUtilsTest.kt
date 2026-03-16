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

package com.vapi4k

import com.vapi4k.utils.DateUtils.abbrevDayOfWeek
import com.vapi4k.utils.DateUtils.age
import com.vapi4k.utils.DateUtils.instantNow
import com.vapi4k.utils.DateUtils.localDateNow
import com.vapi4k.utils.DateUtils.localDateTimeNow
import com.vapi4k.utils.DateUtils.parseToLocalDate
import com.vapi4k.utils.DateUtils.parseToLocalDateTime
import com.vapi4k.utils.DateUtils.toAdjustedString
import com.vapi4k.utils.DateUtils.toDashedYYYYMMDD
import com.vapi4k.utils.DateUtils.toFullDateString
import com.vapi4k.utils.DateUtils.toISO8601
import com.vapi4k.utils.DateUtils.toMMDD
import com.vapi4k.utils.DateUtils.toMMDDYY
import com.vapi4k.utils.DateUtils.toMMDDYYYY
import com.vapi4k.utils.DateUtils.toMMDDYYYYHHMM
import com.vapi4k.utils.DateUtils.toUTCDateTime
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldMatch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class DateUtilsTest : StringSpec() {
  init {
    "instantNow returns non-null Instant" {
      val now = instantNow()
      now.toEpochMilliseconds() shouldBeGreaterThan 0L
    }

    "localDateNow returns a valid date" {
      val today = localDateNow()
      today.year shouldBeGreaterThan 2024
    }

    "localDateTimeNow returns a valid datetime" {
      val now = localDateTimeNow()
      now.year shouldBeGreaterThan 2024
    }

    "parseToLocalDate round-trips with toDashedYYYYMMDD" {
      val dateStr = "2026-03-16"
      val parsed = dateStr.parseToLocalDate()
      parsed.toDashedYYYYMMDD() shouldBe "2026-03-16"
    }

    "parseToLocalDateTime round-trips with toISO8601" {
      val dateTimeStr = "2026-03-16T14:30:45"
      val parsed = dateTimeStr.parseToLocalDateTime()
      parsed.toISO8601() shouldBe "2026-03-16T14:30:45Z"
    }

    "toISO8601 strips fractional seconds" {
      val dt = LocalDateTime(2026, 3, 16, 14, 30, 0, 123_000_000)
      dt.toISO8601() shouldBe "2026-03-16T14:30:00Z"
    }

    "toMMDDYYYY formats with slashes and zero-pads" {
      val date = LocalDate(2026, 1, 5)
      date.toMMDDYYYY() shouldBe "01/05/2026"
    }

    "toMMDDYY formats with two-digit year" {
      val date = LocalDate(2026, 12, 25)
      date.toMMDDYY() shouldBe "12/25/26"
    }

    "toMMDD formats month and day only" {
      val date = LocalDate(2026, 7, 4)
      date.toMMDD() shouldBe "07/04"
    }

    "toDashedYYYYMMDD formats with dashes" {
      val date = LocalDate(2026, 3, 16)
      date.toDashedYYYYMMDD() shouldBe "2026-03-16"
    }

    "toMMDDYYYYHHMM includes time" {
      val dt = LocalDateTime(2026, 3, 16, 9, 5, 0)
      dt.toMMDDYYYYHHMM() shouldBe "03/16/2026 9:05"
    }

    "toFullDateString includes day abbreviation and PST" {
      val dt = LocalDateTime(2026, 3, 16, 14, 30, 45)
      val result = dt.toFullDateString()
      result shouldEndWith "PST"
      result shouldMatch Regex("""[A-Z][a-z]{2} \d{2}/\d{2}/\d{2} \d{2}:\d{2}:\d{2} PST""")
    }

    "toUTCDateTime sets time near end of day" {
      val date = LocalDate(2026, 3, 16)
      val result = date.toUTCDateTime()
      result.hour shouldBe 23
      result.minute shouldBe 59
    }

    "Instant age returns positive duration" {
      val past = instantNow() - 100.milliseconds
      val age = past.age
      age shouldBeGreaterThan Duration.ZERO
    }

    "null Instant age returns ZERO duration" {
      val nullInstant: Instant? = null
      nullInstant.age shouldBe Duration.ZERO
    }

    "toAdjustedString with default SECONDS unit" {
      val duration = 65_000.milliseconds
      duration.toAdjustedString() shouldBe "1m 5s"
    }

    "toAdjustedString with MILLISECONDS unit" {
      val duration = 1_500.milliseconds
      duration.toAdjustedString(DurationUnit.MILLISECONDS) shouldBe "1.5s"
    }

    "abbrevDayOfWeek returns 3-char abbreviation" {
      val date = LocalDate(2026, 3, 16) // Monday
      date.abbrevDayOfWeek() shouldBe "Mon"
    }

    "abbrevDayOfWeek for LocalDateTime" {
      val dt = LocalDateTime(2026, 3, 16, 10, 0, 0) // Monday
      dt.abbrevDayOfWeek() shouldBe "Mon"
    }
  }
}
