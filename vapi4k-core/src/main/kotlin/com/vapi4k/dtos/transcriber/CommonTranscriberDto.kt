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

package com.vapi4k.dtos.transcriber

import com.vapi4k.api.transcriber.TranscriberType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = TranscriberSerializer::class)
interface CommonTranscriberDto {
  val provider: TranscriberType
}

private object TranscriberSerializer : KSerializer<CommonTranscriberDto> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("CommonTranscriberDto")

  override fun serialize(
    encoder: Encoder,
    value: CommonTranscriberDto,
  ) {
    when (value) {
      is DeepgramTranscriberDto -> {
        value.assignEnumOverrides()
        encoder.encodeSerializableValue(DeepgramTranscriberDto.serializer(), value)
      }

      is GladiaTranscriberDto -> {
        value.assignEnumOverrides()
        encoder.encodeSerializableValue(GladiaTranscriberDto.serializer(), value)
      }

      is TalkscriberTranscriberDto -> {
        value.assignEnumOverrides()
        encoder.encodeSerializableValue(TalkscriberTranscriberDto.serializer(), value)
      }

      else -> error("Invalid transcriber provider: ${value::class.simpleName}")
    }
  }

  override fun deserialize(decoder: Decoder): CommonTranscriberDto =
    throw NotImplementedError("Deserialization is not supported")
}
