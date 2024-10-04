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

package com.vapi4k.api.transcriber

import com.vapi4k.common.Constants.UNSPECIFIED_DEFAULT
import kotlinx.serialization.Serializable

// See: https://developers.deepgram.com/docs/models-languages-overview
@Serializable
enum class DeepgramModelType(
  val desc: String,
) {
  NOVA_2("nova-2"),
  NOVA_2_GENERAL("nova-2-general"),
  NOVA_2_MEETING("nova-2-meeting"),
  NOVA_2_PHONECALL("nova-2-phonecall"),
  NOVA_2_FINANCE("nova-2-finance"),
  NOVA_2_CONVERSATIONAL_AI("nova-2-conversationalai"),
  NOVA_2_VOICEMAIL("nova-2-voicemail"),
  NOVA_2_VIDEO("nova-2-video"),
  NOVA_2_MEDICAL("nova-2-medical"),
  NOVA_2_DRIVETHRU("nova-2-drivethru"),
  NOVA_2_AUTOMOTIVE("nova-2-automotive"),

  NOVA("nova"),
  NOVA_GENERAL("nova-general"),
  NOVA_PHONECALL("nova-phonecall"),
  NOVA_MEDICAL("nova-medical"),

  ENHANCED("enhanced"),
  ENHANCED_GENERAL("enhanced-general"),
  ENHANCED_MEETING("enhanced-meeting"),
  ENHANCED_PHONECALL("enhanced-phonecall"),
  ENHANCED_FINANCE("enhanced-finance"),

  BASE("base"),
  BASE_GENERAL("base-general"),
  BASE_MEETING("base-meeting"),
  BASE_PHONECALL("base-phonecall"),
  BASE_FINANCE("base-finance"),
  BASE_CONVERSATIONAL_AI("base-conversationalai"),
  BASE_VOICEMAIL("base-voicemail"),
  BASE_VIDEO("base-video"),

  UNSPECIFIED(UNSPECIFIED_DEFAULT),
  ;

  fun isSpecified() = this != UNSPECIFIED

  fun isNotSpecified() = this == UNSPECIFIED
}
