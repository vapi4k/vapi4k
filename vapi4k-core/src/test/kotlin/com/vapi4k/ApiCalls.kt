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

import com.vapi4k.dsl.call.VapiApiImpl
import com.vapi4k.dsl.call.VapiApiImpl.Companion.vapiApi
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class ApiCalls : StringSpec() {
  init {
    "Missing auth" {
      shouldThrow<IllegalStateException> {
        val api = vapiApi() as VapiApiImpl
        api.test {
          outboundCall {
          }
        }
      }.message shouldContain "VAPI_PRIVATE_KEY"
    }

    "Missing serverPath" {
      shouldThrow<IllegalArgumentException> {
        val api = vapiApi("123-445-666") as VapiApiImpl
        api.test {
          outboundCall {
          }
        }
      }.message shouldBe "serverPath must be assigned in outboundCall{}"
    }

    "Missing phoneNumber" {
      shouldThrow<IllegalArgumentException> {
        val api = vapiApi("123-445-666") as VapiApiImpl
        api.test {
          outboundCall {
            serverPath = "/outboundRequest"
          }
        }
      }.message shouldBe "phoneNumber must be assigned in outboundCall{}"
    }

    "Missing phoneNumberId" {
      shouldThrow<IllegalStateException> {
        val api = vapiApi("123-445-666") as VapiApiImpl
        api.test {
          outboundCall {
            serverPath = "/outboundRequest"
            phoneNumber = "+1123-456-7890"
          }
        }
      }.message shouldContain "Missing phoneNumberId value"
    }

    "multiple outboundCall{} blocks" {
      shouldThrow<IllegalStateException> {
        val api = vapiApi("123-445-666") as VapiApiImpl
        api.test {
          outboundCall {
            serverPath = "/outboundRequest1"
            phoneNumber = "+1123-456-7890"
            phoneNumberId = "123-445-666"
          }
          outboundCall {
            serverPath = "/outboundRequest2"
            phoneNumber = "+1123-456-7890"
            phoneNumberId = "123-445-666"
          }
        }
      }.message shouldBe "outboundCall{} was already called"
    }
  }
}
