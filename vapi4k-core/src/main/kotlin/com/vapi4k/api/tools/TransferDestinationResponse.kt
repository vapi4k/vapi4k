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

package com.vapi4k.api.tools

import com.vapi4k.api.destination.AssistantDestination
import com.vapi4k.api.destination.DynamicDestination
import com.vapi4k.api.destination.NumberDestination
import com.vapi4k.api.destination.SipDestination
import com.vapi4k.api.destination.SquadDestination
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

/**
This is the destination you'd like the call to be transferred to.
 */
@Vapi4KDslMarker
interface TransferDestinationResponse {
  /**
  Transfers the call to an assistant.
   */
  fun assistantDestination(block: AssistantDestination.() -> Unit)

  /**
  Transfers the call to a dynamically determined destination via a webhook.
   */
  fun dynamicDestination(block: DynamicDestination.() -> Unit)

  /**
  Transfers the call to a number.
   */
  fun numberDestination(block: NumberDestination.() -> Unit)

  /**
  Transfers the call to a sip.
   */
  fun sipDestination(block: SipDestination.() -> Unit)

  /**
  Transfers the call to a multi-agent squad.
   */
  fun squadDestination(block: SquadDestination.() -> Unit)
}
