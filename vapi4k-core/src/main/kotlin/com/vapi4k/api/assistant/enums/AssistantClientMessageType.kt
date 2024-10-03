/*
 * Copyright © 2024 Matthew Ambrose (mattbobambrose@gmail.com)
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

package com.vapi4k.api.assistant.enums

import com.vapi4k.api.assistant.enums.AssistantClientMessageType.entries
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = AssistantClientMessageTypeSerializer::class)
enum class AssistantClientMessageType(
  val desc: String,
) {
  CONVERSATION_UPDATE("conversation-update"),
  FUNCTION_CALL("function-call"),
  FUNCTION_CALL_RESULT("function-call-result"),
  HANG("hang"),
  METADATA("metadata"),
  MODEL_OUTPUT("model-output"),
  SPEECH_UPDATE("speech-update"),
  STATUS_UPDATE("status-update"),
  TRANSCRIPT("transcript"),
  TOOL_CALLS("tool-calls"),
  TOOL_CALLS_RESULTS("tool-calls-results"),
  USER_INTERRUPTED("user-interrupted"),
  VOICE_INPUT("voice-input"),
}

private object AssistantClientMessageTypeSerializer : KSerializer<AssistantClientMessageType> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("AssistantClientMessageType", PrimitiveKind.STRING)

  override fun serialize(
    encoder: Encoder,
    value: AssistantClientMessageType,
  ) = encoder.encodeString(value.desc)

  override fun deserialize(decoder: Decoder) = entries.first { it.desc == decoder.decodeString() }
}
