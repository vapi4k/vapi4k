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

import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.contracts.contract
import kotlin.text.Charsets.UTF_8

object Utils {
  fun <T> lambda(block: T) = block

  fun String.ensureStartsWith(prefix: String) = if (startsWith(prefix)) this else prefix + this

  fun String.ensureEndsWith(suffix: String) = if (endsWith(suffix)) this else this + suffix

  fun String.trimLeadingSpaces() = lines().joinToString(separator = "\n") { it.trimStart() }

  fun resourceFile(filename: String): String =
    this::class.java.getResource(filename)?.readText() ?: error("Resource file not found: $filename")
  // this.javaClass.classLoader.getResource(filename)?.readText() ?: error("File not found: $filename")

  fun Int.lpad(
    width: Int,
    padChar: Char = '0',
  ): String = toString().padStart(width, padChar)

  fun Int.rpad(
    width: Int,
    padChar: Char = '0',
  ): String = toString().padEnd(width, padChar)

  fun String.capitalizeFirstChar(): String =
    lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

  fun String.obfuscate(freq: Int = 2) = mapIndexed { i, v -> if (i % freq == 0) '*' else v }.joinToString("")

  val Throwable.errorMsg get() = "${this::class.simpleName} - $message"

  fun Throwable.toErrorString() =
    "${
      stackTraceToString()
        .lines()
        .filterNot { it.trimStart().startsWith("at io.ktor") }
        .filterNot { it.trimStart().startsWith("at kotlin") }
        .joinToString("\n")
    }\t..."

  fun Any?.isNotNull(): Boolean {
    contract {
      returns(true) implies (this@isNotNull != null)
    }
    return this != null
  }

  fun Any?.isNull(): Boolean {
    contract {
      returns(false) implies (this@isNull != null)
    }
    return this == null
  }

  fun String.encode() = URLEncoder.encode(this, UTF_8.toString()) ?: this

  fun String.decode() = URLDecoder.decode(this, UTF_8.toString()) ?: this
}
