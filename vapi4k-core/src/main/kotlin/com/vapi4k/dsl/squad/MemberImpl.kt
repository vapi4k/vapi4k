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

package com.vapi4k.dsl.squad

import com.vapi4k.api.assistant.Assistant
import com.vapi4k.api.squad.AssistantDestinations
import com.vapi4k.api.squad.AssistantId
import com.vapi4k.api.squad.Member
import com.vapi4k.common.DuplicateInvokeChecker
import com.vapi4k.common.QueryParams.APPLICATION_ID
import com.vapi4k.common.QueryParams.ASSISTANT_ID
import com.vapi4k.common.QueryParams.SESSION_ID
import com.vapi4k.dsl.assistant.AssistantImpl
import com.vapi4k.dtos.squad.MemberDto
import com.vapi4k.utils.MiscUtils.appendQueryParams

data class MemberImpl(
  internal val members: MembersImpl,
  private val dto: MemberDto,
) : Member {
  private val memberChecker = DuplicateInvokeChecker()

  override fun assistantId(block: AssistantId.() -> Unit): AssistantId {
    memberChecker.check("Member already has an assistantId assigned")
    return AssistantIdImpl(dto).apply(block)
  }

  override fun assistant(block: Assistant.() -> Unit): Assistant {
    memberChecker.check("Member already has an assistant assigned")
    return AssistantImpl(
      members.squad.requestContext,
      members.squad.assistantIdSource,
      dto.assistantDto,
      dto.assistantOverridesDto,
    ).apply(block)
      .apply {
        this@MemberImpl.dto.assistantDto.serverUrl =
          requestContext.application.serverUrl
            .appendQueryParams(
              APPLICATION_ID to requestContext.application.applicationId.value,
              SESSION_ID to requestContext.sessionId.value,
              ASSISTANT_ID to assistantId.value,
            )
      }
  }

  override fun destinations(block: AssistantDestinations.() -> Unit): AssistantDestinations =
    AssistantDestinationsImpl(this, dto).apply(block)
}
