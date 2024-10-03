/*
 * Copyright © 2024 Matthew Ambrose (mattbobambrose@gmail.com)
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import kotlin.test.Test
import kotlin.test.assertTrue

class ApiCalls {
  @Test
  fun `Missing auth`() {
    assertThrows(IllegalStateException::class.java) {
      val api = vapiApi() as VapiApiImpl
      api.test {
        outboundCall {
        }
      }
    }.also {
      assertTrue(it.message.orEmpty().contains("VAPI_PRIVATE_KEY"))
    }
  }

  @Test
  fun `Missing serverPath`() {
    assertThrows(IllegalArgumentException::class.java) {
      val api = vapiApi("123-445-666") as VapiApiImpl
      api.test {
        outboundCall {
        }
      }
    }.also {
      assertEquals("serverPath must be assigned in outboundCall{}", it.message)
    }
  }

  @Test
  fun `Missing phoneNumber`() {
    assertThrows(IllegalArgumentException::class.java) {
      val api = vapiApi("123-445-666") as VapiApiImpl
      api.test {
        outboundCall {
          serverPath = "/outboundRequest"
        }
      }
    }.also {
      assertEquals("phoneNumber must be assigned in outboundCall{}", it.message)
    }
  }

  @Test
  fun `Missing phoneNumberId`() {
    assertThrows(IllegalStateException::class.java) {
      val api = vapiApi("123-445-666") as VapiApiImpl
      api.test {
        outboundCall {
          serverPath = "/outboundRequest"
          phoneNumber = "+1123-456-7890"
        }
      }
    }.also {
      assertTrue(it.message.orEmpty().contains("Missing phoneNumberId value"))
    }
  }

  @Test
  fun `multiple outboundCall{} blocks`() {
    assertThrows(IllegalStateException::class.java) {
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
    }.also {
      assertEquals("outboundCall{} was already called", it.message)
    }
  }
}
