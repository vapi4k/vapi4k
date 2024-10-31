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

package com.vapi4k.dsl.toolservice

import com.vapi4k.api.tools.ToolMessageFailed
import com.vapi4k.api.toolservice.RequestFailedCondition
import com.vapi4k.api.toolservice.RequestFailedMessages
import com.vapi4k.common.DuplicateInvokeChecker
import com.vapi4k.dsl.assistant.ToolMessageFailedImpl
import com.vapi4k.dtos.tools.ToolMessageCondition
import com.vapi4k.dtos.tools.ToolMessageFailedDto

class RequestFailedMessagesImpl internal constructor() : RequestFailedMessages {
  private val duplicateChecker = DuplicateInvokeChecker()
  internal val messageList = mutableListOf<ToolMessageFailedImpl>()

  private val dtoConditions
    get() = messageList.map { it.dto }.filter { it.conditions.isNotEmpty() }.map { it.conditions }.toSet()

  override fun requestFailedMessage(block: ToolMessageFailed.() -> Unit): ToolMessageFailed {
    duplicateChecker.check("tool{} already has a request failed message")
    return ToolMessageFailedDto().let { dto ->
      ToolMessageFailedImpl(dto).apply(block).also { messageList += it }
    }
  }

  override fun condition(
    requiredCondition: ToolMessageCondition,
    vararg additionalConditions: ToolMessageCondition,
    block: RequestFailedCondition.() -> Unit,
  ) {
    val conditionSet = mutableSetOf(requiredCondition).apply { addAll(additionalConditions) }
    if (conditionSet in dtoConditions) {
      error("condition(${conditionSet.joinToString()}){} duplicates an existing condition{}")
    }
    RequestFailedConditionImpl(this, conditionSet).apply(block)
    if (conditionSet !in dtoConditions) {
      error("condition(${conditionSet.joinToString()}){} must have at least one message")
    }
  }
}
