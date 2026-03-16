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

import com.vapi4k.utils.HtmlUtils.encodeForHtml
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class HtmlUtilsTest : StringSpec() {
  init {
    "encodeForHtml escapes all five critical entities" {
      val input = """<div class="test" data-value='a&b'>content</div>"""
      val result = input.encodeForHtml()
      result shouldBe "&lt;div class=&quot;test&quot; data-value=&#39;a&amp;b&#39;&gt;content&lt;/div&gt;"
    }

    "encodeForHtml preserves non-special characters" {
      val input = "Hello World 123 abc"
      input.encodeForHtml() shouldBe "Hello World 123 abc"
    }

    "encodeForHtml handles empty string" {
      "".encodeForHtml() shouldBe ""
    }

    "encodeForHtml escapes ampersand" {
      "a&b".encodeForHtml() shouldBe "a&amp;b"
    }

    "encodeForHtml escapes angle brackets" {
      "<script>".encodeForHtml() shouldBe "&lt;script&gt;"
    }

    "encodeForHtml escapes quotes" {
      """he said "hello" and 'goodbye'""".encodeForHtml() shouldBe
        "he said &quot;hello&quot; and &#39;goodbye&#39;"
    }
  }
}
