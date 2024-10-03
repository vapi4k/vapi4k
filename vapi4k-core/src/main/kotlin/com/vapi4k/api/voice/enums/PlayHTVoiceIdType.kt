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

package com.vapi4k.api.voice.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = PlayHTVoiceIdTypeSerializer::class)
enum class PlayHTVoiceIdType {
  CHRIS,
  DAVIS,
  DONNA,
  JACK,
  JENNIFER,
  MATT,
  MELISSA,
  MICHAEL,
  RUBY,
  WILL,
  UNSPECIFIED,
  ;

  val desc get() = name.lowercase()

  fun next() = names[(ordinal + 1) % names.size]

  fun previous() = names[(ordinal - 1 + names.size) % names.size]

  internal fun isSpecified() = this != UNSPECIFIED

  internal fun isNotSpecified() = this == UNSPECIFIED

  companion object {
    val names by lazy { entries.filterNot { it == UNSPECIFIED } }
  }
}

private object PlayHTVoiceIdTypeSerializer : KSerializer<PlayHTVoiceIdType> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PlayHTVoiceIdType", STRING)

  override fun serialize(
    encoder: Encoder,
    value: PlayHTVoiceIdType,
  ) = encoder.encodeString(value.desc)

  override fun deserialize(decoder: Decoder) = PlayHTVoiceIdType.entries.first { it.desc == decoder.decodeString() }
}
