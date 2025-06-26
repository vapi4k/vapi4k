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

import com.github.pambrose.common.json.jsonElementList
import com.github.pambrose.common.json.stringValue
import com.vapi4k.api.model.OpenAIModelType
import com.vapi4k.dsl.vapi4k.ApplicationType.INBOUND_CALL
import com.vapi4k.utils.JsonFilenames
import com.vapi4k.utils.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class BaseToolMessageTest {
  @Test
  fun `toolMessageStart test`() {
    val (response, jsonElement) =
      withTestApplication(INBOUND_CALL, JsonFilenames.JSON_ASSISTANT_REQUEST) {
        squad {
          members {
            member {
              assistant {
                name = "assistant 1"
                firstMessage = "I'm assistant 1"

                openAIModel {
                  modelType = OpenAIModelType.GPT_4_TURBO
                  tools {
                    serviceTool(WeatherLookupService1()) {
                      requestStartMessage {
                        content = "tool 1 start message"
                      }
                      requestCompleteMessage {
                        content = "tool 1 complete message"
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }

    assertEquals(200, response.status.value)
    val assistantTools =
      jsonElement
        .jsonElementList("messageResponse.squad.members")
        .first()
        .jsonElementList("assistant.model.tools")
    assertEquals(
      "tool 1 start message",
      assistantTools.first().jsonElementList("messages").first().stringValue("content"),
    )
  }
}
