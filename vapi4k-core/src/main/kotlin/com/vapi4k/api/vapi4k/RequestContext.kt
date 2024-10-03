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

package com.vapi4k.api.vapi4k

import com.vapi4k.api.vapi4k.enums.ServerRequestType
import com.vapi4k.common.SessionId
import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonElement
import kotlin.time.Duration

interface RequestContext {
  val serverRequestType: ServerRequestType
  val request: JsonElement
  val sessionId: SessionId
  val created: Instant
  val age: Duration
  val statusUpdateError: String
  val phoneNumber: String

  fun hasStatusUpdateError(): Boolean
}
