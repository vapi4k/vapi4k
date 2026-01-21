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

package com.vapi4k.dtos.voice

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = VoiceSerializer::class)
interface CommonVoiceDto {
  fun verifyValues()
}

object VoiceSerializer : KSerializer<CommonVoiceDto> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("CommonVoiceDto")

  override fun serialize(
    encoder: Encoder,
    value: CommonVoiceDto,
  ) {
    when (value) {
      is AzureVoiceDto -> {
        value.assignEnumOverrides()
        encoder.encodeSerializableValue(AzureVoiceDto.serializer(), value)
      }

      is CartesiaVoiceDto -> {
        value.assignEnumOverrides()
        encoder.encodeSerializableValue(CartesiaVoiceDto.serializer(), value)
      }

      is DeepgramVoiceDto -> {
        value.assignEnumOverrides()
        encoder.encodeSerializableValue(DeepgramVoiceDto.serializer(), value)
      }

      is ElevenLabsVoiceDto -> {
        value.assignEnumOverrides()
        encoder.encodeSerializableValue(ElevenLabsVoiceDto.serializer(), value)
      }

      is LMNTVoiceDto -> {
        value.assignEnumOverrides()
        encoder.encodeSerializableValue(LMNTVoiceDto.serializer(), value)
      }

      is NeetsVoiceDto -> {
        value.assignEnumOverrides()
        encoder.encodeSerializableValue(NeetsVoiceDto.serializer(), value)
      }

      is OpenAIVoiceDto -> {
        value.assignEnumOverrides()
        encoder.encodeSerializableValue(OpenAIVoiceDto.serializer(), value)
      }

      is PlayHTVoiceDto -> {
        value.assignEnumOverrides()
        encoder.encodeSerializableValue(PlayHTVoiceDto.serializer(), value)
      }

      is RimeAIVoiceDto -> {
        value.assignEnumOverrides()
        encoder.encodeSerializableValue(RimeAIVoiceDto.serializer(), value)
      }

      else -> {
        error("Invalid voice provider: ${value::class.simpleName}")
      }
    }
  }

  override fun deserialize(decoder: Decoder): CommonVoiceDto =
    throw NotImplementedError("Deserialization is not supported")
}
