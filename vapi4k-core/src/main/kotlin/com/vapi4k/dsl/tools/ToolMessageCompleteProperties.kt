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

import com.vapi4k.api.tools.ToolMessageRoleType

interface ToolMessageCompleteProperties {
  /**
  <p>This is optional and defaults to "assistant".
  <br>When role=assistant, <code>content</code> is said out loud.
  <br>When role=system, <code>content</code> is passed to the model in a system message. Example: system: default one assistant: user: assistant: user: assistant: user: assistant: tool called tool: your server response <--- system prompt as hint ---> model generates response which is spoken This is useful when you want to provide a hint to the model about what to say next.
  </p>
   */
  var role: ToolMessageRoleType

  /**
  <p>This is an optional boolean that if true, the call will end after the message is spoken. Default is false.
  <br>This is ignored if <code>role</code> is set to <code>system</code>.
  </p>
   */
  var endCallAfterSpokenEnabled: Boolean?

  /**
  This is the content that the assistant says when this message is triggered.
   */
  var content: String
}
