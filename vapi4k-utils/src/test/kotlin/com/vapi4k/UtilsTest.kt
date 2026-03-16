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
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import kotlin.test.Test

class UtilsTest {
  @Test
  fun `ensureStartsWith already has prefix`() {
    "/path".ensureStartsWith("/") shouldBeEqualTo "/path"
  }

  @Test
  fun `ensureStartsWith missing prefix`() {
    "path".ensureStartsWith("/") shouldBeEqualTo "/path"
  }

  @Test
  fun `ensureStartsWith with multi-char prefix`() {
    "example.com".ensureStartsWith("https://") shouldBeEqualTo "https://example.com"
    "https://example.com".ensureStartsWith("https://") shouldBeEqualTo "https://example.com"
  }

  @Test
  fun `ensureEndsWith already has suffix`() {
    "file.kt".ensureEndsWith(".kt") shouldBeEqualTo "file.kt"
  }

  @Test
  fun `ensureEndsWith missing suffix`() {
    "file".ensureEndsWith(".kt") shouldBeEqualTo "file.kt"
  }

  @Test
  fun `trimLeadingSpaces preserves newlines`() {
    val input = "  line1\n    line2\n  line3"
    val expected = "line1\nline2\nline3"
    input.trimLeadingSpaces() shouldBeEqualTo expected
  }

  @Test
  fun `trimLeadingSpaces with no leading spaces`() {
    "abc\ndef".trimLeadingSpaces() shouldBeEqualTo "abc\ndef"
  }

  @Test
  fun `obfuscate with default freq`() {
    val result = "abcdef".obfuscate()
    // freq=2 means index 0,2,4 are replaced with '*'
    result shouldBeEqualTo "*b*d*f"
  }

  @Test
  fun `obfuscate with freq 3`() {
    val result = "abcdef".obfuscate(3)
    // freq=3 means index 0,3 are replaced with '*'
    result shouldBeEqualTo "*bc*ef"
  }

  @Test
  fun `obfuscate empty string`() {
    "".obfuscate() shouldBeEqualTo ""
  }

  @Test
  fun `capitalizeFirstChar with mixed case`() {
    "hELLO".capitalizeFirstChar() shouldBeEqualTo "Hello"
  }

  @Test
  fun `capitalizeFirstChar with lowercase`() {
    "world".capitalizeFirstChar() shouldBeEqualTo "World"
  }

  @Test
  fun `capitalizeFirstChar with already capitalized`() {
    "Hello".capitalizeFirstChar() shouldBeEqualTo "Hello"
  }

  @Test
  fun `encode and decode round-trip`() {
    val original = "a b&c=d+e"
    original.encode().decode() shouldBeEqualTo original
  }

  @Test
  fun `encode special characters`() {
    val encoded = "hello world".encode()
    encoded shouldBeEqualTo "hello+world"
  }

  @Test
  fun `isNull and isNotNull with null`() {
    val value: String? = null
    value.isNull().shouldBeTrue()
    value.isNotNull().shouldBeFalse()
  }

  @Test
  fun `isNull and isNotNull with non-null`() {
    val value: String? = "hello"
    value.isNull().shouldBeFalse()
    value.isNotNull().shouldBeTrue()
  }

  @Test
  fun `lpad pads with zeros`() {
    42.lpad(5) shouldBeEqualTo "00042"
  }

  @Test
  fun `lpad with custom char`() {
    7.lpad(3, ' ') shouldBeEqualTo "  7"
  }

  @Test
  fun `rpad pads with zeros`() {
    42.rpad(5) shouldBeEqualTo "42000"
  }

  @Test
  fun `rpad with custom char`() {
    7.rpad(3, ' ') shouldBeEqualTo "7  "
  }
}
