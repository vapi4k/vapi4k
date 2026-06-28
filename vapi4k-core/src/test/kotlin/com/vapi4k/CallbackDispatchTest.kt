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

import com.vapi4k.ServerTest.Companion.configPost
import com.vapi4k.api.model.GroqModelType
import com.vapi4k.api.vapi4k.ServerRequestType.ASSISTANT_REQUEST
import com.vapi4k.api.vapi4k.ServerRequestType.TOOL_CALL
import com.vapi4k.common.CoreEnvVars.defaultServerPath
import com.vapi4k.common.Utils.resourceFile
import com.vapi4k.plugin.Vapi4k
import com.vapi4k.utils.JsonFilenames.JSON_ASSISTANT_REQUEST
import io.kotest.assertions.nondeterministic.eventually
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.install
import io.ktor.server.testing.testApplication
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration.Companion.seconds

/**
 * Exercises the asynchronous callback fan-out performed by `AdminJobs.startCallbackThread`.
 *
 * Callbacks are dispatched on a daemon thread that drains `config.callbackChannel`, so each
 * assertion is wrapped in [eventually] to await the off-thread invocations. The shared counters
 * are [AtomicInteger]s because they are mutated from that dispatch thread.
 */
class CallbackDispatchTest : StringSpec() {
  init {
    "global and per-application request callbacks fire for all requests and only matching types" {
      val globalAll = AtomicInteger()
      val globalAssistant = AtomicInteger()
      val globalTool = AtomicInteger()
      val appAll = AtomicInteger()
      val appAssistant = AtomicInteger()
      val appTool = AtomicInteger()

      testApplication {
        application {
          install(Vapi4k) {
            onAllRequests { globalAll.incrementAndGet() }
            onRequest(ASSISTANT_REQUEST) { globalAssistant.incrementAndGet() }
            onRequest(TOOL_CALL) { globalTool.incrementAndGet() }

            inboundCallApplication {
              onAllRequests { appAll.incrementAndGet() }
              onRequest(ASSISTANT_REQUEST) { appAssistant.incrementAndGet() }
              onRequest(TOOL_CALL) { appTool.incrementAndGet() }
              onAssistantRequest {
                assistant { groqModel { modelType = GroqModelType.LLAMA3_70B_8192 } }
              }
            }
          }
        }

        val response = client.post("/inboundCall/$defaultServerPath") {
          configPost()
          setBody(resourceFile(JSON_ASSISTANT_REQUEST))
        }
        response.status shouldBe HttpStatusCode.OK

        eventually(10.seconds) {
          globalAll.get() shouldBe 1
          globalAssistant.get() shouldBe 1
          appAll.get() shouldBe 1
          appAssistant.get() shouldBe 1
        }
      }

      // TOOL_CALL handlers must never fire for an assistant-request message.
      globalTool.get() shouldBe 0
      appTool.get() shouldBe 0
    }

    "global response callbacks fire for all responses and only matching types" {
      val globalAllResponses = AtomicInteger()
      val assistantResponse = AtomicInteger()
      val toolResponse = AtomicInteger()

      testApplication {
        application {
          install(Vapi4k) {
            onAllResponses { globalAllResponses.incrementAndGet() }
            onResponse(ASSISTANT_REQUEST) { assistantResponse.incrementAndGet() }
            onResponse(TOOL_CALL) { toolResponse.incrementAndGet() }

            inboundCallApplication {
              onAssistantRequest {
                assistant { groqModel { modelType = GroqModelType.LLAMA3_70B_8192 } }
              }
            }
          }
        }

        val response = client.post("/inboundCall/$defaultServerPath") {
          configPost()
          setBody(resourceFile(JSON_ASSISTANT_REQUEST))
        }
        response.status shouldBe HttpStatusCode.OK

        eventually(10.seconds) {
          globalAllResponses.get() shouldBe 1
          assistantResponse.get() shouldBe 1
        }
      }

      toolResponse.get() shouldBe 0
    }

    "request callbacks are dispatched only to the application that handled the request" {
      val appA = AtomicInteger()
      val appB = AtomicInteger()

      testApplication {
        application {
          install(Vapi4k) {
            inboundCallApplication {
              serverPath = "/appA"
              onAllRequests { appA.incrementAndGet() }
              onAssistantRequest {
                assistant { groqModel { modelType = GroqModelType.LLAMA3_70B_8192 } }
              }
            }
            inboundCallApplication {
              serverPath = "/appB"
              onAllRequests { appB.incrementAndGet() }
              onAssistantRequest {
                assistant { groqModel { modelType = GroqModelType.LLAMA3_70B_8192 } }
              }
            }
          }
        }

        val response = client.post("/inboundCall/appA") {
          configPost()
          setBody(resourceFile(JSON_ASSISTANT_REQUEST))
        }
        response.status shouldBe HttpStatusCode.OK

        eventually(10.seconds) {
          appA.get() shouldBe 1
        }
      }

      // The request targeted appA, so appB's callbacks must not be invoked.
      appB.get() shouldBe 0
    }
  }
}
