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

import com.vapi4k.common.AssistantId.Companion.getAssistantIdFromSuffix
import com.vapi4k.common.AssistantId.Companion.toAssistantId
import com.vapi4k.common.CacheKey.Companion.cacheKeyValue
import com.vapi4k.common.SessionId.Companion.EMPTY_SESSION_ID
import com.vapi4k.common.SessionId.Companion.toSessionId
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ValueClassTest : StringSpec() {
  init {
    "CacheKey composition from sessionId and assistantId" {
      val sessionId = "session123".toSessionId()
      val assistantId = "assistant456".toAssistantId()
      val cacheKey = cacheKeyValue(sessionId, assistantId)
      cacheKey.value shouldBe "session123_assistant456"
    }

    "CacheKey equality for same session and assistant" {
      val sessionId = "sess1".toSessionId()
      val assistantId = "asst1".toAssistantId()
      val key1 = cacheKeyValue(sessionId, assistantId)
      val key2 = cacheKeyValue(sessionId, assistantId)
      key1 shouldBe key2
    }

    "CacheKey differs for different sessions" {
      val assistantId = "asst1".toAssistantId()
      val key1 = cacheKeyValue("sess1".toSessionId(), assistantId)
      val key2 = cacheKeyValue("sess2".toSessionId(), assistantId)
      key1 shouldNotBe key2
    }

    "AssistantId getAssistantIdFromSuffix extracts correctly" {
      val result = "functionName_assistantXYZ".getAssistantIdFromSuffix()
      result.value shouldBe "assistantXYZ"
    }

    "AssistantId getAssistantIdFromSuffix with multiple separators" {
      val result = "some_function_name_asst123".getAssistantIdFromSuffix()
      result.value shouldBe "asst123"
    }

    "SessionId toString returns value" {
      val sessionId = "my-session".toSessionId()
      sessionId.toString() shouldBe "my-session"
    }

    "EMPTY_SESSION_ID has empty value" {
      EMPTY_SESSION_ID.value shouldBe ""
    }

    "AssistantId toString returns value" {
      val assistantId = "my-assistant".toAssistantId()
      assistantId.toString() shouldBe "my-assistant"
    }
  }
}
