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

@Serializable(with = PlayHTVoiceEmotionTypeSerializer::class)
enum class PlayHTVoiceEmotionType(
  val desc: String,
) {
  FEMALE_ANGRY("female_angry"),
  FEMALE_DISGUST("female_disgust"),
  FEMALE_FEARFUL("female_fearful"),
  FEMALE_HAPPY("female_happy"),
  FEMALE_SAD("female_sad"),
  FEMALE_SURPRISED("female_surprised"),
  MALE_ANGRY("male_angry"),
  MALE_DISGUST("male_disgust"),
  MALE_FEARFUL("male_fearful"),
  MALE_HAPPY("male_happy"),
  MALE_SAD("male_sad"),
  MALE_SURPRISED("male_surprised"),
  UNSPECIFIED(UNSPECIFIED_DEFAULT),
  ;

  fun isSpecified() = this != UNSPECIFIED

  fun isNotSpecified() = this == UNSPECIFIED
}

object PlayHTVoiceEmotionTypeSerializer : KSerializer<PlayHTVoiceEmotionType> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PlayHTVoiceEmotionType", STRING)

  override fun serialize(
    encoder: Encoder,
    value: PlayHTVoiceEmotionType,
  ) = encoder.encodeString(value.desc)

  override fun deserialize(decoder: Decoder) =
    PlayHTVoiceEmotionType.entries.first { it.desc == decoder.decodeString() }
}
