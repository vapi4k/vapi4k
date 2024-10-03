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

package com.vapi4k.dsl.tools

import com.vapi4k.api.tools.ExternalTool
import com.vapi4k.api.tools.Parameters
import com.vapi4k.dtos.tools.ToolDto

open class ExternalToolImpl internal constructor(
  callerName: String,
  toolDto: ToolDto,
) : ToolWithServerImpl(callerName, toolDto),
  ExternalTool {
  override var name
    get() = toolDto.functionDto.name
    set(value) = run { toolDto.functionDto.name = value }

  override var description
    get() = toolDto.functionDto.description
    set(value) = run { toolDto.functionDto.description = value }

  override var async
    get() = toolDto.async ?: true
    set(value) = run { toolDto.async = value }

  override fun parameters(block: Parameters.() -> Unit) {
    ParametersImpl(callerName, toolDto).apply(block)
  }
}
