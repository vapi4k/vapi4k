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

package com.vapi4k.dtos.tools

import com.vapi4k.dsl.tools.ToolType
import com.vapi4k.dsl.tools.ToolWithMetaDataProperties
import com.vapi4k.dtos.api.destination.CommonDestinationDto
import com.vapi4k.dtos.functions.FunctionDto
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ToolDto(
  @EncodeDefault
  var type: ToolType = ToolType.UNSPECIFIED,
  var async: Boolean? = null,
  @SerialName("function")
  val functionDto: FunctionDto = FunctionDto(),
  val messages: MutableList<CommonToolMessageDto> = mutableListOf(),
  val server: ServerDto = ServerDto(),
  val destinations: MutableList<CommonDestinationDto> = mutableListOf(),
  override val metadata: MutableMap<String, String> = mutableMapOf(),
) : ToolWithMetaDataProperties
