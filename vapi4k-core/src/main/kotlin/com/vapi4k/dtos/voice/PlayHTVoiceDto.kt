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

package com.vapi4k.dtos.voice

import com.vapi4k.api.voice.enums.PlayHTVoiceEmotionType
import com.vapi4k.api.voice.enums.PlayHTVoiceIdType
import com.vapi4k.api.voice.enums.PunctuationType
import com.vapi4k.api.voice.enums.VoiceProviderType
import com.vapi4k.dsl.voice.PlayHTVoiceProperties
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PlayHTVoiceDto(
  override var inputPreprocessingEnabled: Boolean? = null,
  override var inputReformattingEnabled: Boolean? = null,
  override var inputMinCharacters: Int = -1,
  override val inputPunctuationBoundaries: MutableSet<PunctuationType> = mutableSetOf(),
  override var fillerInjectionEnabled: Boolean? = null,
  var voiceId: String = "",
  @Transient
  override var voiceIdType: PlayHTVoiceIdType = PlayHTVoiceIdType.UNSPECIFIED,
  @Transient
  override var customVoiceId: String = "",
  override var speed: Double = -1.0,
  override var temperature: Double = -1.0,
  override var emotion: PlayHTVoiceEmotionType = PlayHTVoiceEmotionType.UNSPECIFIED,
  override var voiceGuidance: Double = -1.0,
  override var styleGuidance: Double = -1.0,
  override var textGuidance: Double = -1.0,
) : PlayHTVoiceProperties,
  CommonVoiceDto {
  @EncodeDefault
  val provider: VoiceProviderType = VoiceProviderType.PLAY_HT

  fun assignEnumOverrides() {
    voiceId = customVoiceId.ifEmpty { voiceIdType.desc }
  }

  override fun verifyValues() {
    if (voiceIdType.isNotSpecified() && customVoiceId.isEmpty())
      error("playHTVoice{} requires a voiceIdType or customVoiceId value")
    if (voiceIdType.isSpecified() && customVoiceId.isNotEmpty())
      error("playHTVoice{} cannot have both voiceIdType and customVoiceId values")
    if (voiceGuidance != -1.0 && (voiceGuidance < 1 || voiceGuidance > 6))
      error("playHTVoice{} voiceGuidance must be between 1 and 6")
    if (styleGuidance != -1.0 && (styleGuidance < 1 || styleGuidance > 30))
      error("playHTVoice{} styleGuidance must be between 1 and 30")
    if (textGuidance != -1.0 && (textGuidance < 1 || textGuidance > 2))
      error("playHTVoice{} textGuidance must be between 1 and 2")
//    if (speed != -1.0 && (speed < 0.1 || speed > 5))
//      error("playHTVoice{} speed must be between 0.1 and 5")
  }
}
