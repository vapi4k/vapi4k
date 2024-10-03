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

package com.vapi4k.console

import com.vapi4k.api.json.toJsonString
import com.vapi4k.common.ApplicationId.Companion.toApplicationId
import com.vapi4k.common.AssistantId.Companion.toAssistantId
import com.vapi4k.common.Constants.FUNCTION_NAME
import com.vapi4k.common.QueryParams.APPLICATION_ID
import com.vapi4k.common.QueryParams.ASSISTANT_ID
import com.vapi4k.common.QueryParams.SESSION_ID
import com.vapi4k.common.QueryParams.SYSTEM_IDS
import com.vapi4k.common.QueryParams.TOOL_TYPE
import com.vapi4k.common.SessionId.Companion.toSessionId
import com.vapi4k.common.Utils.isNotNull
import com.vapi4k.common.Utils.toErrorString
import com.vapi4k.dsl.vapi4k.PipelineCall
import com.vapi4k.dsl.vapi4k.Vapi4kConfigImpl
import com.vapi4k.plugin.Vapi4kServer.logger
import com.vapi4k.server.RequestContextImpl
import com.vapi4k.utils.DslUtils.getRandomString
import com.vapi4k.utils.HttpUtils.getQueryParam
import com.vapi4k.utils.HttpUtils.jsonHttpClient
import com.vapi4k.utils.HttpUtils.missingQueryParam
import com.vapi4k.utils.JsonUtils.toJsonArray
import com.vapi4k.utils.JsonUtils.toJsonObject
import com.vapi4k.utils.MiscUtils.appendQueryParams
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

object InvokeTool {
  suspend fun PipelineCall.invokeTool(config: Vapi4kConfigImpl) =
    runCatching {
      val applicationId = call.getQueryParam(APPLICATION_ID)?.toApplicationId() ?: missingQueryParam(APPLICATION_ID)

      val requestContext =
        RequestContextImpl(
          application = config.getApplicationById(applicationId),
          request = call.generateToolRequest(),
          sessionId = call.getQueryParam(SESSION_ID)?.toSessionId() ?: missingQueryParam(SESSION_ID),
          assistantId = call.getQueryParam(ASSISTANT_ID)?.toAssistantId() ?: missingQueryParam(ASSISTANT_ID),
        )

      val url =
        with(requestContext) {
          application.serverUrl.appendQueryParams(
            SESSION_ID to sessionId.value,
            ASSISTANT_ID to assistantId.value,
          )
        }

      val response =
        jsonHttpClient().use { client ->
          client.post(url) {
            headers.append(com.vapi4k.common.Headers.VAPI_SECRET_HEADER, requestContext.application.serverSecret)
            setBody((requestContext.request as JsonObject).toJsonString<JsonObject>(false))
          }
        }
      response.bodyAsText().toJsonString()
    }.getOrElse { e ->
      logger.error(e) { "Error validating tool invoke request" }
      e.toErrorString()
    }

  private fun ApplicationCall.functionParams(argName: String): JsonObject =
    mapOf(
      "name" to JsonPrimitive(getQueryParam(FUNCTION_NAME) ?: missingQueryParam(FUNCTION_NAME)),
      argName to
        request.queryParameters
          .names()
          .filterNot { it in SYSTEM_IDS }
          .filter { getQueryParam(it).isNotNull() }
          .associateWith { JsonPrimitive(getQueryParam(it)) }
          .toJsonObject(),
    ).toJsonObject()

  private fun ApplicationCall.generateToolRequest(): JsonObject {
    val sessionId = getQueryParam(SESSION_ID)?.toSessionId() ?: missingQueryParam(SESSION_ID)
    val toolTypeStr = getQueryParam(TOOL_TYPE) ?: missingQueryParam(TOOL_TYPE)
    val toolType = ToolType.valueOf(toolTypeStr)
    return buildJsonObject {
      put(
        "message",
        mapOf(
          "type" to JsonPrimitive(toolType.messageType.desc),
          "call" to mapOf("id" to JsonPrimitive(sessionId.value)).toJsonObject(),
          if (toolType == ToolType.FUNCTION)
            toolType.funcName to functionParams(toolType.paramName)
          else
            "toolCallList" to
              listOf(
                mapOf(
                  "id" to JsonPrimitive("call_${getRandomString(24)}"),
                  "type" to JsonPrimitive("function"),
                  toolType.funcName to functionParams(toolType.paramName),
                ).toJsonObject(),
              ).toJsonArray(),
        ).toJsonObject(),
      )
    }
  }
}
