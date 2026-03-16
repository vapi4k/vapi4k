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
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
import kotlin.test.Test

class ValueClassTest {
  @Test
  fun `CacheKey composition from sessionId and assistantId`() {
    val sessionId = "session123".toSessionId()
    val assistantId = "assistant456".toAssistantId()
    val cacheKey = cacheKeyValue(sessionId, assistantId)
    cacheKey.value shouldBeEqualTo "session123_assistant456"
  }

  @Test
  fun `CacheKey equality for same session and assistant`() {
    val sessionId = "sess1".toSessionId()
    val assistantId = "asst1".toAssistantId()
    val key1 = cacheKeyValue(sessionId, assistantId)
    val key2 = cacheKeyValue(sessionId, assistantId)
    key1 shouldBeEqualTo key2
  }

  @Test
  fun `CacheKey differs for different sessions`() {
    val assistantId = "asst1".toAssistantId()
    val key1 = cacheKeyValue("sess1".toSessionId(), assistantId)
    val key2 = cacheKeyValue("sess2".toSessionId(), assistantId)
    (key1 != key2).shouldBeTrue()
  }

  @Test
  fun `AssistantId getAssistantIdFromSuffix extracts correctly`() {
    val result = "functionName_assistantXYZ".getAssistantIdFromSuffix()
    result.value shouldBeEqualTo "assistantXYZ"
  }

  @Test
  fun `AssistantId getAssistantIdFromSuffix with multiple separators`() {
    val result = "some_function_name_asst123".getAssistantIdFromSuffix()
    result.value shouldBeEqualTo "asst123"
  }

  @Test
  fun `SessionId toString returns value`() {
    val sessionId = "my-session".toSessionId()
    sessionId.toString() shouldBeEqualTo "my-session"
  }

  @Test
  fun `EMPTY_SESSION_ID has empty value`() {
    EMPTY_SESSION_ID.value shouldBeEqualTo ""
  }

  @Test
  fun `AssistantId toString returns value`() {
    val assistantId = "my-assistant".toAssistantId()
    assistantId.toString() shouldBeEqualTo "my-assistant"
  }
}
