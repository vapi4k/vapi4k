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

import com.vapi4k.common.AssistantId.Companion.toAssistantId
import com.vapi4k.common.FunctionName.Companion.toFunctionName
import com.vapi4k.common.SessionId.Companion.toSessionId
import com.vapi4k.dsl.functions.FunctionInfo
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty

class FunctionInfoTest : StringSpec() {
  init {
    "containsFunction returns false for unknown name" {
      val info = FunctionInfo("s1".toSessionId(), "a1".toAssistantId())
      info.containsFunction("missing_a1".toFunctionName()) shouldBe false
    }

    "getFunction throws for unknown name" {
      val info = FunctionInfo("s1".toSessionId(), "a1".toAssistantId())
      shouldThrow<IllegalStateException> {
        info.getFunction("missing_a1".toFunctionName())
      }.message shouldBe "Function not found: \"missing_a1\""
    }

    "getFunctionOrNull returns null for unknown name" {
      val info = FunctionInfo("s1".toSessionId(), "a1".toAssistantId())
      info.getFunctionOrNull("missing_a1".toFunctionName()) shouldBe null
    }

    "size reflects number of added functions" {
      val info = FunctionInfo("s1".toSessionId(), "a1".toAssistantId())
      info.size shouldBe 0
    }

    "toFunctionInfoDto includes created and age" {
      val info = FunctionInfo("s1".toSessionId(), "a1".toAssistantId())
      val dto = info.toFunctionInfoDto()
      dto.created.shouldNotBeEmpty()
      dto.age.shouldNotBeEmpty()
    }
  }
}
