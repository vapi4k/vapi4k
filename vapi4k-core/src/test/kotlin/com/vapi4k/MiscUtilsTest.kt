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

import com.vapi4k.utils.MiscUtils.appendQueryParams
import com.vapi4k.utils.MiscUtils.findFunction
import com.vapi4k.utils.MiscUtils.removeEnds
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class MiscUtilsTest : StringSpec() {
  class SampleClass {
    fun myMethod(): String = "hello"

    fun anotherMethod(): Int = 42
  }

  init {
    "removeEnds strips matching prefix and suffix" {
      "/path/".removeEnds("/") shouldBe "path"
    }

    "removeEnds with no matching ends" {
      "path".removeEnds("/") shouldBe "path"
    }

    "removeEnds with default parameter" {
      "/hello/".removeEnds() shouldBe "hello"
    }

    "removeEnds with same prefix and suffix character" {
      "\"quoted\"".removeEnds("\"") shouldBe "quoted"
    }

    "appendQueryParams adds first parameter with question mark" {
      val result = "http://example.com".appendQueryParams("key" to "value")
      result shouldBe "http://example.com?key=value"
    }

    "appendQueryParams adds second parameter with ampersand" {
      val result = "http://example.com?existing=1".appendQueryParams("key" to "value")
      result shouldBe "http://example.com?existing=1&key=value"
    }

    "appendQueryParams handles multiple parameters" {
      val result = "http://example.com".appendQueryParams("a" to "1", "b" to "2")
      result shouldContain "a=1"
      result shouldContain "b=2"
      result.count { it == '?' } shouldBe 1
    }

    "appendQueryParams encodes parameter values" {
      val result = "http://example.com".appendQueryParams("q" to "hello world")
      result shouldContain "q=hello+world"
    }

    "findFunction returns matching function" {
      val obj = SampleClass()
      val func = obj.findFunction("myMethod")
      func.name shouldBe "myMethod"
    }

    "findFunction throws for unknown method" {
      val obj = SampleClass()
      shouldThrow<IllegalStateException> {
        obj.findFunction("nonExistent")
      }.message shouldContain "not found"
    }

    "removeEnds with empty string input" {
      "".removeEnds("/") shouldBe ""
    }

    "removeEnds strips only matching prefix and suffix" {
      "/path".removeEnds("/") shouldBe "path"
      "path/".removeEnds("/") shouldBe "path"
    }

    "appendQueryParams encodes spaces as plus signs" {
      val result = "http://example.com".appendQueryParams("city" to "New York")
      result shouldContain "city=New+York"
    }
  }
}
