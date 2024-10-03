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

package com.vapi4k.console

import com.vapi4k.api.assistant.enums.AssistantServerMessageType
import com.vapi4k.api.assistant.enums.AssistantServerMessageType.FUNCTION_CALL
import com.vapi4k.api.assistant.enums.AssistantServerMessageType.TOOL_CALLS

enum class ToolType(
  val messageType: AssistantServerMessageType,
  val funcName: String,
  val paramName: String,
) {
  FUNCTION(FUNCTION_CALL, "functionCall", "parameters"),
  SERVICE_TOOL(TOOL_CALLS, "function", "arguments"),
  MANUAL_TOOL(TOOL_CALLS, "function", "arguments"),
}
