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

package com.vapi4k.responses

import com.vapi4k.api.vapi4k.AssistantRequestUtils.functionName
import com.vapi4k.api.vapi4k.AssistantRequestUtils.functionParameters
import com.vapi4k.common.Utils.errorMsg
import com.vapi4k.plugin.Vapi4kServer.logger
import com.vapi4k.server.RequestContextImpl
import kotlinx.serialization.Serializable

@Serializable
class FunctionResponse(
  var result: String = "",
) {
  companion object {
    suspend fun getFunctionCallResponse(requestContext: RequestContextImpl): FunctionResponse =
      FunctionResponse()
        .also { response ->
          with(requestContext) {
            val funcName = requestContext.request.functionName
            val invokeArgs = requestContext.request.functionParameters
            runCatching {
              if (application.containsFunction(requestContext, funcName)) {
                application.getFunction(requestContext, funcName)
                  .invokeToolMethod(
                    isTool = false,
                    requestContext = requestContext,
                    invokeArgs = invokeArgs,
                    messageDtos = mutableListOf(),
                    successAction = { result -> response.result = result },
                    errorAction = { result -> response.result = result },
                  )
              } else {
                error("Function not found: $funcName")
              }
            }.getOrElse { e ->
              val errorMsg = e.message ?: "Error invoking function: $funcName ${e.errorMsg}"
              logger.error(e) { errorMsg }
            }
          }
        }
  }
}
