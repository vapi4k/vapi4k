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

package com.vapi4k.dsl.vapi4k

import com.vapi4k.api.vapi4k.RequestContext
import com.vapi4k.api.vapi4k.ResponseContext
import com.vapi4k.api.vapi4k.enums.ServerRequestType

interface CommonCallbacks {
  /**
  Whenever a request is made, the contents of the onAllRequests{} block will be executed.
   */
  fun onAllRequests(block: suspend (requestContext: RequestContext) -> Unit)

  /**
  Whenever a request is made for the specified type(s), the contents of the onRequest{} block will be executed.
   */
  fun onRequest(
    requestType: ServerRequestType,
    vararg requestTypes: ServerRequestType,
    block: suspend (requestContext: RequestContext) -> Unit,
  )

  /**
  Whenever a response is received, the contents of the onAllResponses{} block will be executed.
   */
  fun onAllResponses(block: suspend (responseContext: ResponseContext) -> Unit)

  /**
  Whenever a response of the specified type(s) is received, the contents of the onResponse{} block will be executed.
   */
  fun onResponse(
    requestType: ServerRequestType,
    vararg requestTypes: ServerRequestType,
    block: suspend (responseContext: ResponseContext) -> Unit,
  )
}
