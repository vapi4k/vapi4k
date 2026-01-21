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

package simpledemo

import com.vapi4k.api.assistant.AssistantServerMessageType
import com.vapi4k.api.conditions.eq
import com.vapi4k.api.model.OpenAIModelType
import com.vapi4k.api.response.InboundCallAssistantResponse
import com.vapi4k.api.tools.ToolCall
import com.vapi4k.api.tools.ToolMessageRoleType
import com.vapi4k.api.toolservice.ToolCallService
import com.vapi4k.api.vapi4k.RequestContext

object SimpleAssistant {
  // Workflow feature is currently incomplete/experimental - commented out
  // fun InboundCallAssistantResponse.simpleWorkflowRequest() {
//    workflow {
//    val introNode = "introNode"
//    val tool1 = "tool1"
//    val endNode = "endNode"
//    nodes {
//      conversationNode(introNode) {
//        model = openAI.chatgpt4
//        edges {
//          tool1 onCondition "User says hello"
//          endNode onCondition "User says goodbye"
//        }
//      }
//      toolNode(tool1) {
//        edge(endNode)
//      }
//      conversationNode(endNode) {
//
//      }
//    }
//    edges(introNode) {
//      tool1 onCondition "User says hello"
//      endNode onCondition "User says goodbye"
//    }
//    edges(tool1) {
//      endNode onCondition "User says goodbye"
//    }
//
//    transcriber {
//
//    }
//    }
  // }

  fun InboundCallAssistantResponse.simpleAssistantRequest() {
    assistant {
      firstMessage = "Hi there!"

      openAIModel {
        modelType = OpenAIModelType.GPT_4_TURBO
        systemMessage = "You're a weather lookup service"

        tools {
          serviceTool(WeatherLookupService1()) {
            requestStartMessage {
              content = "Default request start weather lookup"
            }
          }

          externalTool {
            name = "wed"
            description = "a description"
            async = true

            parameters {
              parameter {
                name = "param1"
                description = "a description"
              }
              parameter {
                name = "param2"
                description = "a description"
              }
            }

            server {
              url = "yyy"
              secret = "456"
              timeoutSeconds = 5
            }
          }

//          dtmf(WeatherLookupService2(), WeatherLookupService2::getWeatherByCity2) {
//            requestStartMessage {
//              content = "Default request start weather lookup"
//            }
//
//            server {
//              url = "http://localhost:8080/toolCall"
//              secret = "456"
//              timeoutSeconds = 20
//            }
//          }

//          tool(
//            WeatherLookupService2(),
//            // WeatherLookupService2::getWeatherByCity2,
//            WeatherLookupService2::getWeatherByZipCode,
//          ) {
//            requestStartMessage {
//              content = "Default request start weather lookup"
//            }
//          }

//          transfer(WeatherLookupService2(), WeatherLookupService2::getWeatherByZipCode) {
//            assistantDestination {
//              description = "Transfer to assistant 2"
//            }
//          }
        }
      }

      serverMessages -= setOf(
        AssistantServerMessageType.CONVERSATION_UPDATE,
        AssistantServerMessageType.SPEECH_UPDATE,
      )
    }
  }

  class WeatherLookupService1 : ToolCallService() {
    @ToolCall("Look up the weather for a city")
    fun getWeatherByCity1(
      city: String,
      state: String,
    ) = "The weather in city $city and state $state is windy"

    override fun onToolCallComplete(
      requestContext: RequestContext,
      result: String,
    ) = requestCompleteMessages {
      condition("city" eq "Chicago", "state" eq "Illinois") {
        requestCompleteMessage {
          content = "The Chicago override request complete weather lookup"
        }
      }
      requestCompleteMessage {
        role = ToolMessageRoleType.ASSISTANT
        content = "The override request Complete Message weather has arrived"
      }
    }
  }

  class WeatherLookupService2 : ToolCallService() {
    @ToolCall("Look up the weather for a city")
    fun getWeatherByCity2(
      city: String,
      state: String,
    ) = "The weather in city $city and state $state is windy"

    @ToolCall("Look up the weather for a zip code")
    fun getWeatherByZipCode(zipCode: String) = "The weather in zip code $zipCode is rainy"

    override fun onToolCallComplete(
      requestContext: RequestContext,
      result: String,
    ) = requestCompleteMessages {
      condition("city" eq "Chicago", "state" eq "Illinois") {
        requestCompleteMessage {
          content = "The Chicago override request complete weather lookup"
        }
      }
      requestCompleteMessage {
        role = ToolMessageRoleType.ASSISTANT
        content = "The override request Complete Message weather has arrived"
      }
    }
  }
}
