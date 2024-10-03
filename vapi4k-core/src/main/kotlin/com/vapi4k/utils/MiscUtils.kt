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

import com.vapi4k.common.Utils.encode
import com.vapi4k.utils.ReflectionUtils.functions
import io.github.oshai.kotlinlogging.KLogger

object MiscUtils {
  internal fun String.removeEnds(str: String = "/") = removePrefix(str).removeSuffix(str)

  internal fun String.appendQueryParams(vararg params: Pair<String, String>) =
    params.fold(this) { acc, param -> acc.appendQueryParam(param) }

  private fun String.appendQueryParam(param: Pair<String, String>): String =
    (if (contains("?")) "&" else "?").let { "$this$it${param.first}=${param.second.encode()}" }

  fun Any.findFunction(methodName: String) =
    functions.singleOrNull { it.name == methodName } ?: error("Method $methodName not found")

  internal fun getBanner(
    filename: String,
    logger: KLogger,
  ): String {
    val banner = logger.javaClass.classLoader.getResource(filename)?.readText()
      ?: throw IllegalArgumentException("Banner not found: \"$filename\"")

    val lines = banner.lines()

    // Trim initial and trailing blank lines, but preserve blank lines in middle;
    var first = -1
    var last = -1
    var lineNum = 0
    lines.forEach { arg1 ->
      if (arg1.trim { arg2 -> arg2 <= ' ' }.isNotEmpty()) {
        if (first == -1)
          first = lineNum
        last = lineNum
      }
      lineNum++
    }

    lineNum = 0

    val vals =
      lines
        .filter {
          val currLine = lineNum++
          currLine in first..last
        }
        .map { arg -> "     $arg" }

    return "\n\n${vals.joinToString("\n")}\n\n"
  }
}
