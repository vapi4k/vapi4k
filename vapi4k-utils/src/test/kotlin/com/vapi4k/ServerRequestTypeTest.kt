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

import com.github.pambrose.common.json.toJsonElement
import com.vapi4k.api.vapi4k.ServerRequestType
import com.vapi4k.api.vapi4k.ServerRequestType.Companion.isAssistantRequest
import com.vapi4k.api.vapi4k.ServerRequestType.Companion.isEndOfCallReport
import com.vapi4k.api.vapi4k.ServerRequestType.Companion.isToolCall
import com.vapi4k.api.vapi4k.ServerRequestType.Companion.serverRequestType
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import kotlin.test.Test

class ServerRequestTypeTest {
  @Test
  fun `serverRequestType parses assistant-request`() {
    val json = """{"message": {"type": "assistant-request"}}""".toJsonElement()
    json.serverRequestType shouldBeEqualTo ServerRequestType.ASSISTANT_REQUEST
  }

  @Test
  fun `serverRequestType parses end-of-call-report`() {
    val json = """{"message": {"type": "end-of-call-report"}}""".toJsonElement()
    json.serverRequestType shouldBeEqualTo ServerRequestType.END_OF_CALL_REPORT
  }

  @Test
  fun `serverRequestType parses tool-calls`() {
    val json = """{"message": {"type": "tool-calls"}}""".toJsonElement()
    json.serverRequestType shouldBeEqualTo ServerRequestType.TOOL_CALL
  }

  @Test
  fun `serverRequestType parses function-call`() {
    val json = """{"message": {"type": "function-call"}}""".toJsonElement()
    json.serverRequestType shouldBeEqualTo ServerRequestType.FUNCTION_CALL
  }

  @Test
  fun `serverRequestType returns UNKNOWN for invalid type`() {
    val json = """{"message": {"type": "does-not-exist"}}""".toJsonElement()
    json.serverRequestType shouldBeEqualTo ServerRequestType.UNKNOWN_REQUEST_TYPE
  }

  @Test
  fun `isAssistantRequest returns true for assistant-request`() {
    val json = """{"message": {"type": "assistant-request"}}""".toJsonElement()
    json.isAssistantRequest().shouldBeTrue()
  }

  @Test
  fun `isAssistantRequest returns false for other types`() {
    val json = """{"message": {"type": "tool-calls"}}""".toJsonElement()
    json.isAssistantRequest().shouldBeFalse()
  }

  @Test
  fun `isEndOfCallReport returns true for end-of-call-report`() {
    val json = """{"message": {"type": "end-of-call-report"}}""".toJsonElement()
    json.isEndOfCallReport().shouldBeTrue()
  }

  @Test
  fun `isToolCall returns true for tool-calls`() {
    val json = """{"message": {"type": "tool-calls"}}""".toJsonElement()
    json.isToolCall().shouldBeTrue()
  }

  @Test
  fun `all ServerRequestType entries have unique desc values`() {
    val descs = ServerRequestType.entries.map { it.desc }
    descs.size shouldBeEqualTo descs.toSet().size
  }

  @Test
  fun `serverRequestType parses all known types`() {
    ServerRequestType.entries
      .filter { it != ServerRequestType.UNKNOWN_REQUEST_TYPE }
      .forEach { type ->
        val json = """{"message": {"type": "${type.desc}"}}""".toJsonElement()
        json.serverRequestType shouldBeEqualTo type
      }
  }
}
