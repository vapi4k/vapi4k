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

import com.github.pambrose.common.json.stringValue
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.json.JsonElement

enum class ServerRequestType(
  val desc: String,
) {
  // ASSISTANT_STARTED("assistant.started"),
  ASSISTANT_REQUEST("assistant-request"),
  CONVERSATION_UPDATE("conversation-update"),
  END_OF_CALL_REPORT("end-of-call-report"),
  FUNCTION_CALL("function-call"),
  HANG("hang"),
  PHONE_CALL_CONTROL("phone-call-control"),
  SPEECH_UPDATE("speech-update"),
  STATUS_UPDATE("status-update"),
  TOOL_CALL("tool-calls"),
  TRANSCRIPT("transcript"),
  TRANSFER_DESTINATION_REQUEST("transfer-destination-request"),
  USER_INTERRUPTED("user-interrupted"),
  UNKNOWN_REQUEST_TYPE("unknown-request-type"),
  ;

  companion object {
    internal val logger = KotlinLogging.logger {}

    fun JsonElement.isAssistantRequest() = getServerRequestType("isAssistantRequest") == ASSISTANT_REQUEST

    fun JsonElement.isConversationUpdate() = getServerRequestType("isConversationUpdate") == CONVERSATION_UPDATE

    fun JsonElement.isEndOfCallReport() = getServerRequestType("isEndOfCallReport") == END_OF_CALL_REPORT

    fun JsonElement.isFunctionCall() = getServerRequestType("isFunctionCall") == FUNCTION_CALL

    fun JsonElement.isHang() = getServerRequestType("isHang") == HANG

    fun JsonElement.isPhoneCallControl() = getServerRequestType("isPhoneCallControl") == PHONE_CALL_CONTROL

    fun JsonElement.isSpeechUpdate() = getServerRequestType("isSpeechUpdate") == SPEECH_UPDATE

    fun JsonElement.isStatusUpdate() = getServerRequestType("isStatusUpdate") == STATUS_UPDATE

    fun JsonElement.isToolCall() = getServerRequestType("isToolCall") == TOOL_CALL

    fun JsonElement.isTransferDestinationRequest() =
      getServerRequestType("isTransferDestinationRequest") == TRANSFER_DESTINATION_REQUEST

    fun JsonElement.isUserInterrupted() = getServerRequestType("isUserInterrupted") == USER_INTERRUPTED

    fun JsonElement.getServerRequestType(context: String): ServerRequestType {
      val desc = stringValue("message.type")
      return try {
        entries.first { it.desc == desc }
      } catch (e: Exception) {
        logger.error { "Invalid ServerRequestType: $desc from $context" }
        UNKNOWN_REQUEST_TYPE
      }
    }
  }
}
