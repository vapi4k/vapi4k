/*
 * Copyright © 2024 Matthew Ambrose (mattbobambrose@gmail.com)
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

import com.vapi4k.api.assistant.enums.AssistantClientMessageType
import com.vapi4k.api.assistant.enums.AssistantServerMessageType
import com.vapi4k.api.assistant.enums.FirstMessageModeType.ASSISTANT_SPEAKS_FIRST_WITH_MODEL_GENERATED_MODEL
import com.vapi4k.api.conditions.eq
import com.vapi4k.api.json.intValue
import com.vapi4k.api.json.jsonElementList
import com.vapi4k.api.json.stringValue
import com.vapi4k.api.json.toJsonElement
import com.vapi4k.api.model.enums.OpenAIModelType
import com.vapi4k.api.tools.enums.ToolMessageType
import com.vapi4k.api.transcriber.enums.DeepgramLanguageType
import com.vapi4k.api.transcriber.enums.DeepgramModelType
import com.vapi4k.api.transcriber.enums.GladiaModelType
import com.vapi4k.api.transcriber.enums.TalkscriberModelType
import com.vapi4k.common.AssistantId.Companion.toAssistantId
import com.vapi4k.common.SessionId.Companion.toSessionId
import com.vapi4k.dsl.vapi4k.ApplicationType.INBOUND_CALL
import com.vapi4k.dsl.vapi4k.InboundCallApplicationImpl
import com.vapi4k.dsl.vapi4k.Vapi4kConfigImpl
import com.vapi4k.server.RequestContextImpl
import com.vapi4k.utils.DslUtils.getRandomSecret
import com.vapi4k.utils.JsonFilenames.JSON_ASSISTANT_REQUEST
import com.vapi4k.utils.assistantResponse
import com.vapi4k.utils.firstMessageOfType
import com.vapi4k.utils.withTestApplication
import kotlinx.serialization.json.JsonElement
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import kotlin.test.Test

class AssistantTest {
  init {
    Vapi4kConfigImpl()
  }

  private val messageOne = "Hi there test"
  private val sysMessage = "You are the test systemMessage voice"
  private val startMessage = "This is the test request start message"
  private val completeMessage = "This is the test request complete message"
  private val failedMessage = "This is the test request failed message"
  private val delayedMessage = "This is the test request delayed message"
  private val secondStartMessage = "This is the second test request start message"
  private val secondCompleteMessage = "This is the second test request complete message"
  private val secondFailedMessage = "This is the second test request failed message"
  private val secondDelayedMessage = "This is the second test request delayed message"
  private val chicagoIllinoisStartMessage = "This is the Chicago Illinois request start message"
  private val chicagoIllinoisCompleteMessage = "This is the Chicago Illinois request complete message"
  private val chicagoIllinoisFailedMessage = "This is the Chicago Illinois request failed message"
  private val chicagoIllinoisDelayedMessage = "This is the Chicago Illinois request delayed message"

  val JsonElement.assistantClientMessages get() = jsonElementList("messageResponse.assistant.clientMessages")
  val JsonElement.assistantServerMessages get() = jsonElementList("messageResponse.assistant.serverMessages")

  @Test
  fun testRegular() {
    val (response, jsonElement) =
      withTestApplication(INBOUND_CALL, JSON_ASSISTANT_REQUEST) {
        assistant {
          firstMessage = messageOne
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO

            systemMessage = sysMessage
            tools {
              serviceTool(FavoriteFoodService()) {
                requestStartMessage {
                  content = startMessage
                }
                requestCompleteMessage {
                  content = completeMessage
                }
                requestFailedMessage {
                  content = failedMessage
                }
                requestDelayedMessage {
                  content = delayedMessage
                  timingMilliseconds = 2000
                }
              }
            }
          }
        }
      }

    assertEquals(
      "This is the test request start message",
      jsonElement.firstMessageOfType(ToolMessageType.REQUEST_START).stringValue("content"),
    )
  }

  @Test
  fun `multiple application{} blocks`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          firstMessage = "Something"
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
          }
        }
        assistant {
          firstMessage = "Something"
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
          }
        }
      }
    }.also {
      assertEquals("assistant{} was already called", it.message)
    }
  }

  @Test
  fun `application{} and squad{} blocks`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          firstMessage = "Something"
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
          }
        }
        assistantId {
          id = "12345"
        }
      }
    }.also {
      assertEquals("assistant{} was already called", it.message)
    }
  }

  @Test
  fun `Missing application{} blocks`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
      }
    }.also {
      assertEquals(
        "assistantResponse{} is missing a call to assistant{}, assistantId{}, squad{}, or squadId{}",
        it.message,
      )
    }
  }

  @Test
  fun `test reverse delay order`() {
    val (response, jsonElement) =
      withTestApplication(INBOUND_CALL, JSON_ASSISTANT_REQUEST) {
        assistant {
          firstMessage = messageOne
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO

            systemMessage = sysMessage
            tools {
              serviceTool(FavoriteFoodService()) {
                requestStartMessage {
                  content = startMessage
                }
                requestCompleteMessage {
                  content = completeMessage
                }
                requestFailedMessage {
                  content = failedMessage
                }
                requestDelayedMessage {
                  content = delayedMessage
                  timingMilliseconds = 2000
                }
              }
            }
          }
        }
      }
    with(jsonElement.firstMessageOfType(ToolMessageType.REQUEST_RESPONSE_DELAYED)) {
      assertEquals(delayedMessage, stringValue("content"))
      assertEquals(2000, intValue("timingMilliseconds"))
    }
  }

  @Test
  fun `test message with no millis`() {
    val (response, jsonElement) =
      withTestApplication(INBOUND_CALL, JSON_ASSISTANT_REQUEST) {
        assistant {
          firstMessage = messageOne
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
            systemMessage = sysMessage
            tools {
              serviceTool(FavoriteFoodService()) {
                requestStartMessage {
                  content = startMessage
                }
                requestCompleteMessage {
                  content = completeMessage
                }
                requestFailedMessage {
                  content = failedMessage
                }
                requestDelayedMessage {
                  content = delayedMessage
                  timingMilliseconds = 99
                }
              }
            }
          }
        }
      }
    assertEquals(
      99,
      jsonElement
        .firstMessageOfType(ToolMessageType.REQUEST_RESPONSE_DELAYED)
        .intValue("timingMilliseconds"),
    )
  }

  @Test
  fun `multiple message`() {
    val (response, jsonElement) =
      withTestApplication(INBOUND_CALL, JSON_ASSISTANT_REQUEST) {
        assistant {
          firstMessage = messageOne
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
            systemMessage = sysMessage
            tools {
              serviceTool(FavoriteFoodService()) {
                requestStartMessage {
                  content = startMessage
                }
                requestCompleteMessage {
                  content = completeMessage
                }
                requestFailedMessage {
                  content = failedMessage
                }
                requestDelayedMessage {
                  content = delayedMessage
                  content = secondDelayedMessage
                  timingMilliseconds = 2000
                }
              }
            }
          }
        }
      }

    with(jsonElement.firstMessageOfType(ToolMessageType.REQUEST_RESPONSE_DELAYED)) {
      assertEquals(secondDelayedMessage, stringValue("content"))
      assertEquals(2000, intValue("timingMilliseconds"))
    }
  }

  @Test
  fun `multiple delay time`() {
    val (response, jsonElement) =
      withTestApplication(INBOUND_CALL, JSON_ASSISTANT_REQUEST) {
        assistant {
          firstMessage = messageOne
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO

            systemMessage = sysMessage
            tools {
              serviceTool(FavoriteFoodService()) {
                requestStartMessage {
                  content = startMessage
                }
                requestCompleteMessage {
                  content = completeMessage
                }
                requestFailedMessage {
                  content = failedMessage
                }
                requestDelayedMessage {
                  content = delayedMessage
                  timingMilliseconds = 2000
                  timingMilliseconds = 1000
                }
              }
            }
          }
        }
      }

    // println(jsonElement.toJsonString())

    assertEquals(
      1000,
      jsonElement.firstMessageOfType(ToolMessageType.REQUEST_RESPONSE_DELAYED).intValue("timingMilliseconds"),
    )
  }

  @Test
  fun `multiple message multiple delay time`() {
    val (response, jsonElement) =
      withTestApplication(INBOUND_CALL, JSON_ASSISTANT_REQUEST) {
        assistant {
          firstMessage = messageOne
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
            systemMessage = sysMessage
            tools {
              serviceTool(FavoriteFoodService()) {
                requestStartMessage {
                  content = startMessage
                }
                requestCompleteMessage {
                  content = completeMessage
                }
                requestFailedMessage {
                  content = failedMessage
                }
                requestDelayedMessage {
                  content = delayedMessage
                  content = secondDelayedMessage
                  timingMilliseconds = 2000
                  timingMilliseconds = 1000
                }
              }
            }
          }
        }
      }
    with(jsonElement.firstMessageOfType(ToolMessageType.REQUEST_RESPONSE_DELAYED)) {
      assertEquals(secondDelayedMessage, stringValue("content"))
      assertEquals(1000, intValue("timingMilliseconds"))
    }
  }

  @Test
  fun `chicago illinois message`() {
    val (response, jsonElement) =
      withTestApplication(INBOUND_CALL, JSON_ASSISTANT_REQUEST) {
        assistant {
          firstMessage = messageOne
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
            systemMessage = sysMessage
            tools {
              serviceTool(FavoriteFoodService()) {
                condition("city" eq "Chicago", "state" eq "Illinois") {
                  requestStartMessage {
                    content = chicagoIllinoisStartMessage
                  }
                  requestCompleteMessage {
                    content = chicagoIllinoisCompleteMessage
                  }
                  requestFailedMessage {
                    content = chicagoIllinoisFailedMessage
                  }
                  requestDelayedMessage {
                    content = chicagoIllinoisDelayedMessage
                    timingMilliseconds = 2000
                  }
                }
                requestStartMessage {
                  content = startMessage
                }
                requestCompleteMessage {
                  content = completeMessage
                }
                requestFailedMessage {
                  content = failedMessage
                }
                requestDelayedMessage {
                  content = delayedMessage
                  timingMilliseconds = 1000
                }
              }
            }
          }
        }
      }

    val chicagoCity = "city" eq "Chicago"
    val illinoisState = "state" eq "Illinois"

    val chicagoStartMessage =
      jsonElement.firstMessageOfType(ToolMessageType.REQUEST_START, chicagoCity, illinoisState)

    val chicagoCompleteMessage =
      jsonElement.firstMessageOfType(ToolMessageType.REQUEST_COMPLETE, chicagoCity, illinoisState)

    val chicagoFailedMessage =
      jsonElement.firstMessageOfType(ToolMessageType.REQUEST_FAILED, chicagoCity, illinoisState)

    val chicagoDelayedMessage =
      jsonElement.firstMessageOfType(ToolMessageType.REQUEST_RESPONSE_DELAYED, chicagoCity, illinoisState)

    val defaultStartMessage = jsonElement.firstMessageOfType(ToolMessageType.REQUEST_START)
    val defaultCompleteMessage = jsonElement.firstMessageOfType(ToolMessageType.REQUEST_COMPLETE)
    val defaultFailedMessage = jsonElement.firstMessageOfType(ToolMessageType.REQUEST_FAILED)
    val defaultDelayedMessage = jsonElement.firstMessageOfType(ToolMessageType.REQUEST_RESPONSE_DELAYED)

    assertEquals(chicagoIllinoisStartMessage, chicagoStartMessage.stringValue("content"))
    assertEquals(chicagoIllinoisCompleteMessage, chicagoCompleteMessage.stringValue("content"))
    assertEquals(chicagoIllinoisFailedMessage, chicagoFailedMessage.stringValue("content"))
    assertEquals(chicagoIllinoisDelayedMessage, chicagoDelayedMessage.stringValue("content"))
    assertEquals(2000, chicagoDelayedMessage.intValue("timingMilliseconds"))
    assertEquals(startMessage, defaultStartMessage.stringValue("content"))
    assertEquals(completeMessage, defaultCompleteMessage.stringValue("content"))
    assertEquals(failedMessage, defaultFailedMessage.stringValue("content"))
    assertEquals(delayedMessage, defaultDelayedMessage.stringValue("content"))
    assertEquals(1000, defaultDelayedMessage.intValue("timingMilliseconds"))
  }

  @Test
  fun `Missing message`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          firstMessage = messageOne
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
            systemMessage = sysMessage
            tools {
              serviceTool(FavoriteFoodService()) {
                condition("city" eq "Chicago", "state" eq "Illinois") {
                }
              }
            }
          }
        }
      }
    }.also {
      assert(it.message.orEmpty().contains("must have at least one message"))
    }
  }

  @Test
  fun `error on duplicate reverse conditions`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          firstMessage = messageOne
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO

            systemMessage = sysMessage
            tools {
              serviceTool(FavoriteFoodService()) {
                condition("city" eq "Chicago", "state" eq "Illinois") {
                  requestStartMessage {
                    content = chicagoIllinoisStartMessage
                  }
                  requestCompleteMessage {
                    content = chicagoIllinoisCompleteMessage
                  }
                  requestFailedMessage {
                    content = chicagoIllinoisFailedMessage
                  }
                  requestDelayedMessage {
                    content = chicagoIllinoisDelayedMessage
                    timingMilliseconds = 2000
                  }
                }
                condition("state" eq "Illinois", "city" eq "Chicago") {
                  requestStartMessage {
                    content = chicagoIllinoisStartMessage + "2"
                  }
                  requestCompleteMessage {
                    content = chicagoIllinoisCompleteMessage + "2"
                  }
                  requestFailedMessage {
                    content = chicagoIllinoisFailedMessage + "2"
                  }
                  requestDelayedMessage {
                    content = chicagoIllinoisDelayedMessage + "2"
                    timingMilliseconds = 3000
                  }
                }
                requestStartMessage {
                  content = startMessage
                }
                requestCompleteMessage {
                  content = completeMessage
                }
                requestFailedMessage {
                  content = failedMessage
                }
                requestDelayedMessage {
                  content = delayedMessage
                  timingMilliseconds = 1000
                }
              }
            }
          }
        }
      }
    }.also {
      assert(it.message.orEmpty().contains("duplicates an existing condition{}"))
    }
  }

  @Test
  fun `check non-default FirstMessageModeType values`() {
    val assistant =
      assistantResponse(newRequestContext()) {
        assistant {
          firstMessageMode = ASSISTANT_SPEAKS_FIRST_WITH_MODEL_GENERATED_MODEL
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
          }
        }
      }

    val element = assistant.toJsonElement()
    assertEquals(
      ASSISTANT_SPEAKS_FIRST_WITH_MODEL_GENERATED_MODEL.desc,
      element.stringValue("messageResponse.assistant.firstMessageMode"),
    )
  }

  @Test
  fun `check assistant client messages 1`() {
    val assistant =
      assistantResponse(newRequestContext()) {
        assistant {
          clientMessages -= AssistantClientMessageType.HANG
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
          }
        }
      }

    val element = assistant.toJsonElement()
    assertEquals(9, element.assistantClientMessages.size)
  }

  @Test
  fun `check assistant client messages 2`() {
    val assistant =
      assistantResponse(newRequestContext()) {
        assistant {
          clientMessages -= setOf(AssistantClientMessageType.HANG, AssistantClientMessageType.STATUS_UPDATE)
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
          }
        }
      }

    val element = assistant.toJsonElement()
    assertEquals(8, element.assistantClientMessages.size)
  }

  @Test
  fun `check assistant server messages 1`() {
    val assistant =
      assistantResponse(newRequestContext()) {
        assistant {
          firstMessage = "Something"
          serverMessages -= AssistantServerMessageType.HANG
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
          }
        }
      }

    val element = assistant.toJsonElement()
    assertEquals(8, element.assistantServerMessages.size)
  }

  @Test
  fun `check assistant server messages 2`() {
    val assistant =
      assistantResponse(newRequestContext()) {
        assistant {
          serverMessages -= setOf(AssistantServerMessageType.HANG, AssistantServerMessageType.SPEECH_UPDATE)
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
          }
        }
      }

    val element = assistant.toJsonElement()
    assertEquals(7, element.assistantServerMessages.size)
  }

  @Test
  fun `multiple deepgram transcriber blocks`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          deepgramTranscriber {
            transcriberModel = DeepgramModelType.BASE
          }

          deepgramTranscriber {
            transcriberModel = DeepgramModelType.BASE
          }
        }
      }
    }.also {
      assertEquals("deepgramTranscriber{} requires a transcriberLanguage or customLanguagevalue", it.message)
    }
  }

  @Test
  fun `multiple gladia transcriber blocks`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          gladiaTranscriber {
            transcriberModel = GladiaModelType.FAST
          }

          gladiaTranscriber {
            transcriberModel = GladiaModelType.FAST
          }
        }
      }
    }.also {
      assertEquals("gladiaTranscriber{} requires a transcriberLanguage or customLanguage value", it.message)
    }
  }

  @Test
  fun `multiple talkscriber transcriber blocks`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          talkscriberTranscriber {
            transcriberModel = TalkscriberModelType.WHISPER
          }

          talkscriberTranscriber {
            transcriberModel = TalkscriberModelType.WHISPER
          }
        }
      }
    }.also {
      assertEquals("talkscriberTranscriber{} requires a transcriberLanguage or customLanguage value", it.message)
    }
  }

  @Test
  fun `multiple transcriber blocks`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          talkscriberTranscriber {
            transcriberModel = TalkscriberModelType.WHISPER
          }

          gladiaTranscriber {
            transcriberModel = GladiaModelType.FAST
          }
        }
      }
    }.also {
      assertEquals("talkscriberTranscriber{} requires a transcriberLanguage or customLanguage value", it.message)
    }
  }

  @Test
  fun `deepgram transcriber enum value`() {
    val assistant =
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
          }
          deepgramTranscriber {
            transcriberModel = DeepgramModelType.BASE
            transcriberLanguage = DeepgramLanguageType.GERMAN
          }
        }
      }
    val je = assistant.toJsonElement()
    assertEquals(
      DeepgramLanguageType.GERMAN.desc,
      je.stringValue("messageResponse.assistant.transcriber.language"),
    )
  }

  @Test
  fun `deepgram transcriber custom value`() {
    val assistant =
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
          }

          deepgramTranscriber {
            transcriberModel = DeepgramModelType.BASE
            customLanguage = "zzz"
          }
        }
      }
    val jsonElement = assistant.toJsonElement()
    assertEquals(
      "zzz",
      jsonElement.stringValue("messageResponse.assistant.transcriber.language"),
    )
  }

  @Test
  fun `deepgram transcriber conflicting values`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
          }

          deepgramTranscriber {
            transcriberModel = DeepgramModelType.BASE
            transcriberLanguage = DeepgramLanguageType.GERMAN
            customLanguage = "zzz"
          }
        }
      }
    }.also {
      assertEquals(
        "deepgramTranscriber{} cannot have both transcriberLanguage and customLanguage values",
        it.message,
      )
    }
  }

  @Test
  fun `missing model decl`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          firstMessage = "Something"
        }
      }
    }.also {
      assertEquals("An assistant{} requires a model{} decl", it.message)
    }
  }

  companion object {
    private val ASSISTANT_REQUEST = """
    {
    "message": {
        "type": "assistant-request",
        "call": {
            "id": "00dbe917-37fd-4d3f-8cc0-ac6be0923f40",
            "orgId": "379a13ec-f40d-4055-8959-797c4ee1694s"
        },
        "timestamp": "2024-07-13T21:27:59.870Z"
      }
    }
    """.toJsonElement()

    fun newRequestContext() =
      RequestContextImpl(
        InboundCallApplicationImpl(),
        ASSISTANT_REQUEST,
        getRandomSecret(8, 4, 4, 12).toSessionId(),
        getRandomSecret(8, 4, 4, 12).toAssistantId(),
      )
  }
}
