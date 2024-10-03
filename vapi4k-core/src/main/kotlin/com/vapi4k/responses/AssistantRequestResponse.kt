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

package com.vapi4k.responses

import com.vapi4k.dsl.assistant.AssistantIdProperties
import com.vapi4k.dsl.squad.SquadIdSource
import com.vapi4k.dtos.api.destination.CommonDestinationDto
import com.vapi4k.dtos.assistant.AssistantDto
import com.vapi4k.dtos.assistant.AssistantOverridesDto
import com.vapi4k.dtos.buttons.ButtonConfigDto
import com.vapi4k.dtos.squad.SquadDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssistantRequestResponse(
  var destination: CommonDestinationDto? = null,
  override var assistantId: String = "",
  @SerialName("assistant")
  override val assistantDto: AssistantDto = AssistantDto(),
  @SerialName("assistantOverrides")
  override val assistantOverridesDto: AssistantOverridesDto = AssistantOverridesDto(),
  override var squadId: String = "",
  @SerialName("squad")
  val squadDto: SquadDto = SquadDto(),
  @SerialName("buttonConfig")
  val buttonConfigDto: ButtonConfigDto = ButtonConfigDto(),
  var error: String = "",
) : AssistantIdProperties,
  SquadIdSource
