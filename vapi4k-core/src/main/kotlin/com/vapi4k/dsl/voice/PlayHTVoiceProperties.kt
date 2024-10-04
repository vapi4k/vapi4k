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

package com.vapi4k.dsl.voice

import com.vapi4k.api.voice.PlayHTVoiceEmotionType
import com.vapi4k.api.voice.PlayHTVoiceIdType

interface PlayHTVoiceProperties : CommonVoiceProperties {
  /**
  This is the provider-specific ID that will be used.
   */
  var voiceIdType: PlayHTVoiceIdType

  /**
  This enables specifying a voice that doesn't already exist as an PlayHTVoiceIdType enum.
   */
  var customVoiceId: String

  /**
  An emotion to be applied to the speech.
   */
  var emotion: PlayHTVoiceEmotionType

  /**
  This is the speed multiplier that will be used.
   */
  var speed: Double

  /**
  A number between 1 and 30. Use lower numbers to to reduce how strong your chosen emotion will be. Higher numbers will
  create a very emotional performance.
   */
  var styleGuidance: Double

  /**
  A floating point number between 0, exclusive, and 2, inclusive. If equal to null or not provided, the model's default
  temperature will be used. The temperature parameter controls variance. Lower temperatures result in more predictable
  results, higher temperatures allow each run to vary more, so the voice may sound less like the baseline voice.
   */
  var temperature: Double

  /**
  A number between 1 and 2. This number influences how closely the generated speech adheres to the input text. Use lower
  values to create more fluid speech, but with a higher chance of deviating from the input text. Higher numbers will
  make the generated speech more accurate to the input text, ensuring that the words spoken align closely with the
  provided text.
   */
  var textGuidance: Double

  /**
  A number between 1 and 6. Use lower numbers to reduce how unique your chosen voice will be compared to other voices.
   */
  var voiceGuidance: Double
}
