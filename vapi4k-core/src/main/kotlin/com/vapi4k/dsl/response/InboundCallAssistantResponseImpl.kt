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

package com.vapi4k.dsl.response

import com.vapi4k.api.destination.NumberDestination
import com.vapi4k.api.destination.SipDestination
import com.vapi4k.api.response.InboundCallAssistantResponse
import com.vapi4k.dsl.destination.NumberDestinationImpl
import com.vapi4k.dsl.destination.SipDestinationImpl
import com.vapi4k.dtos.api.destination.NumberDestinationDto
import com.vapi4k.dtos.api.destination.SipDestinationDto
import com.vapi4k.server.RequestContextImpl

class InboundCallAssistantResponseImpl internal constructor(
  requestContext: RequestContextImpl,
) : AbstractAssistantResponseImpl(requestContext),
  InboundCallAssistantResponse {
  override var error: String
    get() = messageResponse.error
    set(value) {
      duplicateChecker.check("error already assigned")
      messageResponse.error = value
    }

  override fun numberDestination(block: NumberDestination.() -> Unit): NumberDestination {
    duplicateChecker.check("numberDestination{} already called")
    val numDto = NumberDestinationDto().also { messageResponse.destination = it }
    return NumberDestinationImpl(numDto).apply(block).apply { checkForRequiredFields() }
  }

  override fun sipDestination(block: SipDestination.() -> Unit): SipDestination {
    duplicateChecker.check("sipDestination{} already called")
    val sipDto = SipDestinationDto().also { messageResponse.destination = it }
    return SipDestinationImpl(sipDto).apply(block).apply { checkForRequiredFields() }
  }
}
