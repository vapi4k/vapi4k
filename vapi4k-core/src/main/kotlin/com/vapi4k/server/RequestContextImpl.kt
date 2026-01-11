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

package com.vapi4k.server

import com.github.pambrose.common.json.toJsonString
import com.vapi4k.api.vapi4k.AssistantRequestUtils.hasStatusUpdateError
import com.vapi4k.api.vapi4k.AssistantRequestUtils.phoneNumber
import com.vapi4k.api.vapi4k.AssistantRequestUtils.statusUpdateError
import com.vapi4k.api.vapi4k.RequestContext
import com.vapi4k.api.vapi4k.ServerRequestType
import com.vapi4k.api.vapi4k.ServerRequestType.Companion.getServerRequestType
import com.vapi4k.common.AssistantId
import com.vapi4k.common.SessionId
import com.vapi4k.dsl.vapi4k.AbstractApplicationImpl
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonElement

class RequestContextImpl(
  val application: AbstractApplicationImpl,
  override val request: JsonElement,
  override val sessionId: SessionId,
  val assistantId: AssistantId,
  override val created: Instant = Clock.System.now(),
) : RequestContext {
  override val age get() = Clock.System.now() - created
  override val serverRequestType: ServerRequestType get() = request.getServerRequestType("RequestContextImpl")
  override val statusUpdateError: String get() = request.statusUpdateError
  override val phoneNumber: String get() = request.phoneNumber

  override fun hasStatusUpdateError(): Boolean = request.hasStatusUpdateError()

  override fun toString(): String =
    "ResponseContext:\nRequestType: ${serverRequestType}\nSessionId: $sessionId\nResponse:\n${request.toJsonString()}"
}
