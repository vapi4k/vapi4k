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

import com.vapi4k.api.model.GroqModelType
import com.vapi4k.common.CoreEnvVars.defaultServerPath
import com.vapi4k.common.QueryParams.SECRET_PARAM
import com.vapi4k.common.Utils.resourceFile
import com.vapi4k.dsl.vapi4k.ApplicationType.INBOUND_CALL
import com.vapi4k.plugin.Vapi4k
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.application.install
import io.ktor.server.testing.testApplication

class ServerRoutingTest : StringSpec() {
  init {
    "inboundCallRequest returns 403 when server secret is invalid" {
      testApplication {
        application {
          install(Vapi4k) {
            inboundCallApplication {
              serverSecret = "correct-secret"
              onAssistantRequest {
                assistant {
                  groqModel {
                    modelType = GroqModelType.LLAMA3_70B_8192
                  }
                }
              }
            }
          }
        }

        val response =
          client.post("/${INBOUND_CALL.pathPrefix}/$defaultServerPath?$SECRET_PARAM=wrong-secret") {
            contentType(Application.Json)
            setBody(resourceFile("/json-tool-tests/assistantRequest.json"))
          }

        response.status shouldBe HttpStatusCode.Forbidden
      }
    }

    "inboundCallRequest returns SimpleMessageResponse for STATUS_UPDATE" {
      testApplication {
        application {
          install(Vapi4k) {
            inboundCallApplication {
              onAssistantRequest {
                assistant {
                  groqModel {
                    modelType = GroqModelType.LLAMA3_70B_8192
                  }
                }
              }
            }
          }
        }

        val statusUpdateJson = """
          {
            "message": {
              "type": "status-update",
              "status": "in-progress",
              "call": {
                "id": "call-123"
              }
            }
          }
        """.trimIndent()

        val response =
          client.post("/${INBOUND_CALL.pathPrefix}/$defaultServerPath") {
            contentType(Application.Json)
            setBody(statusUpdateJson)
          }

        response.status shouldBe HttpStatusCode.OK
      }
    }

    "inboundCallRequest returns SimpleMessageResponse for TRANSCRIPT" {
      testApplication {
        application {
          install(Vapi4k) {
            inboundCallApplication {
              onAssistantRequest {
                assistant {
                  groqModel {
                    modelType = GroqModelType.LLAMA3_70B_8192
                  }
                }
              }
            }
          }
        }

        val transcriptJson = """
          {
            "message": {
              "type": "transcript",
              "transcript": "Hello, how are you?"
            }
          }
        """.trimIndent()

        val response =
          client.post("/${INBOUND_CALL.pathPrefix}/$defaultServerPath") {
            contentType(Application.Json)
            setBody(transcriptJson)
          }

        response.status shouldBe HttpStatusCode.OK
      }
    }
  }
}
