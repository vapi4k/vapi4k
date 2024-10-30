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

import com.vapi4k.api.response.DtmfToolResponse
import com.vapi4k.api.response.EndCallToolResponse
import com.vapi4k.api.response.VoiceMailToolResponse
import com.vapi4k.api.tools.DtmfTool
import com.vapi4k.api.tools.EndCallTool
import com.vapi4k.api.tools.Parameters
import com.vapi4k.api.tools.VoiceMailTool
import com.vapi4k.dtos.tools.ToolDto
import kotlinx.serialization.json.JsonElement

open class ToolWithParametersImpl internal constructor(
  callerName: String,
  toolDto: ToolDto,
) : ToolWithServerImpl(callerName, toolDto),
  ToolWithParameters {
  var name
    get() = toolDto.functionDto.name
    set(value) = run { toolDto.functionDto.name = value }

  var description
    get() = toolDto.functionDto.description
    set(value) = run { toolDto.functionDto.description = value }

  override var async
    get() = toolDto.async ?: true
    set(value) = run { toolDto.async = value }

  override fun parameters(block: Parameters.() -> Unit) {
    ParametersImpl(callerName, toolDto).apply(block)
  }
}

class DtmfToolImpl internal constructor(
  callerName: String,
  toolDto: ToolDto,
) : ToolWithParametersImpl(callerName, toolDto),
  DtmfTool {
  internal lateinit var toolCallRequest: (suspend DtmfToolResponse.(JsonElement) -> Unit)

  internal fun isToolCallRequestInitialized() = ::toolCallRequest.isInitialized

  override fun onInvoke(block: suspend DtmfToolResponse.(JsonElement) -> Unit) {
    if (!::toolCallRequest.isInitialized)
      toolCallRequest = block
    else
      error("onInvoke{} can be called only once per $callerName{}")
  }
}

class EndCallToolImpl internal constructor(
  callerName: String,
  toolDto: ToolDto,
) : ToolWithParametersImpl(callerName, toolDto),
  EndCallTool {
  internal lateinit var toolCallRequest: (suspend EndCallToolResponse.(JsonElement) -> Unit)

  internal fun isToolCallRequestInitialized() = ::toolCallRequest.isInitialized

  override fun onInvoke(block: suspend EndCallToolResponse.(JsonElement) -> Unit) {
    if (!::toolCallRequest.isInitialized)
      toolCallRequest = block
    else
      error("onInvoke{} can be called only once per $callerName{}")
  }
}

class VoiceMailToolImpl internal constructor(
  callerName: String,
  toolDto: ToolDto,
) : ToolWithParametersImpl(callerName, toolDto),
  VoiceMailTool {
  internal lateinit var toolCallRequest: (suspend VoiceMailToolResponse.(JsonElement) -> Unit)

  internal fun isToolCallRequestInitialized() = ::toolCallRequest.isInitialized

  override fun onInvoke(block: suspend VoiceMailToolResponse.(JsonElement) -> Unit) {
    if (!::toolCallRequest.isInitialized)
      toolCallRequest = block
    else
      error("onInvoke{} can be called only once per $callerName{}")
  }
}
