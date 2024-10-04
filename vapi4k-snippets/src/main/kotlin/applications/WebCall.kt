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

import com.vapi4k.api.buttons.ButtonColor
import com.vapi4k.api.buttons.ButtonPosition
import com.vapi4k.api.buttons.ButtonType
import com.vapi4k.api.model.GroqModelType
import com.vapi4k.api.vapi4k.RequestContext
import com.vapi4k.api.voice.PlayHTVoiceIdType
import com.vapi4k.dsl.web.VapiWeb.vapiTalkButton
import com.vapi4k.plugin.Vapi4k
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.title

object WebCall {
  fun Application.module() {
    install(Vapi4k) {
      webApplication {
        serverPath = "/talkApp"

        onAssistantRequest { requestContext: RequestContext ->
          assistant {
            firstMessage = "Hello! How can I help you today?"

            groqModel {
              modelType = GroqModelType.LLAMA3_70B
              systemMessage = "You're a polite AI assistant named Vapi who is fun to talk with."
            }

            playHTVoice {
              voiceIdType = PlayHTVoiceIdType.JACK
            }
          }
        }

        // Describe the button configuration
        buttonConfig { requestContext: RequestContext ->
          position = ButtonPosition.TOP
          offset = "40px"
          width = "50px"
          height = "50px"

          idle {
            color = ButtonColor(93, 254, 202)
            type = ButtonType.PILL
            title = "Have a quick question?"
            subtitle = "Talk with our AI assistant"
            icon = "https://unpkg.com/lucide-static@0.321.0/icons/phone.svg"
          }

          loading {
            color = ButtonColor(93, 124, 202)
            type = ButtonType.PILL
            title = "Connecting..."
            subtitle = "Please wait"
            icon = "https://unpkg.com/lucide-static@0.321.0/icons/loader-2.svg"
          }

          active {
            color = ButtonColor(255, 0, 0)
            type = ButtonType.PILL
            title = "Call is in progress..."
            subtitle = "End the call."
            icon = "https://unpkg.com/lucide-static@0.321.0/icons/phone-off.svg"
          }
        }
      }

      // Create a route for the talk page
      routing {
        get("/talk") {
          call.respondHtml {
            head {
              title { +"Talk Button Demo" }
            }
            body {
              h1 { +"Talk Button Demo" }
              vapiTalkButton {
                serverPath = "/webApp"
              }
            }
          }
        }
      }
    }
  }
}
