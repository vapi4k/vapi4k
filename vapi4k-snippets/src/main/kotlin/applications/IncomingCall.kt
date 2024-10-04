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

package applications

import com.vapi4k.api.model.OpenAIModelType
import com.vapi4k.api.vapi4k.RequestContext
import com.vapi4k.api.vapi4k.ServerRequestType.ASSISTANT_REQUEST
import com.vapi4k.api.vapi4k.ServerRequestType.FUNCTION_CALL
import com.vapi4k.api.vapi4k.ServerRequestType.TOOL_CALL
import com.vapi4k.api.voice.DeepGramVoiceIdType
import com.vapi4k.plugin.Vapi4k
import com.vapi4k.plugin.Vapi4kServer.logger
import io.ktor.server.application.Application
import io.ktor.server.application.install

object IncomingCall {
  fun Application.module() {
    install(Vapi4k) {
      inboundCallApplication {
        serverPath = "/inboundApp"
        serverSecret = "12345"

        onAssistantRequest { requestContext: RequestContext ->
          // Describe the assistant or squad
          assistant {
            firstMessage = "Hello! How can I help you today?"

            openAIModel {
              modelType = OpenAIModelType.GPT_4_TURBO
              systemMessage = "You're a polite AI assistant named Vapi who is fun to talk with."
            }

            deepgramVoice {
              voiceIdType = DeepGramVoiceIdType.LUNA
            }
          }
        }

        // Log the ASSISTANT_REQUEST, FUNCTION_CALL, and TOOL_CALL requests
        onRequest(ASSISTANT_REQUEST, FUNCTION_CALL, TOOL_CALL) { requestContext: RequestContext ->
          logger.info { requestContext }
        }
      }
    }
  }
}
