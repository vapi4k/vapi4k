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

package com.vapi4k.dtos.api.destination

import com.vapi4k.api.destination.AssistantDestination
import com.vapi4k.api.destination.enums.AssistantTransferMode
import com.vapi4k.api.destination.enums.DestinationType
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class AssistantDestinationDto(
  override var message: String = "",
  override var description: String = "",
  override var assistantName: String = "",
  override var transferMode: AssistantTransferMode = AssistantTransferMode.UNSPECIFIED,
) : CommonDestinationDto,
  AssistantDestination {
  @EncodeDefault
  val type: DestinationType = DestinationType.ASSISTANT
}
