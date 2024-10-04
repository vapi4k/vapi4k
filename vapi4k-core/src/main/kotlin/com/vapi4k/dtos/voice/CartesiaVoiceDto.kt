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

import com.vapi4k.api.voice.CartesiaVoiceLanguageType
import com.vapi4k.api.voice.CartesiaVoiceModelType
import com.vapi4k.api.voice.PunctuationType
import com.vapi4k.api.voice.VoiceProviderType
import com.vapi4k.dsl.voice.CartesiaVoiceProperties
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class CartesiaVoiceDto(
  override var inputPreprocessingEnabled: Boolean? = null,
  override var inputReformattingEnabled: Boolean? = null,
  override var inputMinCharacters: Int = -1,
  override val inputPunctuationBoundaries: MutableSet<PunctuationType> = mutableSetOf(),
  override var fillerInjectionEnabled: Boolean? = null,
  var model: String = "",
  @Transient
  override var modelType: CartesiaVoiceModelType = CartesiaVoiceModelType.UNSPECIFIED,
  @Transient
  override var customModel: String = "",
  var language: String = "",
  @Transient
  override var languageType: CartesiaVoiceLanguageType = CartesiaVoiceLanguageType.UNSPECIFIED,
  @Transient
  override var customLanguage: String = "",
  override var voiceId: String = "",
) : CartesiaVoiceProperties,
  CommonVoiceDto {
  @EncodeDefault
  val provider: VoiceProviderType = VoiceProviderType.CARTESIA

  fun assignEnumOverrides() {
    model = customModel.ifEmpty { modelType.desc }
    language = customLanguage.ifEmpty { languageType.desc }
  }

  override fun verifyValues() {
//    if (modelType.isNotSpecified() && customModel.isEmpty())
//      error("cartesiaVoice{} requires a modelType or customModel value")
    if (modelType.isSpecified() && customModel.isNotEmpty())
      error("cartesiaVoice{} cannot have both modelType and customModel values")
    if (languageType.isSpecified() && customLanguage.isNotEmpty())
      error("cartesiaVoice{} cannot have both languageType and customLanguage values")
  }
}
