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

import com.github.pambrose.common.json.toJsonString
import com.vapi4k.api.call.ApiObjectType
import com.vapi4k.api.call.OutboundCall
import com.vapi4k.api.call.Phone
import com.vapi4k.api.call.Save
import com.vapi4k.api.call.VapiApi
import com.vapi4k.common.Constants.PRIVATE_KEY_PROPERTY
import com.vapi4k.common.CoreEnvVars.vapiBaseUrl
import com.vapi4k.common.CoreEnvVars.vapiPrivateKey
import com.vapi4k.common.Utils.errorMsg
import com.vapi4k.dsl.call.ProcessOutboundCall.processOutboundCall
import com.vapi4k.envvar.EnvVar.Companion.getSystemValue
import com.vapi4k.plugin.Vapi4kServer.logger
import com.vapi4k.utils.HttpUtils.jsonHttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType.Application
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking

class VapiApiImpl private constructor(
  private val authString: String,
) : VapiApi {
  override fun phone(block: Phone.() -> OutboundCall): HttpResponse = processOutboundCall(authString, block)

  internal fun test(block: Phone.() -> OutboundCall) =
    runBlocking {
      PhoneImpl().runCatching(block)
        .onSuccess { logger.info { "Created call request: ${it.toJsonString()}" } }
        .onFailure { e -> logger.error { "Failed to create call request: ${e.errorMsg}" } }
        .getOrThrow()
    }

  override fun save(block: Save.() -> OutboundCall) =
    runBlocking {
      val callRequest =
        Save().runCatching(block)
          .onSuccess { logger.info { "Created call request: ${it.toJsonString()}" } }
          .onFailure { e -> logger.error { "Failed to create call request: ${e.errorMsg}" } }
          .getOrThrow()

      runCatching {
        jsonHttpClient().use { client ->
          client.post("$vapiBaseUrl/call") {
            configCall(authString)
            setBody(callRequest)
          }
        }
      }.onSuccess { logger.info { "Call saved successfully" } }
        .onFailure { e -> logger.error { "Failed to save call: ${e.errorMsg}" } }
        .getOrThrow()
    }

  override fun list(objectType: ApiObjectType) =
    runBlocking {
      runCatching {
        runCatching {
          jsonHttpClient().use { client ->
            client.get("$vapiBaseUrl/${objectType.endpoint}") { configCall(authString) }
          }
        }.onSuccess { logger.info { "$objectType objects fetched successfully" } }
          .onFailure { e -> logger.error { "Failed to fetch $objectType objects: ${e.errorMsg}" } }
          .getOrThrow()
      }
    }.getOrThrow()

  override fun delete(callId: String) =
    runBlocking {
      runCatching {
        runCatching {
          jsonHttpClient().use { client ->
            client.get("$vapiBaseUrl/call/$callId") { configCall(authString) }
          }
        }.onSuccess { logger.info { "Call deleted successfully" } }
          .onFailure { e -> logger.error { "Failed to delete call: ${e.errorMsg}" } }
          .getOrThrow()
      }
    }.getOrThrow()

  companion object {
    internal fun HttpRequestBuilder.configCall(authString: String) {
      contentType(Application.Json)
      bearerAuth(authString)
    }

    fun vapiApi(authString: String = ""): VapiApi {
      val apiAuth =
        authString.ifBlank {
          getSystemValue(vapiPrivateKey, PRIVATE_KEY_PROPERTY) {
            "VAPI private api key needs to be assigned with $PRIVATE_KEY_PROPERTY in application.conf, " +
              "VAPI_PRIVATE_KEY env var, or passing an authString argument in vapiApi()"
          }
        }

      return VapiApiImpl(apiAuth)
    }
  }
}
