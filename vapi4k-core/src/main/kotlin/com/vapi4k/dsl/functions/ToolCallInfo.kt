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

package com.vapi4k.dsl.functions

import com.vapi4k.common.AssistantId
import com.vapi4k.common.FunctionName.Companion.toFunctionName
import com.vapi4k.dsl.functions.FunctionUtils.llmType
import com.vapi4k.utils.ReflectionUtils.asKClass
import com.vapi4k.utils.ReflectionUtils.toolCallAnnotation
import kotlin.reflect.KFunction

class ToolCallInfo(
  private val assistantId: AssistantId,
  private val function: KFunction<*>,
) {
  private val toolCall get() = function.toolCallAnnotation!!
  private val toolHasName get() = toolCall.name.isNotEmpty()
  private val toolHasDescription get() = toolCall.description.isNotEmpty()
  private val name get() = if (toolHasName) toolCall.name else function.name

  val llmName get() = name.appendAssistantId(assistantId).toFunctionName()

  val llmDescription
    get() =
      when {
        toolHasDescription -> toolCall.description
        toolHasName -> toolCall.name
        else -> function.name
      }

  val llmReturnType get() = function.returnType.asKClass().llmType

  companion object {
    internal const val ID_SEPARATOR = "_"

    internal fun String.appendAssistantId(assistantId: AssistantId): String = "$this$ID_SEPARATOR$assistantId"
  }
}
