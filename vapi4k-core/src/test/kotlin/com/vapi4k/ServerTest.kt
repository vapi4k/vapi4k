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

import com.vapi4k.DoubleToolAssistant.doubleToolAssistant
import com.vapi4k.api.json.get
import com.vapi4k.api.json.intValue
import com.vapi4k.api.json.keys
import com.vapi4k.api.json.stringValue
import com.vapi4k.api.json.toJsonString
import com.vapi4k.api.model.GroqModelType
import com.vapi4k.common.CoreEnvVars.defaultServerPath
import com.vapi4k.common.Endpoints.CACHES_PATH
import com.vapi4k.common.Utils.ensureStartsWith
import com.vapi4k.dsl.vapi4k.ApplicationType.INBOUND_CALL
import com.vapi4k.plugin.Vapi4k
import com.vapi4k.utils.JsonFilenames
import com.vapi4k.utils.JsonUtils.firstInList
import com.vapi4k.utils.withTestApplication
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.application.install
import io.ktor.server.testing.testApplication
import org.junit.Assert.assertEquals
import kotlin.test.Test

class ServerTest {
  companion object {
    fun HttpRequestBuilder.configPost() {
      contentType(Application.Json)
    }
  }

  @Test
  fun `ping request`() {
    testApplication {
      application {
        install(Vapi4k)
      }
      val response = client.get("/ping")
      assertEquals(HttpStatusCode.OK, response.status)
      assertEquals("pong", response.bodyAsText())
    }
  }

  @Test
  fun `simple assistant request`() {
    val (response, jsonElement) =
      withTestApplication(INBOUND_CALL, JsonFilenames.JSON_ASSISTANT_REQUEST) {
        assistant {
          groqModel {
            modelType = GroqModelType.LLAMA3_70B
          }
        }
      }

    assertEquals(HttpStatusCode.OK, response.status)
    assertEquals(GroqModelType.LLAMA3_70B.desc, jsonElement.stringValue("messageResponse.assistant.model.model"))
  }

  // @Test
  fun `Tool requests arg ordering`() {
    val responses =
      withTestApplication(
        INBOUND_CALL,
        listOf(
          JsonFilenames.JSON_ASSISTANT_REQUEST,
          "/json-tool-tests/toolRequest1.json",
          "/json-tool-tests/toolRequest2.json",
          "/json-tool-tests/toolRequest3.json",
          "/json-tool-tests/toolRequest4.json",
          "/json-tool-tests/endOfCallReportRequest.json",
        ),
        CACHES_PATH,
        true,
      ) {
        doubleToolAssistant()
      }

    responses.forEachIndexed { i, (response, jsonElement) ->
      assertEquals(HttpStatusCode.OK, response.status)
      if (i in listOf(1, 2))
        assertEquals(
          "The weather in Danville, California is windy",
          jsonElement["messageResponse.results"].firstInList().stringValue("result"),
        )

      if (i in listOf(3, 4))
        assertEquals(
          "The weather in Boston, Massachusetts is rainy",
          jsonElement["messageResponse.results"].firstInList().stringValue("result"),
        )

      if (i == 6) {
        val path = "${INBOUND_CALL.pathPrefix.ensureStartsWith("/")}/$defaultServerPath"
        println(jsonElement.toJsonString())
        assertEquals(0, jsonElement.intValue("$path.serviceTools.cacheSize"))
        assertEquals(0, jsonElement.intValue("$path.functions.cacheSize"))
      }
    }
  }

  @Test
  fun `Check for EOCR cache removal`() {
    val responses =
      withTestApplication(
        INBOUND_CALL,
        listOf(
          JsonFilenames.JSON_ASSISTANT_REQUEST,
          "/json-tool-tests/endOfCallReportRequest.json",
        ),
        CACHES_PATH,
        false,
      ) {
        doubleToolAssistant()
      }
    responses.forEachIndexed { i, (response, jsonElement) ->
      assertEquals(HttpStatusCode.OK, response.status)
      if (i == 2) {
        println(jsonElement.toJsonString())
        val path = "${INBOUND_CALL.pathPrefix.ensureStartsWith("/")}/$defaultServerPath"
        assertEquals(0, jsonElement["$path.functions.cache"].keys.size)
        assertEquals(2, jsonElement["$path.serviceTools.cache"].keys.size)
      }
    }
  }
}
