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
import com.vapi4k.dsl.assistant.ToolMessageCompleteImpl
import com.vapi4k.dsl.assistant.ToolMessageDelayedImpl
import com.vapi4k.dsl.assistant.ToolMessageFailedImpl
import com.vapi4k.dsl.assistant.ToolMessageStartImpl
import com.vapi4k.dtos.tools.ToolMessageCompleteDto
import com.vapi4k.dtos.tools.ToolMessageCondition
import com.vapi4k.dtos.tools.ToolMessageDelayedDto
import com.vapi4k.dtos.tools.ToolMessageFailedDto
import com.vapi4k.dtos.tools.ToolMessageStartDto

class ToolConditionImpl internal constructor(
  internal val tool: BaseToolImpl,
  private val conditionSet: Set<ToolMessageCondition>,
) : ToolCondition {
  private val startChecker = DuplicateInvokeChecker()
  private val completeChecker = DuplicateInvokeChecker()
  private val failedChecker = DuplicateInvokeChecker()
  private val delayedChecker = DuplicateInvokeChecker()

  private val messages get() = tool.toolDto.messages

  override fun requestStartMessage(block: ToolMessageStart.() -> Unit): ToolMessageStart {
    startChecker.check("condition${conditionSet.joinToString()}{} already has a requestStartMessage{}")
    return ToolMessageStartDto().let { dto ->
      dto.conditions.addAll(conditionSet)
      messages.add(dto)
      ToolMessageStartImpl(dto).apply(block)
    }
  }

  override fun requestCompleteMessage(block: ToolMessageComplete.() -> Unit): ToolMessageComplete {
    completeChecker.check("condition${conditionSet.joinToString()}{} already has a requestCompleteMessage{}")
    return ToolMessageCompleteDto().let { dto ->
      dto.conditions.addAll(conditionSet)
      messages.add(dto)
      ToolMessageCompleteImpl(dto).apply(block)
    }
  }

  override fun requestFailedMessage(block: ToolMessageFailed.() -> Unit): ToolMessageFailed {
    failedChecker.check("condition${conditionSet.joinToString()}{} already has a requestFailedMessage{}")
    return ToolMessageFailedDto().let { dto ->
      dto.conditions.addAll(conditionSet)
      messages.add(dto)
      ToolMessageFailedImpl(dto).apply(block)
    }
  }

  override fun requestDelayedMessage(block: ToolMessageDelayed.() -> Unit): ToolMessageDelayed {
    delayedChecker.check("condition${conditionSet.joinToString()}{} already has a requestDelayedMessage{}")
    return ToolMessageDelayedDto().let { dto ->
      dto.conditions.addAll(conditionSet)
      messages.add(dto)
      ToolMessageDelayedImpl(dto).apply(block)
    }
  }
}
