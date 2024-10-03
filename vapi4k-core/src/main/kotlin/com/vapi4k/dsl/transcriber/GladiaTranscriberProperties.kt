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

package com.vapi4k.dsl.transcriber

import com.vapi4k.api.transcriber.enums.GladiaLanguageType
import com.vapi4k.api.transcriber.enums.GladiaModelType

interface GladiaTranscriberProperties {
  /**
  This is the Gladia model that will be used. Default is 'fast'
   */
  var transcriberModel: GladiaModelType

  /**
  Defines the language to use for the transcription. Required when languageBehaviour is 'manual'.
   */
  var transcriberLanguage: GladiaLanguageType

  /**
  This enables specifying a model that doesn't already exist as an GladiaModelType enum.
   */
  var customModel: String

  /**
  This enables specifying a language that doesn't already exist as an GladiaLanguageType enum.
   */
  var customLanguage: String

  /**
  If true, audio will be pre-processed to improve accuracy but latency will increase. Default value is false.
   */
  var audioEnhancer: Boolean?

  /**
  Defines how the transcription model detects the audio language. Default value is 'automatic single language'.
   */
  var languageBehavior: String

  /**
  If prosody is true, you will get a transcription that can contain prosodies i.e. (laugh) (giggles) (malefic laugh) (toss) (music)… Default value is false.
   */
  var prosody: Boolean?

  /**
  Provides a custom vocabulary to the model to improve accuracy of transcribing context specific words, technical terms,
  names, etc. If empty, this argument is ignored. ⚠️ Warning ⚠️: Please be aware that the transcription_hint field has
  a character limit of 600. If you provide a transcription_hint longer than 600 characters, it will be automatically
  truncated to meet this limit.
   */
  var transcriptionHint: String
}
