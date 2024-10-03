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

package com.vapi4k.api

import com.vapi4k.api.json.toJsonString
import com.vapi4k.api.vapi4k.enums.ServerRequestType
import com.vapi4k.api.vapi4k.enums.ServerRequestType.Companion.serverRequestType
import com.vapi4k.dbms.MessagesTable
import kotlinx.serialization.json.JsonElement
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.Duration

object Messages {
  fun insertRequest(request: JsonElement) {
    transaction {
      val str = request.toJsonString()
      MessagesTable.insert { rec ->
        rec[messageType] = "REQUEST"
        rec[requestType] = request.serverRequestType.desc
        rec[messageJsonb] = str
        rec[messageJson] = str
        rec[elapsedTime] = Duration.ZERO
      }
    }
  }

  fun insertResponse(
    type: ServerRequestType,
    response: JsonElement,
    elapsed: Duration,
  ) {
    transaction {
      val str = response.toJsonString()
      MessagesTable.insert { rec ->
        rec[messageType] = "RESPONSE"
        rec[requestType] = type.desc
        rec[messageJsonb] = str
        rec[messageJson] = str
        rec[elapsedTime] = elapsed
      }
    }
  }
}
