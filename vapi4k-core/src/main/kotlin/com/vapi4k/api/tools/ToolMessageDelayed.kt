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

import com.vapi4k.dsl.tools.ToolMessageDelayedProperties
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

/**
<p>This message is triggered when the tool call is delayed.
<brThere are the two things that can trigger this message:
<ol>
<li>The user talks with the assistant while your server is processing the request. Default is "Sorry, a few more seconds."
<li>The server doesn't respond within timingMilliseconds.
<li>This message is never triggered for async tool calls.
</ol>
</p>
 */
@Vapi4KDslMarker
interface ToolMessageDelayed : ToolMessageDelayedProperties
