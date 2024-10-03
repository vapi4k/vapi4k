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

import com.vapi4k.dsl.assistant.ArtifactPlanProperties
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

/**
<p>This is the plan for artifacts generated during assistant's calls. Stored in <code>call.artifact</code>.
<br><br>Note: <code>recordingEnabled</code> is currently at the root level. It will be moved to <code>artifactPlan</code> in the future, but will remain backwards compatible.
</p>
 */
@Vapi4KDslMarker
interface ArtifactPlan : ArtifactPlanProperties
