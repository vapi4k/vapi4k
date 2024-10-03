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

package com.vapi4k.utils

import kotlinx.html.FlowOrMetaDataOrPhrasingContent
import kotlinx.html.HEAD
import kotlinx.html.HTMLTag
import kotlinx.html.SVG
import kotlinx.html.TagConsumer
import kotlinx.html.link
import kotlinx.html.script
import kotlinx.html.stream.appendHTML
import kotlinx.html.unsafe

object HtmlUtils {
  fun HTMLTag.rawHtml(html: String) = unsafe { raw(html) }

  fun HTMLTag.attribs(vararg pairs: Pair<String, Any>) {
    pairs.forEach { (k, v) -> attributes[k] = v.toString() }
  }

  // Creates snippets of HTML for use with HTMX
  fun html(block: TagConsumer<StringBuilder>.() -> Unit): String =
    buildString {
      appendHTML().apply(block)
    }

  fun HEAD.css(vararg files: String) {
    files.forEach { file ->
      link {
        rel = "stylesheet"
        href = file
      }
    }
  }

  fun FlowOrMetaDataOrPhrasingContent.js(vararg files: String) {
    files.forEach { file ->
      script { src = file }
    }
  }

  fun SVG.details(
    width: Int,
    height: Int,
    href: String,
  ) {
    attribs(
      "width" to width,
      "height" to height,
    )
    rawHtml(
      """
           <use xlink:href="#$href"/>
        """,
    )
  }

  fun String.encodeForHtml() =
    buildString {
      for (char in this@encodeForHtml) {
        when (char) {
          '&' -> append("&amp;")
          '<' -> append("&lt;")
          '>' -> append("&gt;")
          '"' -> append("&quot;")
          '\'' -> append("&#39;")
          else -> append(char)
        }
      }
    }
}
