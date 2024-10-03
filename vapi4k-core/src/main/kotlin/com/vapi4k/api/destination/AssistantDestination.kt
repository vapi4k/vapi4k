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

package com.vapi4k.api.destination

import com.vapi4k.api.destination.enums.AssistantTransferMode
import com.vapi4k.dsl.destination.CommonDestination
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

/**
This is the assistant destination you'd like the call to be transferred to.
 */
@Vapi4KDslMarker
interface AssistantDestination : CommonDestination {
  /**
  This is the assistant to transfer the call to.
   */
  var assistantName: String

  /**
  <p>This is the mode to use for the transfer. Default is <code>rolling-history</code>.
  <ul><li><code>rolling-history</code>: This is the default mode. It keeps the entire conversation history and appends the new assistant's system message on transfer.
  Example:
  Pre-transfer: system: assistant1 system message assistant: assistant1 first message user: hey, good morning assistant: how can I help? user: I need help with my account assistant: (destination.message)
  Post-transfer: system: assistant1 system message assistant: assistant1 first message user: hey, good morning assistant: how can I help? user: I need help with my account assistant: (destination.message) system: assistant2 system message assistant: assistant2 first message (or model generated if firstMessageMode is set to assistant-speaks-first-with-model-generated-message)
  <li><code>swap-system-message-in-history</code>: This replaces the original system message with the new assistant's system message on transfer.
  Example:
  Pre-transfer: system: assistant1 system message assistant: assistant1 first message user: hey, good morning assistant: how can I help? user: I need help with my account assistant: (destination.message)
  Post-transfer: system: assistant2 system message assistant: assistant1 first message user: hey, good morning assistant: how can I help? user: I need help with my account assistant: (destination.message) assistant: assistant2 first message (or model generated if firstMessageMode is set to <code>assistant-speaks-first-with-model-generated-message</code>)
  </ul></p>
   */
  var transferMode: AssistantTransferMode
}
