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

import com.github.pambrose.common.json.intValue
import com.github.pambrose.common.json.jsonElementList
import com.github.pambrose.common.json.stringValue
import com.github.pambrose.common.json.toJsonElement
import com.vapi4k.api.assistant.AssistantClientMessageType
import com.vapi4k.api.assistant.AssistantServerMessageType
import com.vapi4k.api.assistant.FirstMessageModeType.ASSISTANT_SPEAKS_FIRST_WITH_MODEL_GENERATED_MODEL
import com.vapi4k.api.conditions.eq
import com.vapi4k.api.model.OpenAIModelType
import com.vapi4k.api.tools.ToolMessageType
import com.vapi4k.api.transcriber.DeepgramLanguageType
import com.vapi4k.api.transcriber.DeepgramModelType
import com.vapi4k.api.transcriber.GladiaModelType
import com.vapi4k.api.transcriber.TalkscriberModelType
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
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.serialization.json.JsonElement

class AssistantTest : StringSpec() {
  @Suppress("unused")
  private val configInit = Vapi4kConfigImpl()

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

  init {
    "testRegular" {
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

      jsonElement.firstMessageOfType(ToolMessageType.REQUEST_START)
        .stringValue("content") shouldBe "This is the test request start message"
    }

    "multiple application{} blocks" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "assistant{} was already called"
    }

    "application{} and squad{} blocks" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "assistant{} was already called"
    }

    "Missing application{} blocks" {
      shouldThrow<IllegalStateException> {
        assistantResponse(newRequestContext()) {
        }
      }.message shouldBe "assistantResponse{} is missing a call to assistant{}, assistantId{}, squad{}, or squadId{}"
    }

    "test reverse delay order" {
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
        stringValue("content") shouldBe delayedMessage
        intValue("timingMilliseconds") shouldBe 2000
      }
    }

    "test message with no millis" {
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
      jsonElement
        .firstMessageOfType(ToolMessageType.REQUEST_RESPONSE_DELAYED)
        .intValue("timingMilliseconds") shouldBe 99
    }

    "multiple message" {
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
        stringValue("content") shouldBe secondDelayedMessage
        intValue("timingMilliseconds") shouldBe 2000
      }
    }

    "multiple delay time" {
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

      jsonElement.firstMessageOfType(ToolMessageType.REQUEST_RESPONSE_DELAYED)
        .intValue("timingMilliseconds") shouldBe 1000
    }

    "multiple message multiple delay time" {
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
        stringValue("content") shouldBe secondDelayedMessage
        intValue("timingMilliseconds") shouldBe 1000
      }
    }

    "chicago illinois message" {
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

      chicagoStartMessage.stringValue("content") shouldBe chicagoIllinoisStartMessage
      chicagoCompleteMessage.stringValue("content") shouldBe chicagoIllinoisCompleteMessage
      chicagoFailedMessage.stringValue("content") shouldBe chicagoIllinoisFailedMessage
      chicagoDelayedMessage.stringValue("content") shouldBe chicagoIllinoisDelayedMessage
      chicagoDelayedMessage.intValue("timingMilliseconds") shouldBe 2000
      defaultStartMessage.stringValue("content") shouldBe startMessage
      defaultCompleteMessage.stringValue("content") shouldBe completeMessage
      defaultFailedMessage.stringValue("content") shouldBe failedMessage
      defaultDelayedMessage.stringValue("content") shouldBe delayedMessage
      defaultDelayedMessage.intValue("timingMilliseconds") shouldBe 1000
    }

    "Missing message" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldContain "must have at least one message"
    }

    "error on duplicate reverse conditions" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldContain "duplicates an existing condition{}"
    }

    "check non-default FirstMessageModeType values" {
      val assistant =
        assistantResponse(newRequestContext()) {
          assistant {
            firstMessageMode = ASSISTANT_SPEAKS_FIRST_WITH_MODEL_GENERATED_MODEL
            openAIModel {
              modelType = OpenAIModelType.GPT_3_5_TURBO
            }
          }
        }

      val msg = assistant.toJsonElement().stringValue("messageResponse.assistant.firstMessageMode")
      msg shouldBe ASSISTANT_SPEAKS_FIRST_WITH_MODEL_GENERATED_MODEL.desc
    }

    "check assistant client messages 1" {
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
      element.assistantClientMessages.size shouldBe 9
    }

    "check assistant client messages 2" {
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
      element.assistantClientMessages.size shouldBe 8
    }

    "check assistant server messages 1" {
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
      element.assistantServerMessages.size shouldBe 8
    }

    "check assistant server messages 2" {
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
      element.assistantServerMessages.size shouldBe 7
    }

    "multiple deepgram transcriber blocks" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "deepgramTranscriber{} requires a transcriberLanguage or customLanguagevalue"
    }

    "multiple gladia transcriber blocks" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "gladiaTranscriber{} requires a transcriberLanguage or customLanguage value"
    }

    "multiple talkscriber transcriber blocks" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "talkscriberTranscriber{} requires a transcriberLanguage or customLanguage value"
    }

    "multiple transcriber blocks" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "talkscriberTranscriber{} requires a transcriberLanguage or customLanguage value"
    }

    "deepgram transcriber enum value" {
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
      je.stringValue("messageResponse.assistant.transcriber.language") shouldBe DeepgramLanguageType.GERMAN.desc
    }

    "deepgram transcriber custom value" {
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
      jsonElement.stringValue("messageResponse.assistant.transcriber.language") shouldBe "zzz"
    }

    "deepgram transcriber conflicting values" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "deepgramTranscriber{} cannot have both transcriberLanguage and customLanguage values"
    }

    "missing model decl" {
      shouldThrow<IllegalStateException> {
        assistantResponse(newRequestContext()) {
          assistant {
            firstMessage = "Something"
          }
        }
      }.message shouldBe "An assistant{} requires a model{} decl"
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
