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

package com.vapi4k.server

import com.vapi4k.common.ApplicationId
import com.vapi4k.common.AssistantId
import com.vapi4k.common.SessionId
import com.vapi4k.dsl.vapi4k.RequestResponseType
import com.vapi4k.dsl.vapi4k.Vapi4kConfigImpl
import com.vapi4k.utils.JsonUtils.EMPTY_JSON_ELEMENT
import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonElement
import kotlin.time.Duration

data class RequestResponseCallback(
  val type: RequestResponseType,
  val created: Instant,
  val applicationId: ApplicationId,
  val sessionId: SessionId,
  val request: JsonElement,
  val response: () -> JsonElement = { EMPTY_JSON_ELEMENT },
  val elapsed: Duration = Duration.ZERO,
) {
  fun toRequestContext(config: Vapi4kConfigImpl): RequestContextImpl =
    RequestContextImpl(
      application = config.getApplicationById(applicationId),
      request = request,
      sessionId = sessionId,
      assistantId = AssistantId.EMPTY_ASSISTANT_ID,
      created = created,
    )

  fun toResponseContext(
    config: Vapi4kConfigImpl,
    response: JsonElement,
  ): ResponseContextImpl = ResponseContextImpl(toRequestContext(config), response, elapsed)

  companion object {
    fun requestCallback(requestContext: RequestContextImpl): RequestResponseCallback =
      with(requestContext) {
        RequestResponseCallback(
          type = RequestResponseType.REQUEST,
          created = created,
          applicationId = application.applicationId,
          sessionId = sessionId,
          request = request,
        )
      }

    fun responseCallback(
      requestContext: RequestContextImpl,
      response: () -> JsonElement,
      elapsed: Duration,
    ): RequestResponseCallback =
      with(requestContext) {
        RequestResponseCallback(
          type = RequestResponseType.RESPONSE,
          created = created,
          applicationId = application.applicationId,
          sessionId = sessionId,
          request = request,
          response = response,
          elapsed = elapsed,
        )
      }
  }
}
