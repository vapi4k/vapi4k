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

import com.vapi4k.dsl.vapi4k.ApplicationType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldEndWith

class ApplicationTypeTest : StringSpec() {
  init {
    "INBOUND_CALL getRandomSessionId ends with InboundCall" {
      val sessionId = ApplicationType.INBOUND_CALL.getRandomSessionId()
      sessionId.value shouldContain "-InboundCall"
      sessionId.value shouldEndWith "-InboundCall"
    }

    "OUTBOUND_CALL getRandomSessionId ends with OutboundCall" {
      val sessionId = ApplicationType.OUTBOUND_CALL.getRandomSessionId()
      sessionId.value shouldEndWith "-OutboundCall"
    }

    "WEB getRandomSessionId ends with Web" {
      val sessionId = ApplicationType.WEB.getRandomSessionId()
      sessionId.value shouldEndWith "-Web"
    }

    "getRandomSessionId produces unique values" {
      val id1 = ApplicationType.INBOUND_CALL.getRandomSessionId()
      val id2 = ApplicationType.INBOUND_CALL.getRandomSessionId()
      id1 shouldNotBe id2
    }

    "getRandomSessionId has expected format with dashes" {
      val sessionId = ApplicationType.INBOUND_CALL.getRandomSessionId()
      val parts = sessionId.value.split("-")
      (parts.size >= 5) shouldBe true
    }

    "functionName includes braces" {
      ApplicationType.INBOUND_CALL.functionName shouldBe "inboundCallApplication{}"
      ApplicationType.OUTBOUND_CALL.functionName shouldBe "outboundCallApplication{}"
      ApplicationType.WEB.functionName shouldBe "webApplication{}"
    }

    "displayName values are correct" {
      ApplicationType.INBOUND_CALL.displayName shouldBe "inboundCallApplication"
      ApplicationType.OUTBOUND_CALL.displayName shouldBe "outboundCallApplication"
      ApplicationType.WEB.displayName shouldBe "webApplication"
    }

    "pathPrefix values are correct" {
      ApplicationType.INBOUND_CALL.pathPrefix shouldBe "inboundCall"
      ApplicationType.OUTBOUND_CALL.pathPrefix shouldBe "outboundCall"
      ApplicationType.WEB.pathPrefix shouldBe "web"
    }
  }
}
