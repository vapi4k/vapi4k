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

import com.vapi4k.api.destination.AssistantDestination
import com.vapi4k.api.destination.NumberDestination
import com.vapi4k.api.destination.SipDestination
import com.vapi4k.api.destination.StepDestination
import com.vapi4k.api.tools.TransferDestinationResponse
import com.vapi4k.dsl.destination.AssistantDestinationImpl
import com.vapi4k.dsl.destination.NumberDestinationImpl
import com.vapi4k.dsl.destination.SipDestinationImpl
import com.vapi4k.dsl.destination.StepDestinationImpl
import com.vapi4k.dtos.api.destination.AssistantDestinationDto
import com.vapi4k.dtos.api.destination.NumberDestinationDto
import com.vapi4k.dtos.api.destination.SipDestinationDto
import com.vapi4k.dtos.api.destination.StepDestinationDto
import com.vapi4k.dtos.tools.TransferMessageResponseDto
import com.vapi4k.utils.DuplicateInvokeChecker

class TransferDestinationImpl internal constructor(
  private val callerName: String,
  private val dto: TransferMessageResponseDto,
) : TransferDestinationResponse {
  private val duplicateChecker = DuplicateInvokeChecker()

  override fun assistantDestination(block: AssistantDestination.() -> Unit) {
    duplicateChecker.check("assistantDestination{} already called in $callerName{}")
    val assistantDto = AssistantDestinationDto().also { dto.messageResponse.destination = it }
    AssistantDestinationImpl(assistantDto).apply(block).checkForRequiredFields()
  }

  override fun numberDestination(block: NumberDestination.() -> Unit) {
    duplicateChecker.check("numberDestination{} already called in $callerName{}")
    val numDto = NumberDestinationDto().also { dto.messageResponse.destination = it }
    NumberDestinationImpl(numDto).apply(block).checkForRequiredFields()
  }

  override fun sipDestination(block: SipDestination.() -> Unit) {
    duplicateChecker.check("sipDestination{} already called in $callerName{}")
    val sipDto = SipDestinationDto().also { dto.messageResponse.destination = it }
    SipDestinationImpl(sipDto).apply(block).checkForRequiredFields()
  }

  override fun stepDestination(block: StepDestination.() -> Unit) {
    duplicateChecker.check("stepDestination{} already called in $callerName{}")
    val stepDto = StepDestinationDto().also { dto.messageResponse.destination = it }
    StepDestinationImpl(stepDto).apply(block).checkForRequiredFields()
  }
}
