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

package com.vapi4k.api.tools

import com.vapi4k.dsl.tools.ToolMessageStartProperties
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

/**
<p>This message is triggered when the tool call starts.
<br>This message is never triggered for async tools.
<br>If this message is not provided, one of the default filler messages "Hold on a sec", "One moment", "Just a sec", "Give me a moment" or "This'll just take a sec" will be used.
</p>
 */
@Vapi4KDslMarker
interface ToolMessageStart : ToolMessageStartProperties
