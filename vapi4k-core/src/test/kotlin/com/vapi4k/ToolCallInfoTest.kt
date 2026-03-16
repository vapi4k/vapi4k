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

import com.vapi4k.api.tools.ToolCall
import com.vapi4k.common.AssistantId.Companion.EMPTY_ASSISTANT_ID
import com.vapi4k.common.AssistantId.Companion.toAssistantId
import com.vapi4k.dsl.functions.ToolCallInfo
import com.vapi4k.dsl.functions.ToolCallInfo.Companion.appendAssistantId
import com.vapi4k.utils.ReflectionUtils.toolCallFunction
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldEndWith

class ToolCallInfoTest : StringSpec() {
  class DescribedService {
    @ToolCall("Returns the weather forecast")
    fun getWeather(city: String): String = "Sunny in $city"
  }

  class UndescribedService {
    @ToolCall
    fun lookupData(key: String): String = key
  }

  class NamedService {
    @ToolCall(description = "Gets temp", name = "get_temperature")
    fun getTemp(city: String): String = "72F in $city"
  }

  class NameOnlyService {
    @ToolCall(name = "custom_name")
    fun myFunc(): String = "result"
  }

  init {
    "llmName appends assistant ID suffix" {
      val assistantId = "asst-123".toAssistantId()
      val info = ToolCallInfo(assistantId, DescribedService().toolCallFunction)
      info.llmName.value shouldEndWith "_asst-123"
      info.llmName.value shouldContain "getWeather"
    }

    "llmDescription uses annotation description when present" {
      val info = ToolCallInfo(EMPTY_ASSISTANT_ID, DescribedService().toolCallFunction)
      info.llmDescription shouldBe "Returns the weather forecast"
    }

    "llmDescription falls back to function name when no description" {
      val info = ToolCallInfo(EMPTY_ASSISTANT_ID, UndescribedService().toolCallFunction)
      info.llmDescription shouldBe "lookupData"
    }

    "llmDescription uses name when name is set but description is empty" {
      val info = ToolCallInfo(EMPTY_ASSISTANT_ID, NameOnlyService().toolCallFunction)
      info.llmDescription shouldBe "custom_name"
    }

    "llmName uses custom name from annotation when provided" {
      val assistantId = "asst-1".toAssistantId()
      val info = ToolCallInfo(assistantId, NamedService().toolCallFunction)
      info.llmName.value shouldBe "get_temperature_asst-1"
    }

    "appendAssistantId with EMPTY_ASSISTANT_ID still appends separator" {
      val result = "funcName".appendAssistantId(EMPTY_ASSISTANT_ID)
      result shouldBe "funcName_"
    }

    "appendAssistantId with real assistantId" {
      val result = "myFunc".appendAssistantId("abc".toAssistantId())
      result shouldBe "myFunc_abc"
    }
  }
}
