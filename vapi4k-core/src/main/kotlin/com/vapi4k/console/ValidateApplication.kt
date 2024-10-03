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

package com.vapi4k.console

import com.vapi4k.api.json.toJsonElement
import com.vapi4k.common.ApplicationName.Companion.toApplicationName
import com.vapi4k.common.AssistantId.Companion.EMPTY_ASSISTANT_ID
import com.vapi4k.common.Constants.APP_NAME
import com.vapi4k.common.Constants.APP_TYPE
import com.vapi4k.common.Constants.STATIC_BASE
import com.vapi4k.common.CoreEnvVars.REQUEST_VALIDATION_FILENAME
import com.vapi4k.common.CoreEnvVars.vapi4kBaseUrl
import com.vapi4k.common.CssNames.CONNECT_ERROR
import com.vapi4k.common.Headers.VALIDATE_HEADER
import com.vapi4k.common.Headers.VALIDATE_VALUE
import com.vapi4k.common.Headers.VAPI_SECRET_HEADER
import com.vapi4k.common.QueryParams.SECRET_PARAM
import com.vapi4k.common.QueryParams.SESSION_ID
import com.vapi4k.common.Utils.isNotNull
import com.vapi4k.common.Utils.resourceFile
import com.vapi4k.common.Utils.toErrorString
import com.vapi4k.console.ValidateAssistant.navBar
import com.vapi4k.console.ValidateAssistant.singleNavItem
import com.vapi4k.console.ValidateAssistant.validateAssistant
import com.vapi4k.dsl.vapi4k.AbstractApplicationImpl
import com.vapi4k.dsl.vapi4k.ApplicationType.INBOUND_CALL
import com.vapi4k.dsl.vapi4k.ApplicationType.OUTBOUND_CALL
import com.vapi4k.dsl.vapi4k.ApplicationType.WEB
import com.vapi4k.dsl.vapi4k.PipelineCall
import com.vapi4k.dsl.vapi4k.Vapi4kConfigImpl
import com.vapi4k.plugin.Vapi4kServer.logger
import com.vapi4k.server.RequestContextImpl
import com.vapi4k.utils.DslUtils.getRandomSecret
import com.vapi4k.utils.HtmlUtils.html
import com.vapi4k.utils.HttpUtils.getHeader
import com.vapi4k.utils.HttpUtils.getQueryParam
import com.vapi4k.utils.HttpUtils.jsonHttpClient
import com.vapi4k.utils.JsonUtils.EMPTY_JSON_ELEMENT
import com.vapi4k.utils.JsonUtils.modifyObjectWith
import com.vapi4k.utils.MiscUtils.appendQueryParams
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.application.call
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h2
import kotlinx.html.id
import kotlinx.html.p
import kotlinx.html.script
import kotlinx.html.span
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import java.io.IOException

internal object ValidateApplication {
  suspend fun PipelineCall.validateApplication(config: Vapi4kConfigImpl): String =
    runCatching {
      val appType = call.parameters[APP_TYPE].orEmpty()
      val appName = call.parameters[APP_NAME].orEmpty().toApplicationName()
      val app =
        when (appType) {
          WEB.pathPrefix -> config.webApplications
          INBOUND_CALL.pathPrefix -> config.inboundCallApplications
          OUTBOUND_CALL.pathPrefix -> config.outboundCallApplications
          else -> error("Invalid application type: $appType")
        }.firstOrNull { it.serverPathNoSlash == appName.value }

      if (app.isNotNull()) {
        val request = getNewRequest()
        val secret = call.getQueryParam(SECRET_PARAM).orEmpty()
        val typePrefix = app.applicationType.pathPrefix
        val sessionId = app.applicationType.getRandomSessionId()
        val requestContext = RequestContextImpl(app, request, sessionId, EMPTY_ASSISTANT_ID)
        val baseUrl = "$vapi4kBaseUrl/$typePrefix/${appName.value}"
        val url = baseUrl.appendQueryParams(SESSION_ID to sessionId.value)
        logger.info { "Fetching content for url: $url" }
        val (status, responseBody) = fetchContent(app, request, secret, url)
        validateAssistant(app, requestContext, status, responseBody)
      } else {
        html {
          navBar { singleNavItem() }
          div {
            classes += CONNECT_ERROR
            h2 { +"Error" }
            p { "Application for /${appName.value} not found" }
          }
        }
      }
    }.getOrElse {
      if (it is IOException || it is UnresolvedAddressException) {
        serverBasePage()
      } else {
        logger.error(it) { "Error validating application" }
        html {
          navBar { singleNavItem() }
          div {
            classes += CONNECT_ERROR
            h2 { +"Error" }
            p { it.toErrorString() }
          }
        }
      }
    }

  private fun serverBasePage() =
    html {
      navBar { singleNavItem() }
      div {
        classes += CONNECT_ERROR
        h2 { +"Configuration Error" }
        p {}
        val currentVal = System.getenv("VAPI4K_BASE_URL").orEmpty()
        p {
          if (currentVal.isBlank())
            +"The environment variable VAPI4K_BASE_URL is not set"
          else
            +"The current value of VAPI4K_BASE_URL is $currentVal"
        }
        p {
          +"Please set the environment variable VAPI4K_BASE_URL =  "
          span { id = "serverBaseUrl" }
        }
        script { src = "$STATIC_BASE/js/assign-server-base-url.js" }
      }
    }

  private fun getNewRequest(): JsonElement {
    val request = runCatching {
      resourceFile(REQUEST_VALIDATION_FILENAME.value)
    }.getOrElse { ASSISTANT_REQUEST_JSON }
    return copyWithNewCallId(request.toJsonElement())
  }

  private fun copyWithNewCallId(je: JsonElement): JsonElement =
    buildJsonObject {
      put(
        "message",
        je.modifyObjectWith("message") { messageMap ->
          messageMap["call"] =
            je.modifyObjectWith("message.call") { callMap ->
              callMap["id"] = JsonPrimitive(getRandomSecret(8, 4, 4, 12))
            }
        },
      )
    }

  internal fun PipelineCall.isValidSecret(configPropertiesSecret: String): Boolean {
    val secret = call.getHeader(VAPI_SECRET_HEADER)
    return (configPropertiesSecret.isBlank() || secret.trim() == configPropertiesSecret.trim()).also {
      if (!it) {
        logger.info { """Invalid secret. Found: "$secret" Expected: "$configPropertiesSecret"""" }
      }
    }
  }

  private suspend fun fetchContent(
    application: AbstractApplicationImpl,
    request: JsonElement,
    secret: String,
    url: String,
  ): Pair<HttpStatusCode, String> =
    jsonHttpClient().use { client ->
      client.post(url) {
        contentType(Application.Json)
        headers.append(VALIDATE_HEADER, VALIDATE_VALUE)
        if (secret.isNotEmpty())
          headers.append(VAPI_SECRET_HEADER, secret)
        val jsonBody = if (application.applicationType == INBOUND_CALL) request else EMPTY_JSON_ELEMENT
        setBody(jsonBody)
      }
    }.run { status to bodyAsText() }

  private const val ASSISTANT_REQUEST_JSON = """
    {
      "message": {
        "type": "assistant-request",
        "call": {
          "id": "305b7217-6d48-433b-bda9-0f00a1731234",
          "orgId": "679a13ec-f40d-4055-8959-797c4ee11234",
          "createdAt": "2024-07-25T06:07:29.604Z",
          "updatedAt": "2024-07-25T06:07:29.604Z",
          "type": "inboundPhoneCall",
          "status": "ringing",
          "phoneCallProvider": "twilio",
          "phoneCallProviderId": "CAef753577823739784a4a250331e4ab5a",
          "phoneCallTransport": "pstn",
          "phoneNumberId": "4a5a04dc-dcbe-45b1-8f64-fd32a253d136",
          "assistantId": null,
          "squadId": null,
          "customer": {
            "number": "+1234567890"
          }
        },
        "phoneNumber": {
          "id": "4a5a04dc-dcbe-45b1-8f64-fd32a253d136",
          "orgId": "379a13ec-f40d-4055-8959-797c4ee1694s",
          "assistantId": null,
          "number": "+1234567890",
          "createdAt": "2024-06-29T03:03:00.576Z",
          "updatedAt": "2024-07-20T04:24:05.533Z",
          "stripeSubscriptionId": "sub_1PWrYyCRkod4mKy33dFxM9B8",
          "twilioAccountSid": null,
          "twilioAuthToken": null,
          "stripeSubscriptionStatus": "active",
          "stripeSubscriptionCurrentPeriodStart": "2024-06-29T03:02:56.000Z",
          "name": null,
          "credentialId": null,
          "serverUrl": null,
          "serverUrlSecret": null,
          "twilioOutgoingCallerId": null,
          "sipUri": null,
          "provider": "twilio",
          "fallbackForwardingPhoneNumber": null,
          "fallbackDestination": null,
          "squadId": null
        },
        "customer": {
          "number": "+19256831234"
        },
        "timestamp": "2024-07-25T06:07:29.733Z"
      }
    }
  """
}
