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

package com.vapi4k.dsl.vapi4k

import com.vapi4k.api.reponse.InboundCallAssistantResponse
import com.vapi4k.api.vapi4k.InboundCallApplication
import com.vapi4k.api.vapi4k.RequestContext
import com.vapi4k.common.Utils.isNull
import com.vapi4k.dsl.response.InboundCallAssistantResponseImpl
import com.vapi4k.responses.AssistantMessageResponse
import com.vapi4k.server.RequestContextImpl

class InboundCallApplicationImpl internal constructor() :
  AbstractApplicationImpl(ApplicationType.INBOUND_CALL),
  InboundCallApplication {
  private var assistantRequest: (suspend InboundCallAssistantResponse.(RequestContext) -> Unit)? = null

  override fun onAssistantRequest(block: suspend InboundCallAssistantResponse.(RequestContext) -> Unit) {
    if (assistantRequest.isNull())
      assistantRequest = block
    else
      error("onAssistantRequest{} can be called only once per inboundCallApplication{}")
  }

  override suspend fun getAssistantResponse(requestContext: RequestContextImpl): AssistantMessageResponse =
    assistantRequest.let { requestCB ->
      if (requestCB.isNull()) {
        error("onAssistantRequest{} not called")
      } else {
        val assistantResponse = InboundCallAssistantResponseImpl(requestContext)
        requestCB.invoke(assistantResponse, requestContext)
        if (!assistantResponse.isAssigned)
          error("onAssistantRequest{} is missing a call to assistant{}, assistantId{}, squad{}, or squadId{}")
        else
          assistantResponse.assistantRequestResponse
      }
    }
}
