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

import com.github.pambrose.common.json.toJsonString
import com.vapi4k.plugin.Vapi4kServer.logger

object DslUtils {
  inline fun <reified T> logObject(
    value: T,
    prettify: Boolean = false,
  ) = logger.info { "${T::class.simpleName} JSON:\n${value?.toJsonString(prettify) ?: ""}" }

  inline fun <reified T> printObject(
    value: T,
    prettify: Boolean = false,
  ) = println("${T::class.simpleName} JSON:\n${value?.toJsonString(prettify) ?: ""}\"")

  val isLoggingEnabled: Boolean get() = System.getenv("LOGGING_ENABLED")?.toBoolean() ?: false

  fun String.includeIf(
    condition: Boolean,
    otherWise: String = "",
  ) = if (condition) this else otherWise

  fun getRandomString(
    requiredLength: Int = 10,
    charPool: List<Char> = defaultCharPool,
  ) = (1..requiredLength).map { charPool.random() }.joinToString("")

  // This is for DSL users
  fun getRandomSecret(
    requiredLength: Int = 10,
    charPool: List<Char> = defaultCharPool,
  ) = getRandomString(requiredLength, charPool)

  private val defaultCharPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')

  // This is for DSL users
  fun getRandomSecret(
    requiredLength: Int = 10,
    vararg additionalLengths: Int,
    separator: String = "-",
    charPool: List<Char> = defaultCharPool,
  ) = mutableListOf(requiredLength).also { it.addAll(additionalLengths.toList()) }
    .joinToString(separator) { length -> getRandomString(length, charPool) }
}
