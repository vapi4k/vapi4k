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

package com.vapi4k.api.voice

import com.vapi4k.common.Constants.UNSPECIFIED_DEFAULT
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = VoiceProviderTypeSerializer::class)
enum class VoiceProviderType(
  val desc: String,
) {
  AZURE("azure"),
  CARTESIA("cartesia"),
  CUSTOM_VOICE("custom-voice"),
  DEEPGRAM("deepgram"),
  ELEVEN_LABS("11labs"),
  HUME("hume"),
  INWORLD("inworld"),
  LMNT("lmnt"),
  MINIMAX("minimax"),
  NEETS("neets"),
  NEUPHONIC("neuphonic"),
  OPEN_AI("openai"),
  PLAY_HT("playht"),
  RIME_AI("rime-ai"),
  SESAME("sesame"),
  SMALLEST_AI("smallest-ai"),
  TAVUS("tavus"),
  VAPI("vapi"),
  WELLSAID("wellsaid"),
  UNSPECIFIED(UNSPECIFIED_DEFAULT),
  ;

  fun isSpecified() = this != UNSPECIFIED

  fun isNotSpecified() = this == UNSPECIFIED
}

object VoiceProviderTypeSerializer : KSerializer<VoiceProviderType> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ProviderType", STRING)

  override fun serialize(
    encoder: Encoder,
    value: VoiceProviderType,
  ) = encoder.encodeString(value.desc)

  override fun deserialize(decoder: Decoder) = VoiceProviderType.entries.first { it.desc == decoder.decodeString() }
}
