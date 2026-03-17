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

package com.vapi4k.api.assistant

import com.vapi4k.common.Constants.UNSPECIFIED_DEFAULT
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = AssistantServerMessageTypeSerializer::class)
enum class AssistantServerMessageType(
  val desc: String,
) {
  ASSISTANT_STARTED("assistant.started"),
  CALL_DELETED("call.deleted"),
  CALL_DELETE_FAILED("call.delete.failed"),
  CHAT_CREATED("chat.created"),
  CHAT_DELETED("chat.deleted"),
  CONVERSATION_UPDATE("conversation-update"),
  END_OF_CALL_REPORT("end-of-call-report"),
  FUNCTION_CALL("function-call"),
  HANDOFF_DESTINATION_REQUEST("handoff-destination-request"),
  HANG("hang"),
  LANGUAGE_CHANGED("language-changed"),
  LANGUAGE_CHANGE_DETECTED("language-change-detected"),
  MODEL_OUTPUT("model-output"),
  PHONE_CALL_CONTROL("phone-call-control"),
  SESSION_CREATED("session.created"),
  SESSION_DELETED("session.deleted"),
  SESSION_UPDATED("session.updated"),
  SPEECH_UPDATE("speech-update"),
  STATUS_UPDATE("status-update"),
  TOOL_CALLS("tool-calls"),
  TRANSCRIPT("transcript"),
  TRANSCRIPT_FINAL("transcript[transcriptType=\"final\"]"),
  TRANSFER_DESTINATION_REQUEST("transfer-destination-request"),
  TRANSFER_UPDATE("transfer-update"),
  USER_INTERRUPTED("user-interrupted"),
  VOICE_INPUT("voice-input"),
  UNSPECIFIED(UNSPECIFIED_DEFAULT),
  ;

  fun isSpecified() = this != UNSPECIFIED

  fun isNotSpecified() = this == UNSPECIFIED
}

object AssistantServerMessageTypeSerializer : KSerializer<AssistantServerMessageType> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("AssistantServerMessageType", STRING)

  override fun serialize(
    encoder: Encoder,
    value: AssistantServerMessageType,
  ) = encoder.encodeString(value.desc)

  override fun deserialize(decoder: Decoder) =
    AssistantServerMessageType.entries.first { it.desc == decoder.decodeString() }
}
