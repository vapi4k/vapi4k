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
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test

class DuplicateInvokeCheckerTest {
  @Test
  fun `wasCalled is false before any call`() {
    val checker = DuplicateInvokeChecker()
    checker.wasCalled.shouldBeFalse()
  }

  @Test
  fun `first call succeeds and sets wasCalled`() {
    val checker = DuplicateInvokeChecker()
    checker.check("assistant{} was already called")
    checker.wasCalled.shouldBeTrue()
  }

  @Test
  fun `second call throws with first message`() {
    val checker = DuplicateInvokeChecker()
    checker.check("assistant{} was already called")
    assertThrows(IllegalStateException::class.java) {
      checker.check("this message is ignored")
    }.also {
      it.message shouldBeEqualTo "assistant{} was already called"
    }
  }

  @Test
  fun `third call also throws with first message`() {
    val checker = DuplicateInvokeChecker()
    checker.check("first error message")
    assertThrows(IllegalStateException::class.java) {
      checker.check("second")
    }.also {
      it.message shouldBeEqualTo "first error message"
    }
    assertThrows(IllegalStateException::class.java) {
      checker.check("third")
    }.also {
      it.message shouldBeEqualTo "first error message"
    }
  }
}
