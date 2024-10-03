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

import com.vapi4k.api.voice.enums.ElevenLabsVoiceIdType
import com.vapi4k.api.voice.enums.ElevenLabsVoiceModelType

interface ElevenLabsVoiceProperties : CommonVoiceProperties {
  /**
  This is the model that will be used. Defaults to 'eleven_turbo_v2' if not specified.
   */
  var modelType: ElevenLabsVoiceModelType

  /**
  This enables specifying a model that doesn't already exist as an ElevenLabsVoiceModelType enum.
   */
  var customModel: String

  /**
  This is the provider-specific ID that will be used. Ensure the Voice is present in your 11Labs Voice Library.
   */
  var voiceIdType: ElevenLabsVoiceIdType

  /**
  This enables specifying a voice that doesn't already exist as an ElevenLabsVoiceIdType enum.
   */
  var customVoiceId: String

  /**
  Defines the use of https://elevenlabs.io/docs/speech-synthesis/prompting#pronunciation. Disabled by default.
   */
  var enableSsmlParsing: Boolean?

  /**
  Defines the optimize streaming latency for voice settings. Defaults to 3.
   */
  var optimizeStreaming: Double

  /**
  Defines the similarity boost for voice settings.
   */
  var similarityBoost: Double

  /**
  Defines the similarity boost for voice settings.
   */
  var stability: Double

  /**
  Defines the style for voice settings.
   */
  var style: Double

  /**
  Defines the use speaker boost for voice settings.
   */
  var useSpeakerBoost: Boolean?
}
