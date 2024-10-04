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

import com.vapi4k.api.json.stringValue
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.json.JsonElement

enum class ServerRequestType(
  val desc: String,
) {
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

    fun JsonElement.isAssistantRequest() = serverRequestType == ASSISTANT_REQUEST

    fun JsonElement.isConversationUpdate() = serverRequestType == CONVERSATION_UPDATE

    fun JsonElement.isEndOfCallReport() = serverRequestType == END_OF_CALL_REPORT

    fun JsonElement.isFunctionCall() = serverRequestType == FUNCTION_CALL

    fun JsonElement.isHang() = serverRequestType == HANG

    fun JsonElement.isPhoneCallControl() = serverRequestType == PHONE_CALL_CONTROL

    fun JsonElement.isSpeechUpdate() = serverRequestType == SPEECH_UPDATE

    fun JsonElement.isStatusUpdate() = serverRequestType == STATUS_UPDATE

    fun JsonElement.isToolCall() = serverRequestType == TOOL_CALL

    fun JsonElement.isTransferDestinationRequest() = serverRequestType == TRANSFER_DESTINATION_REQUEST

    fun JsonElement.isUserInterrupted() = serverRequestType == USER_INTERRUPTED

    val JsonElement.serverRequestType: ServerRequestType
      get() {
        val desc = stringValue("message.type")
        return try {
          entries.first { it.desc == desc }
        } catch (e: Exception) {
          logger.error { "Invalid ServerMessageType: $desc" }
          UNKNOWN_REQUEST_TYPE
        }
      }
  }
}
