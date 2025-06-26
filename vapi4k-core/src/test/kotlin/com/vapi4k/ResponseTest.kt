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

import com.github.pambrose.common.json.toJsonString
import com.vapi4k.AssistantTest.Companion.newRequestContext
import com.vapi4k.api.model.AnthropicModelType
import com.vapi4k.api.transcriber.DeepgramModelType
import com.vapi4k.dtos.api.destination.SipDestinationDto
import com.vapi4k.utils.assistantResponse
import kotlin.test.Test

class ResponseTest {
  @Test
  fun testGetTwoSquadIds() {
    val requestContext = newRequestContext()
    val assistant =
      assistantResponse(requestContext) {
        assistant {
          anthropicModel {
            modelType = AnthropicModelType.CLAUDE_3_OPUS
//          maxTokens = 99
            userMessage = "Hello"
            userMessage += " Hello2"
            systemMessage = "Hello4"
            systemMessage += " Hello5"

            knowledgeBase {
              fileIds += listOf("eeeee", "ffff")
              topK = 5.3
            }
          }

          deepgramTranscriber {
            transcriberModel = DeepgramModelType.BASE
            customLanguage = "zzz"
          }
        }
      }

    val dest =
      assistantResponse(requestContext) {
        sipDestination {
          sipUri = "sip:wedwedwed"
          message = "Hello"
        }
      }

    println((dest.messageResponse.destination as SipDestinationDto).toJsonString())
  }
}
