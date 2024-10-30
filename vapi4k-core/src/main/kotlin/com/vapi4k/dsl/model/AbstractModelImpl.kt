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

package com.vapi4k.dsl.model

import com.vapi4k.api.functions.Functions
import com.vapi4k.api.model.KnowledgeBase
import com.vapi4k.api.tools.Tools
import com.vapi4k.common.FunctionName
import com.vapi4k.common.Utils.trimLeadingSpaces
import com.vapi4k.dsl.functions.FunctionsImpl
import com.vapi4k.dsl.tools.ToolsImpl
import com.vapi4k.dtos.model.CommonModelDto
import com.vapi4k.dtos.model.KnowledgeBaseDto
import com.vapi4k.dtos.model.RoleMessageDto
import com.vapi4k.utils.DuplicateInvokeChecker
import kotlin.reflect.KProperty

internal abstract class AbstractModelImpl(
  override val modelUnion: ModelUnion,
  private val dto: CommonModelDto,
) : AbstractModelProperties {
  // This will track manual tools declared per model and prevent duplicates
  internal val declaredManualTools = mutableSetOf<FunctionName>()

  override val assistantId get() = modelUnion.assistantId
  override val messages get() = dto.messages
  override val toolDtos get() = dto.tools
  override val functionDtos get() = dto.functions

  var systemMessage by ModelMessageDelegate(MessageRoleType.SYSTEM)
  var assistantMessage by ModelMessageDelegate(MessageRoleType.ASSISTANT)
  var functionMessage by ModelMessageDelegate(MessageRoleType.FUNCTION)
  var toolMessage by ModelMessageDelegate(MessageRoleType.TOOL)
  var userMessage by ModelMessageDelegate(MessageRoleType.USER)

  private val duplicateChecker = DuplicateInvokeChecker()

  fun tools(block: Tools.() -> Unit): Tools = ToolsImpl(this).apply(block)

  fun functions(block: Functions.() -> Unit): Functions = FunctionsImpl(this).apply(block)

  fun knowledgeBase(block: KnowledgeBase.() -> Unit): KnowledgeBase {
    val caller = this::class.simpleName.orEmpty().removeSuffix("Impl").replaceFirstChar { it.lowercaseChar() }
    duplicateChecker.check("$caller{} contains multiple calls to knowledgeBase{}")
    val kbDto = KnowledgeBaseDto().also { dto.knowledgeBaseDto = it }
    return KnowledgeBaseImpl(modelUnion.requestContext, kbDto)
      .apply(block)
      .apply {
        if (kbDto.fileIds.isEmpty())
          error("knowledgeBase{} must have at least one file")
      }
  }

  override fun message(
    role: MessageRoleType,
    content: String,
  ) {
    // Remove any existing messages with the same role
    messages.removeAll { it.role == role.desc }
    // Use trimLeadingSpaces() instead of trimIndent() because trimIndent() doesn't work with += operator
    messages += RoleMessageDto(role.desc, content.trimLeadingSpaces())
  }
}

internal class ModelMessageDelegate(
  private val messageRoleType: MessageRoleType,
) {
  operator fun getValue(
    model: AbstractModelProperties,
    property: KProperty<*>,
  ): String {
    val msgs = model.messages.filter { it.role == messageRoleType.desc }
    return if (msgs.isEmpty()) "" else (msgs.joinToString("") { it.content })
  }

  operator fun setValue(
    model: AbstractModelProperties,
    property: KProperty<*>,
    newVal: String,
  ) = model.message(messageRoleType, newVal)
}
