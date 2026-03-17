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

import com.vapi4k.api.assistant.AssistantClientMessageType.entries
import com.vapi4k.common.Constants.UNSPECIFIED_DEFAULT
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = AssistantClientMessageTypeSerializer::class)
enum class AssistantClientMessageType(
  val desc: String,
) {
  ASSISTANT_STARTED("assistant.started"),
  CONVERSATION_UPDATE("conversation-update"),
  FUNCTION_CALL("function-call"),
  FUNCTION_CALL_RESULT("function-call-result"),
  HANG("hang"),
  LANGUAGE_CHANGED("language-changed"),
  METADATA("metadata"),
  MODEL_OUTPUT("model-output"),
  SPEECH_UPDATE("speech-update"),
  STATUS_UPDATE("status-update"),
  TOOL_CALLS("tool-calls"),
  TOOL_CALLS_RESULT("tool-calls-result"),
  TOOL_COMPLETED("tool.completed"),
  TRANSCRIPT("transcript"),
  TRANSFER_UPDATE("transfer-update"),
  USER_INTERRUPTED("user-interrupted"),
  VOICE_INPUT("voice-input"),
  WORKFLOW_NODE_STARTED("workflow.node.started"),
  UNSPECIFIED(UNSPECIFIED_DEFAULT),
  ;

  fun isSpecified() = this != UNSPECIFIED

  fun isNotSpecified() = this == UNSPECIFIED
}

object AssistantClientMessageTypeSerializer : KSerializer<AssistantClientMessageType> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("AssistantClientMessageType", STRING)

  override fun serialize(
    encoder: Encoder,
    value: AssistantClientMessageType,
  ) = encoder.encodeString(value.desc)

  override fun deserialize(decoder: Decoder) = entries.first { it.desc == decoder.decodeString() }
}
