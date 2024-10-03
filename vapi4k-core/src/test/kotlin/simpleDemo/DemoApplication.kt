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

package simpleDemo

import com.vapi4k.plugin.Vapi4k
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import simpleDemo.SimpleAssistant.simpleAssistantRequest

fun main() {
  embeddedServer(
    factory = CIO,
    port = 8080,
    host = "0.0.0.0",
    module = Application::demoModule,
  ).start(wait = true)
}

fun Application.demoModule() {
  install(Vapi4k) {
    inboundCallApplication {
      serverPath = "vapi4k"
      serverSecret = "12345"

      onAssistantRequest {
//      getAssistantIdWithTool(requestContext)
//      getAssistantWithOverrides(requestContext)
//
//      AssistantOverrides with Squad
//      MemberOverrides with Squad

        // doubleToolAssistant2(requestContext)
        simpleAssistantRequest()
      }
    }
  }
}
