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

import com.vapi4k.utils.DslUtils.getRandomSecret
import com.vapi4k.utils.DslUtils.getRandomString
import com.vapi4k.utils.DslUtils.includeIf
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.matchers.string.shouldMatch

class DslUtilsTest : StringSpec() {
  init {
    "getRandomString produces expected length" {
      getRandomString(15) shouldHaveLength 15
    }

    "getRandomString produces unique values" {
      val a = getRandomString(20)
      val b = getRandomString(20)
      a shouldNotBe b
    }

    "getRandomString uses only alphanumeric characters" {
      val result = getRandomString(100)
      result shouldMatch Regex("[a-zA-Z0-9]+")
    }

    "getRandomString with default length" {
      getRandomString() shouldHaveLength 10
    }

    "includeIf returns string when condition is true" {
      "hello".includeIf(true) shouldBe "hello"
    }

    "includeIf returns empty string when condition is false" {
      "hello".includeIf(false) shouldBe ""
    }

    "includeIf returns otherWise when condition is false" {
      "hello".includeIf(false, "fallback") shouldBe "fallback"
    }

    "getRandomSecret with single length" {
      getRandomSecret(12) shouldHaveLength 12
    }

    "getRandomSecret with multiple segments" {
      val result = getRandomSecret(5, 3, 4)
      result.split("-").size shouldBe 3
      result.split("-")[0] shouldHaveLength 5
      result.split("-")[1] shouldHaveLength 3
      result.split("-")[2] shouldHaveLength 4
    }

    "getRandomSecret with custom separator" {
      val result = getRandomSecret(5, 5, separator = ".")
      result.split(".").size shouldBe 2
    }
  }
}
