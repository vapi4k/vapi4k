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

package com.vapi4k.api.tools

import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

/**
This is a condition that the tool call arguments must meet in order for a message to be triggered.
 */
@Vapi4KDslMarker
interface ToolCondition {
  /**
  Creates a request start message and adds it to the messages in the tool.
   */
  fun requestStartMessage(block: ToolMessageStart.() -> Unit): ToolMessageStart

  /**
  Creates a request complete message and adds it to the messages in the tool.
   */
  fun requestCompleteMessage(block: ToolMessageComplete.() -> Unit): ToolMessageComplete

  /**
  Creates a request failed message and adds it to the messages in the tool.
   */
  fun requestFailedMessage(block: ToolMessageFailed.() -> Unit): ToolMessageFailed

  /**
  Creates a request delayed message and adds it to the messages in the tool.
   */
  fun requestDelayedMessage(block: ToolMessageDelayed.() -> Unit): ToolMessageDelayed
}
