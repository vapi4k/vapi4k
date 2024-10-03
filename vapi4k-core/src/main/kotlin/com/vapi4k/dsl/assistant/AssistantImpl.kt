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

package com.vapi4k.dsl.assistant

import com.vapi4k.api.assistant.Assistant
import com.vapi4k.api.assistant.AssistantOverrides
import com.vapi4k.dtos.assistant.AssistantDto
import com.vapi4k.dtos.assistant.AssistantOverridesDto
import com.vapi4k.server.RequestContextImpl
import com.vapi4k.utils.AssistantIdSource
import com.vapi4k.utils.DuplicateInvokeChecker

interface AssistantProperties : CommonAssistantProperties

class AssistantImpl internal constructor(
  override val requestContext: RequestContextImpl,
  private val assistantIdSource: AssistantIdSource,
  private val assistantDto: AssistantDto,
  private val assistantOverridesDto: AssistantOverridesDto,
) : AbstractAssistantImpl(),
  AssistantProperties by assistantDto,
  Assistant {
  override val assistantId = assistantIdSource.nextAssistantId(requestContext)
  override val transcriberChecker = DuplicateInvokeChecker()
  override val modelChecker = DuplicateInvokeChecker()
  override val voiceChecker = DuplicateInvokeChecker()
  override val modelDtoUnion get() = assistantDto
  override val voicemailDetectionDto get() = modelDtoUnion.voicemailDetectionDto
  override val analysisPlanDto get() = modelDtoUnion.analysisPlanDto
  override val artifactPlanDto get() = modelDtoUnion.artifactPlanDto

  // AssistantOverrides
  override fun assistantOverrides(block: AssistantOverrides.() -> Unit): AssistantOverrides =
    AssistantOverridesImpl(
      requestContext,
      assistantIdSource,
      assistantOverridesDto,
    ).apply(block)
}
