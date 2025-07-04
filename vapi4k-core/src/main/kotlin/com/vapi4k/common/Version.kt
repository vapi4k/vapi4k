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

package com.vapi4k.common

import com.github.pambrose.common.json.toJsonString
import com.vapi4k.common.Constants.UNKNOWN
import com.vapi4k.utils.DateUtils.TIME_ZONE
import com.vapi4k.utils.DateUtils.toFullDateString
import kotlinx.datetime.Instant.Companion.fromEpochMilliseconds
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Version(
  val version: String,
  val releaseDate: String,
  val buildTime: Long,
) {
  companion object {
    private fun buildDateTime(buildTime: Long) = fromEpochMilliseconds(buildTime).toLocalDateTime(TIME_ZONE)

    private fun buildDateTimeStr(buildTime: Long) = buildDateTime(buildTime).toFullDateString()

    val jsonStr = { version: String, buildDate: String, buildTime: Long ->
      buildJsonObject {
        put("version", version)
        put("release_date", buildDate)
        put("build_time", buildDateTimeStr(buildTime))
      }.toJsonString()
    }
    val plainStr = { version: String, buildDate: String, buildTime: Long ->
      "Version: $version Release Date: $buildDate Build Date: ${buildDateTimeStr(buildTime)}"
    }

    fun KClass<*>.buildString() = findAnnotation<Version>()?.run { buildDateTimeStr(buildTime) } ?: UNKNOWN

    fun KClass<*>.buildDateTime() = findAnnotation<Version>()?.run { buildDateTime(buildTime) }

    fun KClass<*>.version() = findAnnotation<Version>()?.run { version } ?: UNKNOWN

    fun KClass<*>.versionDesc(asJson: Boolean = false): String =
      findAnnotation<Version>()
        ?.run {
          if (asJson)
            jsonStr(version, releaseDate, buildTime)
          else
            plainStr(version, releaseDate, buildTime)
        }
        ?: if (asJson) jsonStr(UNKNOWN, UNKNOWN, 0) else plainStr(UNKNOWN, UNKNOWN, 0)
  }
}
