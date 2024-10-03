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

import com.vapi4k.api.model.enums.AnthropicModelType
import com.vapi4k.api.voice.enums.ElevenLabsVoiceIdType
import com.vapi4k.api.voice.enums.ElevenLabsVoiceModelType
import com.vapi4k.plugin.Vapi4k
import io.ktor.server.application.Application
import io.ktor.server.application.install

object OutgoingCall {
  fun Application.module() {
    install(Vapi4k) {
      outboundCallApplication {
        serverPath = "/callCustomer"
        serverSecret = "12345"

        onAssistantRequest { args ->
          assistant {
            firstMessage = "Hello! I am calling to ask you a question."

            anthropicModel {
              modelType = AnthropicModelType.CLAUDE_3_HAIKU
              systemMessage = "You're a polite AI assistant named Vapi who is fun to talk with."
            }

            elevenLabsVoice {
              voiceIdType = ElevenLabsVoiceIdType.PAULA
              modelType = ElevenLabsVoiceModelType.ELEVEN_TURBO_V2
            }
          }
        }
      }
    }
  }
}
