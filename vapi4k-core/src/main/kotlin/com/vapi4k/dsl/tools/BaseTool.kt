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

import com.vapi4k.api.tools.ToolCondition
import com.vapi4k.api.tools.ToolMessageComplete
import com.vapi4k.api.tools.ToolMessageDelayed
import com.vapi4k.api.tools.ToolMessageFailed
import com.vapi4k.api.tools.ToolMessageStart
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker
import com.vapi4k.dtos.tools.ToolMessageCondition

@Vapi4KDslMarker
interface BaseTool {
  /**
  <p>Adds a ToolMessageStart to the messages in the tool.
  <br>This message is triggered when the tool call starts.
  <br>This message is never triggered for async tools.
  <br>If this message is not provided, one of the default filler messages "Hold on a sec", "One moment", "Just a sec", "Give me a moment" or "This'll just take a sec" will be used.
  </p>
   */
  fun requestStartMessage(block: ToolMessageStart.() -> Unit): ToolMessageStart

  /**
  <p>Adds a ToolMessageComplete to the messages in the tool.
  <br>This message is triggered when the tool call is complete.
  <br>This message is triggered immediately without waiting for your server to respond for async tool calls.
  <br>If this message is not provided, the model will be requested to respond.
  <br>If this message is provided, only this message will be spoken and the model will not be requested to come up with a response. It's an exclusive OR.
  </p>
   */
  fun requestCompleteMessage(block: ToolMessageComplete.() -> Unit): ToolMessageComplete

  /**
  <p>Adds a ToolMessageFailed to the messages in the tool.
  <br>This message is triggered when the tool call fails.
  <br>This message is never triggered for async tool calls.
  <br>If this message is not provided, the model will be requested to respond.
  <br>If this message is provided, only this message will be spoken and the model will not be requested to come up with a response. It's an exclusive OR.
  </p>
   */
  fun requestFailedMessage(block: ToolMessageFailed.() -> Unit): ToolMessageFailed

  /**
  <p>Adds a ToolMessageDelayed to the messages in the tool.
  <br>This message is triggered when the tool call is delayed.
  <br>There are the two things that can trigger this message:
  <ol>
  <li>The user talks with the assistant while your server is processing the request. Default is "Sorry, a few more seconds."
  <li>The server doesn't respond within timingMilliseconds.
  <li>This message is never triggered for async tool calls.
  </ol>
  </p>
   */
  fun requestDelayedMessage(block: ToolMessageDelayed.() -> Unit): ToolMessageDelayed

  /**
  Adds a ToolCondition to the optional array of conditions that the tool call arguments must meet in order for this message to be triggered.
   */
  fun condition(
    requiredCondition: ToolMessageCondition,
    vararg additionalConditions: ToolMessageCondition,
    block: ToolCondition.() -> Unit,
  )
}
