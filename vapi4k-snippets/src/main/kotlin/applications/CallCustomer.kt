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

package applications

import com.vapi4k.api.json.toJsonElement
import com.vapi4k.dsl.call.VapiApiImpl.Companion.vapiApi
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking

object CallCustomer {
  @JvmStatic
  fun main(args: Array<String>) {
    outboundCallExample()
  }
}

fun outboundCallExample() {
  val response =
    vapiApi()
      .phone {
        outboundCall {
          serverPath = "/callCustomer"
          phoneNumber = "+14155551212"
        }
      }

  runBlocking {
    println("Call status: ${response.status}")
    println("Call response: ${response.bodyAsText().toJsonElement()}")
  }
}
