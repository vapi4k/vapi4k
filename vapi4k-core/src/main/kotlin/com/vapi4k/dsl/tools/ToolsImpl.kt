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

import com.vapi4k.api.tools.DtmfTool
import com.vapi4k.api.tools.EndCallTool
import com.vapi4k.api.tools.ExternalTool
import com.vapi4k.api.tools.GhlTool
import com.vapi4k.api.tools.MakeTool
import com.vapi4k.api.tools.ManualTool
import com.vapi4k.api.tools.Tools
import com.vapi4k.api.tools.TransferTool
import com.vapi4k.api.tools.VoiceMailTool
import com.vapi4k.common.FunctionName.Companion.toFunctionName
import com.vapi4k.dsl.functions.FunctionUtils.populateFunctionDto
import com.vapi4k.dsl.functions.FunctionUtils.verifyIsToolCall
import com.vapi4k.dsl.functions.FunctionUtils.verifyIsValidReturnType
import com.vapi4k.dsl.functions.FunctionUtils.verifyObjectHasOnlyOneToolCall
import com.vapi4k.dsl.functions.ToolCallInfo.Companion.appendAssistantId
import com.vapi4k.dsl.model.AbstractModelImpl
import com.vapi4k.dtos.tools.ToolDto
import com.vapi4k.utils.ReflectionUtils.isUnitReturnType
import com.vapi4k.utils.ReflectionUtils.toolCallFunction
import kotlin.reflect.KFunction

class ToolsImpl internal constructor(
  internal val model: AbstractModelImpl,
) : Tools {
  override fun serviceTool(
    obj: Any,
    vararg functions: KFunction<*>,
    block: BaseTool.() -> Unit,
  ) = processServiceTool(obj, functions) {
    BaseToolImpl("serviceTool", it).apply(block)
  }

  override fun manualTool(block: ManualTool.() -> Unit) {
    val toolDto = ToolDto(ToolType.FUNCTION).also { model.toolDtos += it }
    val manualToolImpl = ManualToolImpl("manualTool", toolDto).apply(block)

    // Append the assistantId to the tool name to avoid cache collisions
    toolDto.functionDto.name = toolDto.functionDto.name.appendAssistantId(model.assistantId)

    val funcName = toolDto.functionDto.name.toFunctionName()
    if (funcName.value.isBlank()) error("manualTool{} name is required")
    if (!manualToolImpl.isToolCallRequestInitialized()) error("manualTool{} must have onInvoke{} declared")
    if (model.declaredManualTools.contains(funcName)) error("Duplicate manual tool name declared: $funcName")
    model.declaredManualTools += funcName

    model.application.addManualToolToCache(funcName, manualToolImpl)
  }

  override fun externalTool(block: ExternalTool.() -> Unit) {
    val toolDto = ToolDto(ToolType.FUNCTION).also { model.toolDtos += it }
    ExternalToolImpl("externalTool", toolDto).apply(block).checkIfServerCalled()
    if (toolDto.functionDto.name.isBlank()) error("externalTool{} parameter name is required")
  }

  override fun transferTool(block: TransferTool.() -> Unit) {
    val toolDto = ToolDto(ToolType.TRANSFER_CALL).also { model.toolDtos += it }
    TransferToolImpl("transferTool", toolDto).apply(block)
  }

  override fun dtmfTool(block: DtmfTool.() -> Unit) {
    val toolDto = ToolDto(ToolType.DTMF).also { model.toolDtos += it }
    val impl = DtmfToolImpl("dtmfTool", toolDto).apply(block)

    // Append the assistantId to the tool name to avoid cache collisions
    toolDto.functionDto.name = toolDto.functionDto.name.appendAssistantId(model.assistantId)

    val funcName = toolDto.functionDto.name.toFunctionName()
    if (funcName.value.isBlank()) error("dtmfTool{} name is required")
    if (!impl.isToolCallRequestInitialized()) error("dtmfTool{} must have onInvoke{} declared")
    if (model.declaredManualTools.contains(funcName)) error("Duplicate manual tool name declared: $funcName")
    model.declaredManualTools += funcName
    model.application.addManualToolToCache(funcName, impl)
  }

  override fun endCallTool(block: EndCallTool.() -> Unit) {
    val toolDto = ToolDto(ToolType.END_CALL).also { model.toolDtos += it }
    val impl = EndCallToolImpl("endCallTool", toolDto).apply(block)

    // Append the assistantId to the tool name to avoid cache collisions
    toolDto.functionDto.name = toolDto.functionDto.name.appendAssistantId(model.assistantId)

    val funcName = toolDto.functionDto.name.toFunctionName()
    if (funcName.value.isBlank()) error("endCallTool{} name is required")
    if (!impl.isToolCallRequestInitialized()) error("endCallTool{} must have onInvoke{} declared")
    if (model.declaredManualTools.contains(funcName)) error("Duplicate manual tool name declared: $funcName")
    model.declaredManualTools += funcName
    model.application.addManualToolToCache(funcName, impl)
  }

  override fun voiceMailTool(block: VoiceMailTool.() -> Unit) {
    val toolDto = ToolDto(ToolType.VOICEMAIL).also { model.toolDtos += it }
    val impl = VoiceMailToolImpl("voiceMailTool", toolDto).apply(block)

    // Append the assistantId to the tool name to avoid cache collisions
    toolDto.functionDto.name = toolDto.functionDto.name.appendAssistantId(model.assistantId)

    val funcName = toolDto.functionDto.name.toFunctionName()
    if (funcName.value.isBlank()) error("voiceMailTool{} name is required")
    if (!impl.isToolCallRequestInitialized()) error("voiceMailTool{} must have onInvoke{} declared")
    if (model.declaredManualTools.contains(funcName)) error("Duplicate manual tool name declared: $funcName")
    model.declaredManualTools += funcName
    model.application.addManualToolToCache(funcName, impl)
  }

  override fun ghlTool(block: GhlTool.() -> Unit) {
    val toolDto = ToolDto(ToolType.GHL).also { model.toolDtos += it }
    ToolWithMetaDataImpl("ghlTool", toolDto).apply(block).checkIfServerCalled()
  }

  override fun makeTool(block: MakeTool.() -> Unit) {
    val toolDto = ToolDto(ToolType.MAKE).also { model.toolDtos += it }
    ToolWithMetaDataImpl("makeTool", toolDto).apply(block).checkIfServerCalled()
  }

  private fun processServiceTool(
    obj: Any,
    functionRefs: Array<out KFunction<*>>,
    block: (ToolDto) -> Unit,
  ) {
    if (functionRefs.isEmpty()) {
      verifyObjectHasOnlyOneToolCall(obj)
      val function = obj.toolCallFunction
      verifyIsValidReturnType(true, function)
      addTool(obj, function) { block(it) }
    } else {
      functionRefs.forEach { function ->
        verifyIsToolCall(true, function)
        verifyIsValidReturnType(true, function)
        addTool(obj, function) { block(it) }
      }
    }
  }

  private fun addTool(
    obj: Any,
    function: KFunction<*>,
    implInitBlock: (ToolDto) -> Unit,
  ) {
    model.toolDtos += ToolDto().also { toolDto ->
      populateFunctionDto(model, obj, function, toolDto.functionDto)
      model.application.addServiceToolToCache(model, obj, function)

      with(toolDto) {
        type = ToolType.FUNCTION
        async = function.isUnitReturnType
      }

      // Apply block to tool
      implInitBlock(toolDto)
    }.also { toolDto ->
      if (model.toolDtos.any { toolDto.functionDto.name == it.functionDto.name }) {
        error("Duplicate tool name declared: ${toolDto.functionDto.name}")
      }
    }
  }
}
