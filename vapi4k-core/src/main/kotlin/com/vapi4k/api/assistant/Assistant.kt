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

package com.vapi4k.api.assistant

import com.vapi4k.dsl.assistant.AssistantProperties
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

/**
<p>This is the assistant that will be used for the call. To use an existing assistant, use `assistantId` instead.
<br><br>If you're unsure why you're getting an invalid assistant, try logging your response and send the JSON blob to POST /assistant which will return the validation errors.
</p>
 */
@Vapi4KDslMarker
interface Assistant :
  AssistantProperties,
  AssistantModels,
  AssistantVoices,
  AssistantTranscribers,
  AssistantFunctions {
  /**
   * These are the overrides for the `assistant`'s settings and template variables.
   */
  fun assistantOverrides(block: AssistantOverrides.() -> Unit): AssistantOverrides
}
