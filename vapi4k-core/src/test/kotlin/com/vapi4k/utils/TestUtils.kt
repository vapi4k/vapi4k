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

package com.vapi4k.utils

import com.vapi4k.ServerTest.Companion.configPost
import com.vapi4k.api.json.containsKey
import com.vapi4k.api.json.get
import com.vapi4k.api.json.jsonElementList
import com.vapi4k.api.json.stringValue
import com.vapi4k.api.json.toJsonElement
import com.vapi4k.api.json.toJsonElementList
import com.vapi4k.api.json.toJsonString
import com.vapi4k.api.response.InboundCallAssistantResponse
import com.vapi4k.api.tools.ToolMessageType
import com.vapi4k.common.CoreEnvVars.defaultServerPath
import com.vapi4k.common.Utils.resourceFile
import com.vapi4k.dsl.response.InboundCallAssistantResponseImpl
import com.vapi4k.dsl.vapi4k.AbstractApplicationImpl
import com.vapi4k.dsl.vapi4k.ApplicationType
import com.vapi4k.dtos.tools.ToolMessageCondition
import com.vapi4k.plugin.Vapi4k
import com.vapi4k.responses.AssistantMessageResponse
import com.vapi4k.server.RequestContextImpl
import com.vapi4k.utils.HttpUtils.queryParams
import com.vapi4k.utils.JsonUtils.EMPTY_JSON_ELEMENT
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.install
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.JsonElement

fun assistantResponse(
  requestContext: RequestContextImpl,
  block: InboundCallAssistantResponse.() -> Unit,
): AssistantMessageResponse {
  val assistantResponse = InboundCallAssistantResponseImpl(requestContext).apply(block)
  return if (assistantResponse.isAssigned)
    assistantResponse.assistantRequestResponse
  else
    error("assistantResponse{} is missing a call to assistant{}, assistantId{}, squad{}, or squadId{}")
}

fun JsonElement.tools() = jsonElementList("messageResponse.assistant.model.tools")

fun JsonElement.firstToolMessages() = tools().first().jsonElementList("messages")

fun JsonElement.firstMessageOfType(
  type: ToolMessageType,
  vararg conditions: ToolMessageCondition,
) = if (conditions.isEmpty())
  firstToolMessages()
    .filter { !it.containsKey("conditions") }
    .first { it.stringValue("type") == type.desc }
else
  firstToolMessages()
    .filter { it.containsKey("conditions") }
    .filter {
      conditions.all { c -> it.jsonElementList("conditions").contains(c.toJsonElement()) }
    }
    .first { it.stringValue("type") == type.desc }

fun withTestApplication(
  appType: ApplicationType,
  fileName: String,
  block: InboundCallAssistantResponse.() -> Unit,
): Pair<HttpResponse, JsonElement> {
  var response: HttpResponse? = null
  var je: JsonElement? = null
  testApplication {
    application {
      install(Vapi4k) {
        inboundCallApplication {
          onAssistantRequest {
            block()
          }
        }
      }
    }

    response =
      client.post("/${appType.pathPrefix}/$defaultServerPath") {
        configPost()
        setBody(resourceFile(fileName))
      }

    je =
      if (response!!.status == HttpStatusCode.OK)
        response!!.bodyAsText().toJsonElement()
      else
        EMPTY_JSON_ELEMENT
  }
  return response!! to je!!
}

fun withTestApplication(
  appType: ApplicationType,
  fileNames: List<String>,
  getArg: String = "",
  cacheRemovalEnabled: Boolean = true,
  block: InboundCallAssistantResponse.() -> Unit,
): List<Pair<HttpResponse, JsonElement>> {
  val responses: MutableList<Pair<HttpResponse, JsonElement>> = mutableListOf()
  testApplication {
    application {
      install(Vapi4k) {
        inboundCallApplication {
          (this as AbstractApplicationImpl).eocrCacheRemovalEnabled = cacheRemovalEnabled

          onAssistantRequest {
            block()
          }
        }
      }
    }
// zzz
    val baseUrl = "/${appType.pathPrefix}/$defaultServerPath"
    var url = baseUrl
    responses
      .addAll(
        fileNames.map { fileName ->
          println("url: $url")
          val arg = resourceFile(fileName)
          // val jsonArg = arg.toJsonElement()

          val response = client.post(url) {
            configPost()
            setBody(arg)
          }

          val body = response.bodyAsText().toJsonElement()
          println("response: ${body.toJsonString()}")
          val members = "messageResponse.squad.members"
          if (body.containsKey(members)) {
            val qp = body[members].toJsonElementList().first().stringValue("assistant.serverUrl").queryParams()
            url = "$baseUrl?$qp"
            println("response: ${body.toJsonString()}")
            println(arg)
          }
          response to body
        },
      )

    if (getArg.isNotEmpty()) {
      responses.add(
        client.get(getArg) { configPost() }.let { it to it.bodyAsText().toJsonElement() },
      )
    }
  }
  return responses
}
