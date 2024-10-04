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

import com.vapi4k.api.model.enums.GroqModelType
import com.vapi4k.api.reponse.InboundCallAssistantResponse
import com.vapi4k.api.tools.ToolCall
import com.vapi4k.api.toolservice.ToolCallService

object SimpleSquad {
  fun InboundCallAssistantResponse.doubleToolAssistant2() =
    squad {
      members {
        member {
          assistant {
            // name = "assistant1"
            firstMessage = "Hi there! I'm assistant1"

            groqModel {
              modelType = GroqModelType.LLAMA3_70B
              tools {
                serviceTool(TestWeatherLookupService("windy"))
              }
            }
          }
          destinations {
            destination {
              assistantName = "assistant2"
              message = "Transfer to assistant2"
              description = "assistant 2"
            }
          }
        }

        member {
          assistant {
            name = "assistant2"
            firstMessage = "Hi there! I'm assistant2"

            groqModel {
              modelType = GroqModelType.LLAMA3_70B
              tools {
                serviceTool(TestWeatherLookupService("rainy"))
              }
            }
          }
          destinations {
            destination {
              assistantName = "assistant1"
              message = "Transfer to assistant1"
              description = "assistant 1"
            }
          }
        }
      }
    }

  class TestWeatherLookupService(
    val weather: String,
  ) : ToolCallService() {
    @ToolCall("Look up the weather for a city")
    fun getWeatherByCity(
      city: String,
      state: String,
    ) = "The weather in $city, $state is $weather"
  }
}
