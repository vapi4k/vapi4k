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

package com.vapi4k.dsl.tools

import com.vapi4k.api.tools.Server
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

@Vapi4KDslMarker
interface ToolWithServer : BaseTool {
  /**
  <p>This is the server that will be hit when this tool is requested by the LLM.
  <br>All requests will be sent with the call object among other things. You can find more details in the Server URL documentation.
  <br>his overrides the serverUrl set on the org and the phoneNumber. Order of precedence: highest tool.server.url, then assistant.serverUrl, then phoneNumber.serverUrl, then org.serverUrl.
  </p>
   */
  fun server(block: Server.() -> Unit): Server
}
