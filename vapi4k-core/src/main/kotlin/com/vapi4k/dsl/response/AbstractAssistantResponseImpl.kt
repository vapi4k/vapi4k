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

import com.vapi4k.api.assistant.Assistant
import com.vapi4k.api.assistant.AssistantId
import com.vapi4k.api.squad.Squad
import com.vapi4k.api.squad.SquadId
import com.vapi4k.common.DuplicateInvokeChecker
import com.vapi4k.common.QueryParams.APPLICATION_ID
import com.vapi4k.common.QueryParams.ASSISTANT_ID
import com.vapi4k.common.QueryParams.SESSION_ID
import com.vapi4k.dsl.assistant.AssistantIdImpl
import com.vapi4k.dsl.assistant.AssistantImpl
import com.vapi4k.dsl.squad.SquadIdImpl
import com.vapi4k.dsl.squad.SquadImpl
import com.vapi4k.responses.AssistantMessageResponse
import com.vapi4k.server.RequestContextImpl
import com.vapi4k.utils.MiscUtils.appendQueryParams

abstract class AbstractAssistantResponseImpl internal constructor(
  internal val requestContext: RequestContextImpl,
) {
  internal val duplicateChecker = DuplicateInvokeChecker()
  internal val assistantRequestResponse = AssistantMessageResponse()

  internal val messageResponse get() = assistantRequestResponse.messageResponse
  internal val isAssigned get() = duplicateChecker.wasCalled

  fun assistant(block: Assistant.() -> Unit): Assistant {
    duplicateChecker.check("assistant{} was already called")
    val assistantIdSource = AssistantIdSource()
    return AssistantImpl(
      requestContext,
      assistantIdSource,
      messageResponse.assistantDto,
      messageResponse.assistantOverridesDto,
    ).apply(block)
      .apply {
        with(messageResponse.assistantDto) {
          verifyValues()

          serverUrl = requestContext.application.serverUrl.appendQueryParams(
            APPLICATION_ID to requestContext.application.applicationId.value,
            SESSION_ID to requestContext.sessionId.value,
            ASSISTANT_ID to assistantId.value,
          )
        }
      }
  }

  fun assistantId(block: AssistantId.() -> Unit): AssistantId {
    duplicateChecker.check("assistantId{} was already called")
    val assistantIdSource = AssistantIdSource()
    return AssistantIdImpl(requestContext, assistantIdSource, messageResponse).apply(block)
  }

  fun squad(block: Squad.() -> Unit): Squad {
    duplicateChecker.check("squad{} was already called")
    val assistantIdSource = AssistantIdSource()
    return SquadImpl(requestContext, assistantIdSource, messageResponse.squadDto).apply(block)
  }

  fun squadId(block: SquadId.() -> Unit): SquadId {
    duplicateChecker.check("squadId{} was already called")
    return SquadIdImpl(requestContext, messageResponse).apply(block)
  }
}
