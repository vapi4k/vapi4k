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

package com.vapi4k.api.assistant

import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

/**
This is the assistant that will be used for the call. To use a transient assistant, use `assistant` instead.
 */
@Vapi4KDslMarker
interface AssistantId {
  /**
  This is the id of the assistant that will be used for the call.
   */
  var id: String

  /**
  These are the overrides for the `assistantId`'s settings and template variables.
   */
  fun assistantOverrides(block: AssistantOverrides.() -> Unit): AssistantOverrides
}
