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
  ASSISTANT_STARTED("assistant.started"),
  ASSISTANT_REQUEST("assistant-request"),
  CALL_DELETED("call.deleted"),
  CALL_DELETE_FAILED("call.delete.failed"),
  CALL_ENDPOINTING_REQUEST("call.endpointing.request"),
  CHAT_CREATED("chat.created"),
  CHAT_DELETED("chat.deleted"),
  CONVERSATION_UPDATE("conversation-update"),
  END_OF_CALL_REPORT("end-of-call-report"),
  FUNCTION_CALL("function-call"),
  HANDOFF_DESTINATION_REQUEST("handoff-destination-request"),
  HANG("hang"),
  KNOWLEDGE_BASE_REQUEST("knowledge-base-request"),
  LANGUAGE_CHANGE_DETECTED("language-change-detected"),
  MODEL_OUTPUT("model-output"),
  PHONE_CALL_CONTROL("phone-call-control"),
  SESSION_CREATED("session.created"),
  SESSION_DELETED("session.deleted"),
  SESSION_UPDATED("session.updated"),
  SPEECH_UPDATE("speech-update"),
  STATUS_UPDATE("status-update"),
  TOOL_CALL("tool-calls"),
  TRANSCRIPT("transcript"),
  TRANSFER_DESTINATION_REQUEST("transfer-destination-request"),
  TRANSFER_UPDATE("transfer-update"),
  USER_INTERRUPTED("user-interrupted"),
  VOICE_INPUT("voice-input"),
  VOICE_REQUEST("voice-request"),
  UNKNOWN_REQUEST_TYPE("unknown-request-type"),
  ;

  companion object {
    internal val logger = KotlinLogging.logger {}

    fun JsonElement.isAssistantStarted() = serverRequestType == ASSISTANT_STARTED

    fun JsonElement.isAssistantRequest() = serverRequestType == ASSISTANT_REQUEST

    fun JsonElement.isCallDeleted() = serverRequestType == CALL_DELETED

    fun JsonElement.isCallDeleteFailed() = serverRequestType == CALL_DELETE_FAILED

    fun JsonElement.isCallEndpointingRequest() = serverRequestType == CALL_ENDPOINTING_REQUEST

    fun JsonElement.isChatCreated() = serverRequestType == CHAT_CREATED

    fun JsonElement.isChatDeleted() = serverRequestType == CHAT_DELETED

    fun JsonElement.isConversationUpdate() = serverRequestType == CONVERSATION_UPDATE

    fun JsonElement.isEndOfCallReport() = serverRequestType == END_OF_CALL_REPORT

    fun JsonElement.isFunctionCall() = serverRequestType == FUNCTION_CALL

    fun JsonElement.isHandoffDestinationRequest() = serverRequestType == HANDOFF_DESTINATION_REQUEST

    fun JsonElement.isHang() = serverRequestType == HANG

    fun JsonElement.isKnowledgeBaseRequest() = serverRequestType == KNOWLEDGE_BASE_REQUEST

    fun JsonElement.isLanguageChangeDetected() = serverRequestType == LANGUAGE_CHANGE_DETECTED

    fun JsonElement.isModelOutput() = serverRequestType == MODEL_OUTPUT

    fun JsonElement.isPhoneCallControl() = serverRequestType == PHONE_CALL_CONTROL

    fun JsonElement.isSessionCreated() = serverRequestType == SESSION_CREATED

    fun JsonElement.isSessionDeleted() = serverRequestType == SESSION_DELETED

    fun JsonElement.isSessionUpdated() = serverRequestType == SESSION_UPDATED

    fun JsonElement.isSpeechUpdate() = serverRequestType == SPEECH_UPDATE

    fun JsonElement.isStatusUpdate() = serverRequestType == STATUS_UPDATE

    fun JsonElement.isToolCall() = serverRequestType == TOOL_CALL

    fun JsonElement.isTransferDestinationRequest() = serverRequestType == TRANSFER_DESTINATION_REQUEST

    fun JsonElement.isTransferUpdate() = serverRequestType == TRANSFER_UPDATE

    fun JsonElement.isUserInterrupted() = serverRequestType == USER_INTERRUPTED

    fun JsonElement.isVoiceInput() = serverRequestType == VOICE_INPUT

    fun JsonElement.isVoiceRequest() = serverRequestType == VOICE_REQUEST

    val JsonElement.serverRequestType: ServerRequestType
      get() {
        val messageType = stringValue("message.type")
        return try {
          entries.first { it.desc == messageType }
        } catch (e: Exception) {
          logger.error { "Invalid ServerRequestType: $messageType" }
          UNKNOWN_REQUEST_TYPE
        }
      }
  }
}
