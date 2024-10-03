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

package com.vapi4k.dsl.voice

import com.vapi4k.api.voice.enums.CartesiaVoiceLanguageType
import com.vapi4k.api.voice.enums.CartesiaVoiceModelType

interface CartesiaVoiceProperties : CommonVoiceProperties {
  /**
  This is the model that will be used. This is optional and will default to the correct model for the voiceId.
   */
  var modelType: CartesiaVoiceModelType

  /**
  This is the language that will be used. This is optional and will default to the correct language for the voiceId.
   */
  var languageType: CartesiaVoiceLanguageType

  /**
  This enables specifying a model that doesn't already exist as an CartesiaVoiceModelType enum.
   */
  var customModel: String

  /**
  This enables specifying a language that doesn't already exist as an CartesiaVoiceLanguageType enum.
   */
  var customLanguage: String

  /**
  This is the provider-specific ID that will be used.
   */
  var voiceId: String
}
