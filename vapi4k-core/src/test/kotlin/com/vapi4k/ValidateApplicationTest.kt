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

import com.vapi4k.plugin.Vapi4k
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.install
import io.ktor.server.testing.testApplication

class ValidateApplicationTest : StringSpec() {
  init {
    // Drives the "application not found" branch of validateApplication. Without the
    // kotlinx.html unaryPlus on the <p>, the message string is discarded and the
    // element renders empty, so this assertion guards that fix.
    "validateApplication renders a not-found message for an unknown app" {
      testApplication {
        application { install(Vapi4k) }
        val response =
          client.get("/validate/inboundCall/no-such-app") {
            basicAuth("admin", "admin")
          }
        response.status shouldBe HttpStatusCode.OK
        response.bodyAsText() shouldContain "Application for /no-such-app not found"
      }
    }
  }
}
