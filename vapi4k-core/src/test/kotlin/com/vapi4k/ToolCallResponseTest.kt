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

import com.pambrose.common.json.toJsonElement
import com.vapi4k.common.AssistantId.Companion.toAssistantId
import com.vapi4k.common.SessionId.Companion.toSessionId
import com.vapi4k.dsl.vapi4k.InboundCallApplicationImpl
import com.vapi4k.responses.ToolCallResponseDto.Companion.getToolCallResponse
import com.vapi4k.server.RequestContextImpl
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.string.shouldContain

class ToolCallResponseTest : StringSpec() {
  private fun requestContextForTool(toolName: String): RequestContextImpl {
    val request =
      """
      {
        "message": {
          "type": "tool-calls",
          "toolCallList": [
            { "id": "call_1", "type": "function", "function": { "name": "$toolName", "arguments": {} } }
          ]
        }
      }
      """.trimIndent().toJsonElement()
    return RequestContextImpl(
      application = InboundCallApplicationImpl(),
      request = request,
      sessionId = "test-session".toSessionId(),
      assistantId = "test-assistant".toAssistantId(),
    )
  }

  init {
    // Exercises the onFailure branch of getToolCallResponse: an unknown tool throws
    // ("Tool not found"), which must be caught and recorded on the result rather than
    // propagated. Guards the getOrElse -> onFailure change.
    "getToolCallResponse records an error when the tool is not found" {
      val response = getToolCallResponse(requestContextForTool("missingTool"))
      val dto = response.messageResponse

      dto.results shouldHaveSize 1
      dto.results.first().error shouldContain "Tool not found"
      dto.error shouldContain "Tool not found"
    }
  }
}
