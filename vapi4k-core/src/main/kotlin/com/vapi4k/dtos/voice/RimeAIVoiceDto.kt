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

import com.vapi4k.api.voice.enums.PunctuationType
import com.vapi4k.api.voice.enums.RimeAIVoiceIdType
import com.vapi4k.api.voice.enums.RimeAIVoiceModelType
import com.vapi4k.api.voice.enums.VoiceProviderType
import com.vapi4k.dsl.voice.RimeAIVoiceProperties
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class RimeAIVoiceDto(
  override var inputPreprocessingEnabled: Boolean? = null,
  override var inputReformattingEnabled: Boolean? = null,
  override var inputMinCharacters: Int = -1,
  override val inputPunctuationBoundaries: MutableSet<PunctuationType> = mutableSetOf(),
  override var fillerInjectionEnabled: Boolean? = null,
  var voiceId: String = "",
  @Transient
  override var voiceIdType: RimeAIVoiceIdType = RimeAIVoiceIdType.UNSPECIFIED,
  @Transient
  override var customVoiceId: String = "",
  var model: String = "",
  @Transient
  override var modelType: RimeAIVoiceModelType = RimeAIVoiceModelType.UNSPECIFIED,
  @Transient
  override var customModel: String = "",
  override var speed: Double = -1.0,
) : RimeAIVoiceProperties,
  CommonVoiceDto {
  @EncodeDefault
  val provider: VoiceProviderType = VoiceProviderType.RIME_AI

  fun assignEnumOverrides() {
    voiceId = customVoiceId.ifEmpty { voiceIdType.desc }
    model = customModel.ifEmpty { modelType.desc }
  }

  override fun verifyValues() {
    if (voiceIdType.isNotSpecified() && customVoiceId.isEmpty())
      error("rimeAIVoice{} requires a voiceIdType or customVoiceId value")
    if (voiceIdType.isSpecified() && customVoiceId.isNotEmpty())
      error("rimeAIVoice{} cannot have both voiceIdType and customVoiceId values")
    if (modelType.isSpecified() && customModel.isNotEmpty())
      error("rimeAIVoice{} cannot have both modelType and customModel values")
    // TODO: Confirm values for speed limits
//    if (speed != -1.0 && (speed < 0.1 || speed > 2))
//      error("rimeAIVoice{} speed must be between 0.1 and 2")
  }
}
