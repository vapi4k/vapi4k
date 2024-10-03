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

import com.vapi4k.api.voice.enums.LMNTVoiceIdType

interface LMNTVoiceProperties : CommonVoiceProperties {
  /**
  This is the provider-specific ID that will be used.
   */
  var voiceIdType: LMNTVoiceIdType

  /**
  This enables specifying a voice that doesn't already exist as an LMNTVoiceIdType enum.
   */
  var customVoiceId: String

  /**
  This is the speed multiplier that will be used.
   */
  var speed: Double
}
