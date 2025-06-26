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

import com.github.pambrose.common.json.containsKey
import com.github.pambrose.common.json.get
import com.github.pambrose.common.json.stringValue
import com.vapi4k.api.vapi4k.ServerRequestType.Companion.isFunctionCall
import com.vapi4k.api.vapi4k.ServerRequestType.Companion.isStatusUpdate
import com.vapi4k.common.FunctionName.Companion.toFunctionName
import kotlinx.serialization.json.JsonElement

/**
Utility functions for assistant requests.
 */
object AssistantRequestUtils {
  /**
  Check if the JsonElement is an assistant response.
   */
  fun JsonElement.isAssistantResponse() = containsKey("messageResponse.assistant")

  /**
  Check if the JsonElement is an assistant id response.
   */
  fun JsonElement.isAssistantIdResponse() = containsKey("messageResponse.assistantId")

  /**
  Check if the JsonElement is a squad response.
   */
  fun JsonElement.isSquadResponse() = containsKey("messageResponse.squad")

  /**
  Check if the JsonElement is a squad id response.
   */
  fun JsonElement.isSquadIdResponse() = containsKey("messageResponse.squadId")

  /**
  Extract the id from a JsonElement.
   */
  val JsonElement.id get() = stringValue("id")

  /**
  Extract the message call id from a JsonElement.
   */
  val JsonElement.messageCallId get() = stringValue("message.call.id")

  /**
  Extract the request type from a JsonElement.
   */
  val JsonElement.requestType get() = stringValue("message.type")

  /**
  Extract the phone number from a JsonElement.
   */
  val JsonElement.phoneNumber get() = stringValue("message.call.customer.number")

  /**
  Extract the tool call name from a JsonElement.
   */
  val JsonElement.toolCallName get() = stringValue("function.name").toFunctionName()

  /**
  Extract the listenUrl value from a JsonElement.
   */
  val JsonElement.listenUrl get() = stringValue("message.call.monitor.listenUrl")

  /**
  Extract the controlUrl value from a JsonElement.
   */
  val JsonElement.controlUrl get() = stringValue("message.call.monitor.controlUrl")

  /**
  Extract the webCallUrl value from a JsonElement.
   */
  val JsonElement.webCallUrl get() = stringValue("message.call.webCallUrl")

  /**
  Extract the tool call arguments from a JsonElement.
   */
  val JsonElement.toolCallArguments: JsonElement get() = this["function.arguments"]
//    get() {
//      val args = this["function.arguments"]
//      return buildJsonObject {
//        args.keys
//          .filterNot { it.startsWith(ID_PREFIX) }
//          .forEach { put(it, args[it]) }
//      }
//    }

  /**
  Extract the function name from a function call from a JsonElement.
   */
  val JsonElement.functionName
    get() = if (isFunctionCall())
      stringValue("message.functionCall.name").toFunctionName()
    else
      error("JsonElement is not a function call")

  /**
  Extract the function parameters from a function call from a JsonElement.
   */
  val JsonElement.functionParameters
    get() = if (isFunctionCall())
      this["message.functionCall.parameters"]
    else
      error("JsonElement is not a function call")

  /**
  Extract the assistant request error message from a status update message.
   */
  val JsonElement.statusUpdateError: String
    get() = if (isStatusUpdate()) {
      runCatching {
        stringValue("message.inboundPhoneCallDebuggingArtifacts.assistantRequestError")
      }.getOrElse { "" }
    } else {
      error("Not a status update message. Use .isStatusUpdate() to determine if .statusUpdateError is appropriate")
    }

  /**
  Check if the JsonElement has a status update error message.
   */
  fun JsonElement.hasStatusUpdateError(): Boolean = statusUpdateError.isNotEmpty()
}
