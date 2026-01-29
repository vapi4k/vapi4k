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

package com.vapi4k.dsl.vapi4k

import com.vapi4k.api.vapi4k.InboundCallApplication
import com.vapi4k.api.vapi4k.OutboundCallApplication
import com.vapi4k.api.vapi4k.RequestContext
import com.vapi4k.api.vapi4k.ResponseContext
import com.vapi4k.api.vapi4k.ServerRequestType
import com.vapi4k.api.vapi4k.Vapi4kConfig
import com.vapi4k.api.vapi4k.WebApplication
import com.vapi4k.common.ApplicationId
import com.vapi4k.dsl.vapi4k.ApplicationType.INBOUND_CALL
import com.vapi4k.dsl.vapi4k.ApplicationType.OUTBOUND_CALL
import com.vapi4k.dsl.vapi4k.ApplicationType.WEB
import com.vapi4k.server.RequestResponseCallback
import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.channels.Channel

typealias RequestArgs = suspend (RequestContext) -> Unit
typealias ResponseArgs = suspend (ResponseContext) -> Unit

class Vapi4kConfigImpl internal constructor() : Vapi4kConfig {
  init {
    config = this
  }

  internal lateinit var applicationConfig: ApplicationConfig
  internal lateinit var callbackChannel: Channel<RequestResponseCallback>

  internal val globalAllRequests = mutableListOf<(RequestArgs)>()
  internal val globalPerRequests = mutableListOf<Pair<ServerRequestType, RequestArgs>>()
  internal val globalAllResponses = mutableListOf<ResponseArgs>()
  internal val globalPerResponses = mutableListOf<Pair<ServerRequestType, ResponseArgs>>()

  internal val inboundCallApplications = mutableListOf<AbstractApplicationImpl>()
  internal val outboundCallApplications = mutableListOf<AbstractApplicationImpl>()
  internal val webApplications = mutableListOf<AbstractApplicationImpl>()

  internal val allApplications get() = inboundCallApplications + outboundCallApplications + webApplications

  override var enableJsonVerboseLogging: Boolean = false

  internal val isVerbose: Boolean
    get() = enableJsonVerboseLogging

  private fun verifyServerPath(
    serverPath: String,
    name: String,
    applications: List<AbstractApplicationImpl>,
  ) {
    if (applications.any { it.serverPath == serverPath })
      error("$name with serverPath \"${serverPath}\" already exists")
  }

  override fun inboundCallApplication(block: InboundCallApplication.() -> Unit): InboundCallApplication =
    InboundCallApplicationImpl()
      .apply(block)
      .also { ica ->
        verifyServerPath(ica.serverPath, INBOUND_CALL.functionName, inboundCallApplications)
        inboundCallApplications += ica
      }

  override fun outboundCallApplication(block: OutboundCallApplication.() -> Unit): OutboundCallApplication =
    OutboundCallApplicationImpl()
      .apply(block)
      .also { ica ->
        verifyServerPath(ica.serverPath, OUTBOUND_CALL.functionName, outboundCallApplications)
        outboundCallApplications += ica
      }

  override fun webApplication(block: WebApplication.() -> Unit): WebApplication =
    WebApplicationImpl()
      .apply(block)
      .also { app ->
        verifyServerPath(app.serverPath, WEB.functionName, webApplications)
        webApplications += app
      }

  override fun onAllRequests(block: suspend (requestContext: RequestContext) -> Unit) {
    globalAllRequests += block
  }

  override fun onRequest(
    requestType: ServerRequestType,
    vararg requestTypes: ServerRequestType,
    block: suspend (requestContext: RequestContext) -> Unit,
  ) = (listOf(requestType) + requestTypes).forEach { globalPerRequests += it to block }

  override fun onAllResponses(block: suspend (responseContext: ResponseContext) -> Unit) {
    globalAllResponses += block
  }

  override fun onResponse(
    requestType: ServerRequestType,
    vararg requestTypes: ServerRequestType,
    block: suspend (responseContext: ResponseContext) -> Unit,
  ) = (listOf(requestType) + requestTypes).forEach { globalPerResponses += it to block }

  internal fun getApplicationById(applicationId: ApplicationId): AbstractApplicationImpl =
    allApplications.firstOrNull { it.applicationId == applicationId }
      ?: error("Application not found for applicationId: $applicationId")

  companion object {
    internal lateinit var config: Vapi4kConfigImpl
  }
}
