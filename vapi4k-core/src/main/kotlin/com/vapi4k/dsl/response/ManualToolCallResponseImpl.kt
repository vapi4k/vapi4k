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

import com.vapi4k.api.assistant.ManualToolCallResponse
import com.vapi4k.api.toolservice.RequestCompleteMessages
import com.vapi4k.api.toolservice.RequestFailedMessages
import com.vapi4k.dsl.toolservice.RequestCompleteMessagesImpl
import com.vapi4k.dsl.toolservice.RequestFailedMessagesImpl
import com.vapi4k.responses.ToolCallResult

class ManualToolCallResponseImpl internal constructor(
  private val completeMessages: RequestCompleteMessagesImpl,
  private val failedMessages: RequestFailedMessagesImpl,
  private val toolCallResult: ToolCallResult,
) : ManualToolCallResponse {
  override var result: String
    get() = toolCallResult.result
    set(value) {
      toolCallResult.result = value
    }

  override var error: String
    get() = toolCallResult.error
    set(value) {
      toolCallResult.error = value
    }

  override fun requestCompleteMessages(block: RequestCompleteMessages.() -> Unit) {
    completeMessages.apply(block).messageList
  }

  override fun requestFailedMessages(block: RequestFailedMessages.() -> Unit) {
    failedMessages.apply(block).messageList
  }
}
