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

package com.vapi4k.api.prompt

import com.vapi4k.common.Utils.trimLeadingSpaces

class Prompt {
  val stringBuilder = StringBuilder()

  operator fun String.unaryPlus() {
    stringBuilder.append(this).append("\n\n")
  }

  fun singleLine(
    str: String,
    vararg strs: String,
  ) {
    processSingleLine(str)
    strs.forEach { processSingleLine(it) }
  }

  private fun processSingleLine(str: String) {
    var firstInSection = true
    val newStr =
      buildString {
        str
          .lines()
          .map { it.trim() }
          .forEach { s ->
            if (s.isNotEmpty()) {
              if (firstInSection)
                firstInSection = false
              else
                append(" ")
              append(s)
            } else {
              append("\n\n")
              firstInSection = true
            }
          }
      }
    stringBuilder.append(newStr.trim()).append("\n\n")
  }

  fun trimPrefix(
    str: String,
    vararg strs: String,
  ) {
    processTrimPrefix(str)
    strs.forEach { processTrimPrefix(it) }
  }

  private fun processTrimPrefix(str: String) {
    stringBuilder.append(str.trimLeadingSpaces().trim()).append("\n\n")
  }

  companion object {
    fun prompt(block: Prompt.() -> Unit): String = Prompt().apply(block).stringBuilder.toString().trim()
  }
}
