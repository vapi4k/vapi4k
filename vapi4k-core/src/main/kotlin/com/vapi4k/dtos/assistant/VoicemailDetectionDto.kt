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

package com.vapi4k.dtos.assistant

import com.vapi4k.api.assistant.enums.VoicemailDetectionType
import com.vapi4k.dsl.assistant.VoicemailDetectionProperties
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class VoicemailDetectionDto(
  @EncodeDefault
  var provider: String = "twilio",
  override var enabled: Boolean? = null,
  override var machineDetectionTimeout: Int = -1,
  override var machineDetectionSpeechThreshold: Int = -1,
  override var machineDetectionSpeechEndThreshold: Int = -1,
  override var machineDetectionSilenceTimeout: Int = -1,
  override val voicemailDetectionTypes: MutableSet<VoicemailDetectionType> = mutableSetOf(),
) : VoicemailDetectionProperties
