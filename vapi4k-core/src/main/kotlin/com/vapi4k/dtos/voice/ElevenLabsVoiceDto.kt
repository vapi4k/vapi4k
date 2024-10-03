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

import com.vapi4k.api.voice.enums.ElevenLabsVoiceIdType
import com.vapi4k.api.voice.enums.ElevenLabsVoiceModelType
import com.vapi4k.api.voice.enums.PunctuationType
import com.vapi4k.api.voice.enums.VoiceProviderType
import com.vapi4k.dsl.voice.ElevenLabsVoiceProperties
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ElevenLabsVoiceDto(
  override var inputPreprocessingEnabled: Boolean? = null,
  override var inputReformattingEnabled: Boolean? = null,
  override var inputMinCharacters: Int = -1,
  override val inputPunctuationBoundaries: MutableSet<PunctuationType> = mutableSetOf(),
  override var fillerInjectionEnabled: Boolean? = null,
  var voiceId: String = "",
  @Transient
  override var voiceIdType: ElevenLabsVoiceIdType = ElevenLabsVoiceIdType.UNSPECIFIED,
  @Transient
  override var customVoiceId: String = "",
  var model: String = "",
  @Transient
  override var modelType: ElevenLabsVoiceModelType = ElevenLabsVoiceModelType.UNSPECIFIED,
  @Transient
  override var customModel: String = "",
  override var stability: Double = -1.0,
  override var similarityBoost: Double = -1.0,
  override var style: Double = -1.0,
  override var useSpeakerBoost: Boolean? = null,
  override var optimizeStreaming: Double = -1.0,
  override var enableSsmlParsing: Boolean? = null,
) : ElevenLabsVoiceProperties,
  CommonVoiceDto {
  @EncodeDefault
  val provider: VoiceProviderType = VoiceProviderType.ELEVEN_LABS

  fun assignEnumOverrides() {
    voiceId = customVoiceId.ifEmpty { voiceIdType.desc }
    model = customModel.ifEmpty { modelType.desc }
  }

  override fun verifyValues() {
    if (voiceIdType.isNotSpecified() && customVoiceId.isEmpty())
      error("elevenLabsVoice{} requires a voiceIdType or customVoiceId value")
    if (voiceIdType.isSpecified() && customVoiceId.isNotEmpty())
      error("elevenLabsVoice{} cannot have both voiceIdType and customVoiceId values")
    if (modelType.isNotSpecified() && customModel.isEmpty())
      error("elevenLabsVoice{} requires a modelType or customModel value")
    if (modelType.isSpecified() && customModel.isNotEmpty())
      error("elevenLabsVoice{} cannot have both modelType and customModel values")
  }
}
