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

import com.vapi4k.api.voice.NeetsVoiceIdType
import com.vapi4k.api.voice.PunctuationType
import com.vapi4k.api.voice.VoiceProviderType
import com.vapi4k.dsl.voice.NeetsVoiceProperties
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class NeetsVoiceDto(
  override var inputPreprocessingEnabled: Boolean? = null,
  override var inputReformattingEnabled: Boolean? = null,
  override var inputMinCharacters: Int = -1,
  override val inputPunctuationBoundaries: MutableSet<PunctuationType> = mutableSetOf(),
  override var fillerInjectionEnabled: Boolean? = null,
  var voiceId: String = "",
  @Transient
  override var voiceIdType: NeetsVoiceIdType = NeetsVoiceIdType.UNSPECIFIED,
  @Transient
  override var customVoiceId: String = "",
) : NeetsVoiceProperties,
  CommonVoiceDto {
  @EncodeDefault
  val provider: VoiceProviderType = VoiceProviderType.NEETS

  fun assignEnumOverrides() {
    voiceId = customVoiceId.ifEmpty { voiceIdType.desc }
  }

  override fun verifyValues() {
    if (voiceIdType.isNotSpecified() && customVoiceId.isEmpty())
      error("neetsVoice{} requires a voiceIdType or customVoiceId value")
    if (voiceIdType.isSpecified() && customVoiceId.isNotEmpty())
      error("neetsVoice{} cannot have both voiceIdType and customVoiceId values")
  }
}
