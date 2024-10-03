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

package com.vapi4k.dtos.squad

import com.vapi4k.dtos.api.destination.AssistantDestinationDto
import com.vapi4k.dtos.assistant.AssistantDto
import com.vapi4k.dtos.assistant.AssistantOverridesDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberDto(
  var assistantId: String = "",
  @SerialName("assistant")
  val assistantDto: AssistantDto = AssistantDto(),
  @SerialName("assistantOverrides")
  val assistantOverridesDto: AssistantOverridesDto = AssistantOverridesDto(),
  @SerialName("assistantDestinations")
  val assistantDestinationsDto: MutableList<AssistantDestinationDto> = mutableListOf(),
)
