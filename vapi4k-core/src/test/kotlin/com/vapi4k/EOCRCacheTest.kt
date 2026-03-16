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

import com.github.pambrose.common.json.get
import com.github.pambrose.common.json.intValue
import com.github.pambrose.common.json.keys
import com.vapi4k.DoubleToolAssistant.doubleToolAssistant
import com.vapi4k.common.CoreEnvVars.defaultServerPath
import com.vapi4k.common.Endpoints.CACHES_PATH
import com.vapi4k.common.Utils.ensureStartsWith
import com.vapi4k.dsl.vapi4k.ApplicationType.INBOUND_CALL
import com.vapi4k.utils.withTestApplication
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode

class EOCRCacheTest : StringSpec() {
  init {
    "processEOCRMessage removes cache entries when eocrCacheRemovalEnabled is true" {
      val responses =
        withTestApplication(
          INBOUND_CALL,
          listOf(
            "/json-tool-tests/assistantRequest.json",
            "/json-tool-tests/endOfCallReportRequest.json",
          ),
          CACHES_PATH,
          true,
        ) {
          doubleToolAssistant()
        }
      responses.forEach { (response, _) ->
        response.status shouldBe HttpStatusCode.OK
      }
      val cachesResponse = responses.last()
      val jsonElement = cachesResponse.second
      val path = "${INBOUND_CALL.pathPrefix.ensureStartsWith("/")}/$defaultServerPath"
      jsonElement.intValue("$path.serviceTools.cacheSize") shouldBeLessThan 2
    }

    "processEOCRMessage preserves cache entries when eocrCacheRemovalEnabled is false" {
      val responses =
        withTestApplication(
          INBOUND_CALL,
          listOf(
            "/json-tool-tests/assistantRequest.json",
            "/json-tool-tests/endOfCallReportRequest.json",
          ),
          CACHES_PATH,
          false,
        ) {
          doubleToolAssistant()
        }
      responses.forEach { (response, _) ->
        response.status shouldBe HttpStatusCode.OK
      }
      val cachesResponse = responses.last()
      val jsonElement = cachesResponse.second
      val path = "${INBOUND_CALL.pathPrefix.ensureStartsWith("/")}/$defaultServerPath"
      jsonElement["$path.serviceTools.cache"].keys.size shouldBe 2
    }
  }
}
