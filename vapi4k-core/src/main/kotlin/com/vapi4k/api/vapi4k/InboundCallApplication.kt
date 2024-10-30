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

package com.vapi4k.api.vapi4k

import com.vapi4k.api.response.InboundCallAssistantResponse
import com.vapi4k.api.tools.TransferDestinationResponse
import com.vapi4k.dsl.vapi4k.CommonCallbacks
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

@Vapi4KDslMarker
interface InboundCallApplication : CommonCallbacks {
  /**
  Defaults to "/vapi4k"
   */
  var serverPath: String

  /**
  The server secret in Vapi is a security feature that allows you to authenticate requests sent from Vapi to your server. It will be sent with every request from Vapi to your server.
   */
  var serverSecret: String

  /**
  Whenever an AssistantRequest is made, the contents of the onAssistantRequest{} block will be executed.
   */
  fun onAssistantRequest(block: suspend InboundCallAssistantResponse.(RequestContext) -> Unit)

  /**
  Whenever a TransferDestinationRequest is made, the contents of the onTransferDestinationRequest{} block will be executed.
   */
  fun onTransferDestinationRequest(block: suspend TransferDestinationResponse.(RequestContext) -> Unit)
}
