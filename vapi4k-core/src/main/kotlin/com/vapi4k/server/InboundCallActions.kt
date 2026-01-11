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

import com.github.pambrose.common.json.toJsonElement
import com.vapi4k.api.vapi4k.ServerRequestType.ASSISTANT_REQUEST
import com.vapi4k.api.vapi4k.ServerRequestType.Companion.getServerRequestType
import com.vapi4k.api.vapi4k.ServerRequestType.END_OF_CALL_REPORT
import com.vapi4k.api.vapi4k.ServerRequestType.FUNCTION_CALL
import com.vapi4k.api.vapi4k.ServerRequestType.TOOL_CALL
import com.vapi4k.api.vapi4k.ServerRequestType.TRANSFER_DESTINATION_REQUEST
import com.vapi4k.common.AssistantId.Companion.EMPTY_ASSISTANT_ID
import com.vapi4k.common.AssistantId.Companion.toAssistantId
import com.vapi4k.common.CoreEnvVars.isProduction
import com.vapi4k.common.Headers.VALIDATE_HEADER
import com.vapi4k.common.Headers.VALIDATE_VALUE
import com.vapi4k.common.QueryParams.ASSISTANT_ID
import com.vapi4k.common.QueryParams.SESSION_ID
import com.vapi4k.common.SessionId.Companion.toSessionId
import com.vapi4k.common.Utils.lambda
import com.vapi4k.common.Utils.toErrorString
import com.vapi4k.console.ValidateApplication.isValidSecret
import com.vapi4k.dsl.vapi4k.AbstractApplicationImpl
import com.vapi4k.dsl.vapi4k.ApplicationType.INBOUND_CALL
import com.vapi4k.dsl.vapi4k.Vapi4kConfigImpl
import com.vapi4k.plugin.Vapi4kServer.logger
import com.vapi4k.responses.FunctionResponse.Companion.getFunctionCallResponse
import com.vapi4k.responses.SimpleMessageResponse
import com.vapi4k.responses.ToolCallResponseDto.Companion.getToolCallResponse
import com.vapi4k.server.AdminJobs.invokeRequestCallbacks
import com.vapi4k.server.AdminJobs.invokeResponseCallbacks
import com.vapi4k.utils.HttpUtils.getHeader
import com.vapi4k.utils.HttpUtils.getQueryParam
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.RoutingContext
import kotlin.time.measureTimedValue

internal object InboundCallActions {
  internal suspend fun RoutingContext.inboundCallRequest(
    config: Vapi4kConfigImpl,
    application: AbstractApplicationImpl,
  ) {
    val postObj = call.receive<String>()
    logger.info { "Inbound call request: $postObj" }
    val requestContext = RequestContextImpl(
      application = application,
      request = postObj.toJsonElement(),
      sessionId = call.getQueryParam(SESSION_ID)?.toSessionId() ?: INBOUND_CALL.getRandomSessionId(),
      assistantId = call.getQueryParam(ASSISTANT_ID)?.toAssistantId() ?: EMPTY_ASSISTANT_ID,
    )

    if (!isValidSecret(requestContext.application.serverSecret)) {
      call.respond<String>(HttpStatusCode.Forbidden, "Invalid secret")
    } else {
      if (isProduction || call.getHeader(VALIDATE_HEADER) != VALIDATE_VALUE) {
        processInboundCallRequest(config, requestContext)
      } else {
        runCatching {
          processInboundCallRequest(config, requestContext)
        }.onFailure { e ->
          logger.error(e) { "Error processing inbound call assistant request" }
          call.respondText(e.toErrorString(), status = HttpStatusCode.InternalServerError)
        }
      }
    }
  }

  private suspend fun RoutingContext.processInboundCallRequest(
    config: Vapi4kConfigImpl,
    requestContext: RequestContextImpl,
  ) {
    val requestType = requestContext.request.getServerRequestType("processInboundCallRequest")
    invokeRequestCallbacks(config, requestContext)

    val (response, duration) =
      measureTimedValue {
        with(requestContext) {
          when (requestType) {
            ASSISTANT_REQUEST -> {
              val response = application.getAssistantResponse(requestContext)
              call.respond(response)
              lambda { response.toJsonElement() }
            }

            FUNCTION_CALL -> {
              val response = getFunctionCallResponse(requestContext)
              call.respond(response)
              lambda { response.toJsonElement() }
            }

            TOOL_CALL -> {
              val response = getToolCallResponse(requestContext)
              call.respond(response)
              lambda { response.toJsonElement() }
            }

            TRANSFER_DESTINATION_REQUEST -> {
              val response = application.getTransferDestinationResponse(requestContext)
              call.respond(response)
              lambda { response.toJsonElement() }
            }

            END_OF_CALL_REPORT -> {
              application.processEOCRMessage(requestContext)
              val response = SimpleMessageResponse("End of call report received")
              call.respond(response)
              lambda { response.toJsonElement() }
            }

            else -> {
              val response = SimpleMessageResponse("$requestType received")
              call.respond(response)
              lambda { response.toJsonElement() }
            }
          }
        }
      }

    invokeResponseCallbacks(config, requestContext, response, duration)
  }
}
