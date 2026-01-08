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

package com.vapi4k.dsl.call

import com.github.pambrose.common.json.get
import com.github.pambrose.common.json.keys
import com.github.pambrose.common.json.toJsonElement
import com.vapi4k.api.call.OutboundCall
import com.vapi4k.api.call.Phone
import com.vapi4k.common.CoreEnvVars.vapi4kBaseUrl
import com.vapi4k.common.CoreEnvVars.vapiBaseUrl
import com.vapi4k.common.ErrorMessages.INVALID_BASE_URL
import com.vapi4k.common.Headers.VAPI_SECRET_HEADER
import com.vapi4k.common.Utils.ensureStartsWith
import com.vapi4k.common.Utils.errorMsg
import com.vapi4k.dsl.call.VapiApiImpl.Companion.configCall
import com.vapi4k.dsl.vapi4k.ApplicationType.OUTBOUND_CALL
import com.vapi4k.plugin.Vapi4kServer.logger
import com.vapi4k.utils.HttpUtils.jsonHttpClient
import com.vapi4k.utils.HttpUtils.stripQueryParams
import com.vapi4k.utils.MiscUtils.removeEnds
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.buildJsonObject
import java.net.URI

object ProcessOutboundCall {
  internal fun processOutboundCall(
    authString: String,
    block: Phone.() -> OutboundCall,
  ): HttpResponse =
    runBlocking {
      val outboundCall =
        PhoneImpl().runCatching(block)
          .onFailure { e -> logger.error { "Failed to create outbound call: ${e.errorMsg}" } }
          .getOrThrow()

      val url = "$vapi4kBaseUrl/${OUTBOUND_CALL.pathPrefix}/${outboundCall.serverPath.removeEnds("/")}"
      val assistantResponse =
        runCatching {
          jsonHttpClient().use { client ->
            if (outboundCall.method.isPost()) {
              client.post(url) {
                contentType(Application.Json)
                addVapiSecret(outboundCall)
                setBody(outboundCall.postArgs)
              }
            } else {
              client.get(url) {
                contentType(Application.Json)
                addVapiSecret(outboundCall)
              }
            }
          }
        }.onFailure { e -> logger.error { "Failed to fetch assistant from vapi4k server: ${e.errorMsg}" } }
          .getOrThrow()

      val status = assistantResponse.status
      if (status != HttpStatusCode.OK) {
        val stripped = url.stripQueryParams()
        when (status) {
          HttpStatusCode.NotFound -> {
            val path = URI(url).path.split("/").last().ensureStartsWith("/")
            error("""$INVALID_BASE_URL or invalid serverPath "$path" used in assistant definition: $stripped""")
          }

          HttpStatusCode.Unauthorized -> {
            error("Unauthorized to fetch assistant: $stripped")
          }

          else -> {
            val body = assistantResponse.bodyAsText().let { if (it.isBlank()) "" else "- $it" }
            error("Failed to fetch assistant $stripped from vapi4k server: $status $body")
          }
        }
      }

      val assistantJson = assistantResponse.bodyAsText().toJsonElement()

      val outboundDto = (outboundCall as OutboundCallImpl).outboundCallRequestDto
      val outboundJson = outboundDto.toJsonElement()

      val requestJson =
        buildJsonObject {
          outboundJson.keys.forEach { key -> put(key, outboundJson[key]) }
          assistantJson.keys.forEach { key -> put(key, assistantJson[key]) }
        }

      val vapiResponse =
        runCatching {
          jsonHttpClient().use { client ->
            client.post("$vapiBaseUrl/call/phone") {
              configCall(authString)
              setBody(requestJson)
            }
          }
        }.onFailure { e -> logger.error { "Failed calling $vapiBaseUrl: ${e.errorMsg}" } }
          .getOrThrow()

      if (vapiResponse.status.value != HttpStatusCode.Created.value) {
        val msg = "Failed calling $vapiBaseUrl: ${vapiResponse.status} - ${vapiResponse.bodyAsText()}"
        logger.error { msg }
        error(msg)
      }

      vapiResponse
    }

  private fun HttpRequestBuilder.addVapiSecret(call: OutboundCall) {
    if (call.serverSecret.isNotBlank()) {
      headers.append(VAPI_SECRET_HEADER, call.serverSecret)
    }
  }
}
