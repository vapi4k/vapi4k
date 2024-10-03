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

package com.vapi4k.api.reponse

import com.vapi4k.api.assistant.Assistant
import com.vapi4k.api.assistant.AssistantId
import com.vapi4k.api.destination.NumberDestination
import com.vapi4k.api.destination.SipDestination
import com.vapi4k.api.squad.Squad
import com.vapi4k.api.squad.SquadId
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

@Vapi4KDslMarker
interface InboundCallAssistantResponse {
  /**
  <p>This is the error if the call shouldn't be accepted. This is spoken to the customer.
  <br>If this is sent, <code>assistantId</code>, <code>assistant</code>, <code>squadId</code>, <code>squad</code>, and <code>destination</code> are ignored.
  </p>
   */
  var error: String

  /**
  <p>This is the assistant that will be used for the call. To use an existing assistant, use <code>assistantId</code> instead.
  <br>If you're unsure why you're getting an invalid assistant, try logging your response and send the JSON blob to POST /assistant which will return the validation errors.
  </p>
   */
  fun assistant(block: Assistant.() -> Unit): Assistant

  /**
  This is the assistant that will be used for the call. To use a transient assistant, use `assistant` instead.
   */
  fun assistantId(block: AssistantId.() -> Unit): AssistantId

  /**
  This is a squad that will be used for the call. To use an existing squad, use `squadId` instead.
   */
  fun squad(block: Squad.() -> Unit): Squad

  /**
  This is the squad that will be used for the call. To use a transient squad, use `squad` instead.
   */
  fun squadId(block: SquadId.() -> Unit): SquadId

  /**
  <p>This is the destination to transfer the inbound call to. This will immediately transfer to a number without using any assistants.
  <br>If this is sent, <code>assistantId</code>, <code>assistant</code>, <code>squadId</code>, and <code>squad</code> are ignored.
  </p>
   */
  fun numberDestination(block: NumberDestination.() -> Unit): NumberDestination

  /**
  <p>This is the destination to transfer the inbound call to. This will immediately transfer to a SIP refer without using any assistants.
  <br>If this is sent, <code>assistantId</code>, <code>assistant</code>, <code>squadId</code>, and <code>squad</code> are ignored.
  </p>
   */
  fun sipDestination(block: SipDestination.() -> Unit): SipDestination
}
