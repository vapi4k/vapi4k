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

package com.vapi4k.api.assistant.enums

import com.vapi4k.common.Constants.UNSPECIFIED_DEFAULT
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = VoicemailDetectionTypeSerializer::class)
enum class VoicemailDetectionType(
  val desc: String,
) {
  FAX("fax"),
  HUMAN("human"),
  MACHINE_END_BEEP("machine_end_beep"),
  MACHINE_END_OTHER("machine_end_other"),
  MACHINE_END_SILENCE("machine_end_silence"),
  MACHINE_START("machine_start"),
  UNKNOWN("unknown"),
  UNSPECIFIED(UNSPECIFIED_DEFAULT),
}

private object VoicemailDetectionTypeSerializer : KSerializer<VoicemailDetectionType> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("VoicemailDetectionType", PrimitiveKind.STRING)

  override fun serialize(
    encoder: Encoder,
    value: VoicemailDetectionType,
  ) = encoder.encodeString(value.desc)

  override fun deserialize(decoder: Decoder) =
    VoicemailDetectionType.entries.first { it.desc == decoder.decodeString() }
}
