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

package com.vapi4k.dtos.transcriber

import com.vapi4k.api.transcriber.enums.DeepgramLanguageType
import com.vapi4k.api.transcriber.enums.DeepgramModelType
import com.vapi4k.api.transcriber.enums.TranscriberType
import com.vapi4k.dsl.transcriber.DeepgramTranscriberProperties
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class DeepgramTranscriberDto(
  override var model: String = "",
  override var language: String = "",
  @Transient
  override var transcriberModel: DeepgramModelType = DeepgramModelType.UNSPECIFIED,
  @Transient
  override var transcriberLanguage: DeepgramLanguageType = DeepgramLanguageType.UNSPECIFIED,
  override var smartFormat: Boolean? = null,
  override val keywords: MutableSet<String> = mutableSetOf(),
) : AbstractTranscriberDto(),
  DeepgramTranscriberProperties,
  CommonTranscriberDto {
  @EncodeDefault
  override val provider: TranscriberType = TranscriberType.DEEPGRAM

  fun assignEnumOverrides() {
    model = customModel.ifEmpty { transcriberModel.desc }
    language = customLanguage.ifEmpty { transcriberLanguage.desc }
  }

  fun verifyValues() {
    if (transcriberModel.isSpecified() && customModel.isNotEmpty())
      error("deepgramTranscriber{} cannot have both transcriberModel and customModel values")

    if (transcriberModel.isNotSpecified() && customModel.isEmpty())
      error("deepgramTranscriber{} requires transcriberModel or customModel value")

    if (transcriberLanguage.isSpecified() && customLanguage.isNotEmpty())
      error("deepgramTranscriber{} cannot have both transcriberLanguage and customLanguage values")

    if (transcriberLanguage.isNotSpecified() && customLanguage.isEmpty())
      error("deepgramTranscriber{} requires a transcriberLanguage or customLanguagevalue")
  }
}
