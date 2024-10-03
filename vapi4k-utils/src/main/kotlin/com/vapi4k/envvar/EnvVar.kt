/*
 * Copyright © 2024 Matthew Ambrose (mattbobambrose@gmail.com)
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

package com.vapi4k.envvar

import com.typesafe.config.ConfigFactory
import com.vapi4k.common.Utils.isNull
import com.vapi4k.common.Utils.lambda
import com.vapi4k.common.Utils.obfuscate
import io.ktor.server.config.HoconApplicationConfig
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class EnvVar(
  val name: String,
  private val src: EnvVar.() -> Any,
  private val maskFunc: ((str: String) -> String)? = null,
  private val width: Int = 10,
  val required: Boolean = false,
  val reportOnBoot: Boolean = true,
) {
  init {
    envVars[name] = this
    if (required)
      getRequired()
  }

  val value: String by lazy { src().toString() }

  private val logReport: String by lazy { "$name: $logValue" }

  private val logValue
    get() = if (maskFunc.isNull())
      value
    else
      maskFunc.invoke(value).let { if (it.length > width) it.substring(0, width) else it.padEnd(width, '*') }

  fun toBoolean(): Boolean = value.toBoolean()

  fun toInt(): Int = value.toInt()

  fun getEnvOrNull(): String? = System.getenv(name)

  fun getEnv(default: String): String = getEnvOrNull() ?: default

  fun getEnv(default: Boolean) = getEnvOrNull()?.toBoolean() ?: default

  fun getEnv(default: Int) = getEnvOrNull()?.toInt() ?: default

  fun getRequired() = getEnvOrNull() ?: error("Missing $name value")

  override fun toString(): String = value

  companion object {
    init {
      // Without this, the logger will not throw a missing value exception when reporting the values at startup
      System.setProperty("kotlin-logging.throwOnMessageError", "true")
    }

    fun obfuscate(freq: Int): String.() -> String = lambda { obfuscate(freq) }

    fun getWithDefault(default: String): EnvVar.() -> String = lambda { System.getenv(name) ?: default }

    fun getWithDefault(default: Int) = getWithDefault(default.toString())

    fun getWithDefault(default: Boolean) = getWithDefault(default.toString())

    private val envVars = mutableMapOf<String, EnvVar>()

    fun String.isDefined() = System.getenv(this).orEmpty().isNotBlank()

    fun logEnvVarValues(block: (String) -> Unit) =
      envVars.values
        .filter { it.reportOnBoot && it.value.isNotBlank() }
        .sortedBy { it.name }
        .map { it.logReport }
        .forEach(block)

    fun jsonEnvVarValues() =
      buildJsonObject {
        envVars.values
          .filter { it.reportOnBoot && it.value.isNotBlank() }
          .sortedBy { it.name }
          .forEach { put(it.name, JsonPrimitive(it.logValue)) }
      }

    private val config by lazy { HoconApplicationConfig(ConfigFactory.load()) }

    fun getSystemValue(
      envvarValue: String,
      propertyName: String,
      block: () -> String,
    ) = envvarValue.ifBlank { config.propertyOrNull(propertyName)?.getString() ?: error(block()) }
  }
}
