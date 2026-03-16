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

import com.vapi4k.utils.HttpUtils.queryParams
import com.vapi4k.utils.HttpUtils.stripQueryParams
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class HttpUtilsTest : StringSpec() {
  init {
    "stripQueryParams removes query string" {
      "http://example.com/path?a=1&b=2".stripQueryParams() shouldBe "http://example.com/path"
    }

    "stripQueryParams preserves URL without query string" {
      "http://example.com/path".stripQueryParams() shouldBe "http://example.com/path"
    }

    "queryParams extracts query string portion" {
      "http://example.com/path?a=1&b=2".queryParams() shouldBe "a=1&b=2"
    }

    "queryParams returns full URL when no query present" {
      // Note: split("?").last() returns the full string when "?" is not present
      "http://example.com/path".queryParams() shouldBe "http://example.com/path"
    }

    "missingQueryParam throws with parameter name" {
      shouldThrow<IllegalStateException> {
        com.vapi4k.utils.HttpUtils.missingQueryParam("myParam")
      }.message shouldContain "myParam"
    }
  }
}
