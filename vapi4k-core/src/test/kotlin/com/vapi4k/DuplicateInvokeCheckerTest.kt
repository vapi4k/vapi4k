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

import com.vapi4k.common.DuplicateInvokeChecker
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DuplicateInvokeCheckerTest : StringSpec() {
  init {
    "wasCalled is false before any call" {
      val checker = DuplicateInvokeChecker()
      checker.wasCalled shouldBe false
    }

    "first call succeeds and sets wasCalled" {
      val checker = DuplicateInvokeChecker()
      checker.check("assistant{} was already called")
      checker.wasCalled shouldBe true
    }

    "second call throws with first message" {
      val checker = DuplicateInvokeChecker()
      checker.check("assistant{} was already called")
      shouldThrow<IllegalStateException> {
        checker.check("this message is ignored")
      }.message shouldBe "assistant{} was already called"
    }

    "third call also throws with first message" {
      val checker = DuplicateInvokeChecker()
      checker.check("first error message")
      shouldThrow<IllegalStateException> {
        checker.check("second")
      }.message shouldBe "first error message"
      shouldThrow<IllegalStateException> {
        checker.check("third")
      }.message shouldBe "first error message"
    }
  }
}
