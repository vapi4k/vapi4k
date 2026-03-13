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

package com.vapi4k.api.voice

import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker
import com.vapi4k.dsl.voice.ChunkPlanProperties

/**
<p>This is the chunk plan for the voice output. It controls how the model output is split into chunks
before being sent to the voice provider for generation.
<br>Default <code>true</code> because voice generation sounds better with chunking (and reformatting them).
<br>To send every token from the model output directly to the voice provider and rely on the voice provider's audio
generation logic, set <code>enabled</code> to <code>false</code>.
</p>
 */
@Vapi4KDslMarker
interface ChunkPlan : ChunkPlanProperties {
  /**
  <p>This is the format plan for how chunks are reformatted before being sent to the voice provider. Many things are
  reformatted including phone numbers, emails and addresses to improve their enunciation.
  </p>
   */
  fun formatPlan(block: FormatPlan.() -> Unit)
}
