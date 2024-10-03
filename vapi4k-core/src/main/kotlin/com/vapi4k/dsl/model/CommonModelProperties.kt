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

package com.vapi4k.dsl.model

import com.vapi4k.api.functions.Functions
import com.vapi4k.api.model.KnowledgeBase
import com.vapi4k.api.tools.Tools

interface CommonModelProperties {
  /**
  This is the starting state for the conversation for the **system** role.
   */
  var systemMessage: String

  /**
  This is the starting state for the conversation for the **assistant** role.
   */
  var assistantMessage: String

  /**
  This is the starting state for the conversation for the **function** role.
   */
  var functionMessage: String

  /**
  This is the starting state for the conversation for the **tool** role.
   */
  var toolMessage: String

  /**
  This is the starting state for the conversation for the **user** role.
   */
  var userMessage: String

  /**
  These are the tools that the assistant can use during the call. To use existing tools, use `toolIds`.
  Both `tools` and `toolIds` can be used together.
   */
  fun tools(block: Tools.() -> Unit): Tools

  /**
  This is the function definition of the tool.
  For `endCall`, `transferCall`, and `dtmf` tools, this is auto-filled based on tool-specific fields like tool.destinations.
  But, even in those cases, you can provide a custom function definition for advanced use cases.
  An example of an advanced use case is if you want to customize the message that's spoken for `endCall` tool. You can
  specify a function where it returns an argument "reason". Then, in `messages` array, you can have many
  "request-complete" messages. One of these messages will be triggered if the `messages[].conditions` matches
  the "reason" argument.
   */
  fun functions(block: Functions.() -> Unit): Functions

  /**
  These are the options for the knowledge base.
   */
  fun knowledgeBase(block: KnowledgeBase.() -> Unit): KnowledgeBase
}
