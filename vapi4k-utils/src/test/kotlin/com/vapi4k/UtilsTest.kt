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

import com.vapi4k.common.Utils.capitalizeFirstChar
import com.vapi4k.common.Utils.decode
import com.vapi4k.common.Utils.encode
import com.vapi4k.common.Utils.ensureEndsWith
import com.vapi4k.common.Utils.ensureStartsWith
import com.vapi4k.common.Utils.isNotNull
import com.vapi4k.common.Utils.isNull
import com.vapi4k.common.Utils.lpad
import com.vapi4k.common.Utils.obfuscate
import com.vapi4k.common.Utils.rpad
import com.vapi4k.common.Utils.trimLeadingSpaces
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class UtilsTest : StringSpec() {
  init {
    "ensureStartsWith already has prefix" {
      "/path".ensureStartsWith("/") shouldBe "/path"
    }

    "ensureStartsWith missing prefix" {
      "path".ensureStartsWith("/") shouldBe "/path"
    }

    "ensureStartsWith with multi-char prefix" {
      "example.com".ensureStartsWith("https://") shouldBe "https://example.com"
      "https://example.com".ensureStartsWith("https://") shouldBe "https://example.com"
    }

    "ensureEndsWith already has suffix" {
      "file.kt".ensureEndsWith(".kt") shouldBe "file.kt"
    }

    "ensureEndsWith missing suffix" {
      "file".ensureEndsWith(".kt") shouldBe "file.kt"
    }

    "trimLeadingSpaces preserves newlines" {
      val input = "  line1\n    line2\n  line3"
      val expected = "line1\nline2\nline3"
      input.trimLeadingSpaces() shouldBe expected
    }

    "trimLeadingSpaces with no leading spaces" {
      "abc\ndef".trimLeadingSpaces() shouldBe "abc\ndef"
    }

    "obfuscate with default freq" {
      val result = "abcdef".obfuscate()
      result shouldBe "*b*d*f"
    }

    "obfuscate with freq 3" {
      val result = "abcdef".obfuscate(3)
      result shouldBe "*bc*ef"
    }

    "obfuscate empty string" {
      "".obfuscate() shouldBe ""
    }

    "capitalizeFirstChar with mixed case" {
      "hELLO".capitalizeFirstChar() shouldBe "Hello"
    }

    "capitalizeFirstChar with lowercase" {
      "world".capitalizeFirstChar() shouldBe "World"
    }

    "capitalizeFirstChar with already capitalized" {
      "Hello".capitalizeFirstChar() shouldBe "Hello"
    }

    "encode and decode round-trip" {
      val original = "a b&c=d+e"
      original.encode().decode() shouldBe original
    }

    "encode special characters" {
      val encoded = "hello world".encode()
      encoded shouldBe "hello+world"
    }

    "isNull and isNotNull with null" {
      val value: String? = null
      value.isNull() shouldBe true
      value.isNotNull() shouldBe false
    }

    "isNull and isNotNull with non-null" {
      val value: String? = "hello"
      value.isNull() shouldBe false
      value.isNotNull() shouldBe true
    }

    "lpad pads with zeros" {
      42.lpad(5) shouldBe "00042"
    }

    "lpad with custom char" {
      7.lpad(3, ' ') shouldBe "  7"
    }

    "rpad pads with zeros" {
      42.rpad(5) shouldBe "42000"
    }

    "rpad with custom char" {
      7.rpad(3, ' ') shouldBe "7  "
    }
  }
}
