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

@Serializable(with = CartesiaVoiceLanguageSerializer::class)
enum class CartesiaVoiceLanguageType(
  val desc: String,
) {
  ARABIC("ar"),
  BENGALI("bn"),
  BULGARIAN("bg"),
  CHINESE("zh"),
  CROATIAN("hr"),
  CZECH("cs"),
  DANISH("da"),
  DUTCH("nl"),
  ENGLISH("en"),
  FINNISH("fi"),
  FRENCH("fr"),
  GEORGIAN("ka"),
  GERMAN("de"),
  GREEK("el"),
  GUJARATI("gu"),
  HEBREW("he"),
  HINDI("hi"),
  HUNGARIAN("hu"),
  INDONESIAN("id"),
  ITALIAN("it"),
  JAPANESE("ja"),
  KANNADA("kn"),
  KOREAN("ko"),
  MALAY("ms"),
  MALAYALAM("ml"),
  MARATHI("mr"),
  NORWEGIAN("no"),
  POLISH("pl"),
  PUNJABI("pa"),
  PORTUGUESE("pt"),
  ROMANIAN("ro"),
  RUSSIAN("ru"),
  SLOVAK("sk"),
  SPANISH("es"),
  SWEDISH("sv"),
  TAGALOG("tl"),
  TAMIL("ta"),
  TELUGU("te"),
  THAI("th"),
  TURKISH("tr"),
  UKRAINIAN("uk"),
  VIETNAMESE("vi"),
  UNSPECIFIED(UNSPECIFIED_DEFAULT),
  ;

  fun isSpecified() = this != UNSPECIFIED

  fun isNotSpecified() = this == UNSPECIFIED
}

object CartesiaVoiceLanguageSerializer : KSerializer<CartesiaVoiceLanguageType> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CartesiaVoiceLanguageType", STRING)

  override fun serialize(
    encoder: Encoder,
    value: CartesiaVoiceLanguageType,
  ) = encoder.encodeString(value.desc)

  override fun deserialize(decoder: Decoder) =
    CartesiaVoiceLanguageType.entries.first { it.desc == decoder.decodeString() }
}
