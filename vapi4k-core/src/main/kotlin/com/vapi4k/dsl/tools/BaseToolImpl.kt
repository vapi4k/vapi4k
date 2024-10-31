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

import com.vapi4k.api.tools.ToolCondition
import com.vapi4k.api.tools.ToolMessageComplete
import com.vapi4k.api.tools.ToolMessageDelayed
import com.vapi4k.api.tools.ToolMessageFailed
import com.vapi4k.api.tools.ToolMessageStart
import com.vapi4k.common.DuplicateInvokeChecker
import com.vapi4k.common.Utils.capitalizeFirstChar
import com.vapi4k.dsl.assistant.ToolMessageCompleteImpl
import com.vapi4k.dsl.assistant.ToolMessageDelayedImpl
import com.vapi4k.dsl.assistant.ToolMessageFailedImpl
import com.vapi4k.dsl.assistant.ToolMessageStartImpl
import com.vapi4k.dtos.tools.ToolDto
import com.vapi4k.dtos.tools.ToolMessageCompleteDto
import com.vapi4k.dtos.tools.ToolMessageCondition
import com.vapi4k.dtos.tools.ToolMessageDelayedDto
import com.vapi4k.dtos.tools.ToolMessageFailedDto
import com.vapi4k.dtos.tools.ToolMessageStartDto

open class BaseToolImpl internal constructor(
  internal val callerName: String,
  internal val toolDto: ToolDto,
) : BaseTool {
  private val startChecker = DuplicateInvokeChecker()
  private val completeChecker = DuplicateInvokeChecker()
  private val failedChecker = DuplicateInvokeChecker()
  private val delayedChecker = DuplicateInvokeChecker()

  internal val messages get() = toolDto.messages
  internal val properties get() = toolDto.functionDto.parametersDto.properties

  internal val signature
    get() = properties
      .map { (k, v) -> "$k: ${v.type.capitalizeFirstChar()}" }
      .joinToString(", ")

  private val dtoConditions
    get() = messages.filter { it.conditions.isNotEmpty() }.map { it.conditions }.toSet()

  override fun requestStartMessage(block: ToolMessageStart.() -> Unit): ToolMessageStart {
    startChecker.check("${this.callerName}{} already has a request start message")
    return ToolMessageStartDto().let { dto ->
      toolDto.messages.add(dto)
      ToolMessageStartImpl(dto).apply(block)
    }
  }

  override fun requestCompleteMessage(block: ToolMessageComplete.() -> Unit): ToolMessageComplete {
    completeChecker.check("${this.callerName}{} already has a request complete message")
    return ToolMessageCompleteDto().let { dto ->
      toolDto.messages.add(dto)
      ToolMessageCompleteImpl(dto).apply(block)
    }
  }

  override fun requestFailedMessage(block: ToolMessageFailed.() -> Unit): ToolMessageFailed {
    failedChecker.check("${this.callerName}{} already has a request failed message")
    return ToolMessageFailedDto().let { dto ->
      toolDto.messages.add(dto)
      ToolMessageFailedImpl(dto).apply(block)
    }
  }

  override fun requestDelayedMessage(block: ToolMessageDelayed.() -> Unit): ToolMessageDelayed {
    delayedChecker.check("${this.callerName}{} already has a request delayed message")
    return ToolMessageDelayedDto().let { dto ->
      toolDto.messages.add(dto)
      ToolMessageDelayedImpl(dto).apply(block)
    }
  }

  override fun condition(
    requiredCondition: ToolMessageCondition,
    vararg additionalConditions: ToolMessageCondition,
    block: ToolCondition.() -> Unit,
  ) {
    val conditionSet = mutableSetOf(requiredCondition).apply { addAll(additionalConditions) }
    if (conditionSet in dtoConditions) {
      error("condition(${conditionSet.joinToString()}){} duplicates an existing condition{}")
    }
    ToolConditionImpl(this, conditionSet).apply(block)
    if (conditionSet !in dtoConditions) {
      error("condition(${conditionSet.joinToString()}){} must have at least one message")
    }
  }
}
