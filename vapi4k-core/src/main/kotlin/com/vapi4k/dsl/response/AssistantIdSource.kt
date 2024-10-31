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

import com.vapi4k.common.AssistantId
import com.vapi4k.common.AssistantId.Companion.toAssistantId
import com.vapi4k.common.Constants.ASSISTANT_ID_WIDTH
import com.vapi4k.server.RequestContextImpl

class AssistantIdSource {
  private var assistantCounter = 1

  internal fun nextAssistantId(requestContext: RequestContextImpl): AssistantId {
    val newId = (assistantCounter++).toString().padStart(ASSISTANT_ID_WIDTH, '0').toAssistantId()

    // Add assistantId to the application's list of them
    requestContext.application.addAssistantId(newId)

    return newId
  }
}
