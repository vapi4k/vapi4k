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

package com.vapi4k.api.squad

import com.vapi4k.api.assistant.Assistant
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

/**
A member of the squad.
 */
@Vapi4KDslMarker
interface Member {
  /**
  This is the assistant that will be used for the call. To use a transient assistant, use `assistant` instead.
   */
  fun assistantId(block: AssistantId.() -> Unit): AssistantId

  /**
  This is the assistant that will be used for the call. To use an existing assistant, use `assistantId` instead.
   */
  fun assistant(block: Assistant.() -> Unit): Assistant

  /**
  <p>These are the others assistants that this assistant can transfer to.
  <br>If the assistant already has transfer call tool, these destinations are just appended to existing ones.
  </p>
   */
  fun destinations(block: AssistantDestinations.() -> Unit): AssistantDestinations
}
