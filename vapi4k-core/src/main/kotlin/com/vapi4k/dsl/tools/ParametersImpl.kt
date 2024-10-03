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

package com.vapi4k.dsl.tools

import com.vapi4k.api.tools.Parameter
import com.vapi4k.api.tools.Parameters
import com.vapi4k.dsl.functions.FunctionUtils
import com.vapi4k.dsl.functions.FunctionUtils.llmType
import com.vapi4k.dtos.functions.FunctionPropertyDescDto
import com.vapi4k.dtos.tools.ToolDto

class ParametersImpl internal constructor(
  private val callerName: String,
  private val toolDto: ToolDto,
) : Parameters {
  override fun parameter(block: Parameter.() -> Unit) {
    val p = ParameterImpl().apply(block)
    if (p.name.isBlank()) error("Parameter name must be assigned in externalTool{}")
    if (p.type !in FunctionUtils.allowedParamTypes)
      error(
        "$callerName{} parameter type must be one of these: String::class, Int::class, Double::class, Boolean::class",
      )

    with(toolDto.functionDto.parametersDto) {
      if (properties.containsKey(p.name)) error("externalTool{} parameter ${p.name} already exists")
      properties[p.name] = FunctionPropertyDescDto(p.type.llmType, p.description)
      if (p.required) this.required.add(p.name)
    }
  }
}
