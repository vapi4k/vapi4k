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

import com.vapi4k.api.json.toJsonElement
import com.vapi4k.dsl.web.VapiWeb.vapiTalkButton
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.title

object TalkPage {
  fun Application.talkPage() {
    routing {
      get("/talk") {
        call.respondHtml {
          head {
            title { +"Talk Button Demo" }
          }
          body {
            h1 { +"Talk Button Demo" }
            vapiTalkButton {
              serverPath = "/talkApp"
              // post args are optional
              postArgs = mapOf(
                "arg1" to "10",
                "arg2" to "20",
                "name" to "Jane",
              ).toJsonElement()
            }
          }
        }
      }
    }
  }
}
