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

package com.vapi4k.server

import com.vapi4k.common.Endpoints.CACHES_PATH
import com.vapi4k.common.Utils.ensureStartsWith
import com.vapi4k.dsl.vapi4k.PipelineCall
import com.vapi4k.dsl.vapi4k.Vapi4kConfigImpl
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import kotlinx.serialization.json.buildJsonObject

internal object CacheActions {
  suspend fun PipelineCall.clearCaches(config: Vapi4kConfigImpl) {
    config.allApplications.forEach { application ->
      with(application) {
        clearServiceToolCache()
        clearFunctionCache()
      }
    }
    call.respondRedirect(CACHES_PATH)
  }

  suspend fun PipelineCall.cachesRequest(config: Vapi4kConfigImpl) {
    call.respond(
      buildJsonObject {
        config.allApplications.forEach { application ->
          put(
            application.fullServerPath.ensureStartsWith("/"),
            buildJsonObject {
              put(
                "serviceTools",
                application.serviceCacheAsJson(),
              )
              put(
                "functions",
                application.functionCacheAsJson(),
              )
              put(
                "manualTools",
                application.manualCacheAsJson(),
              )
            },
          )
        }
      },
    )
  }
}
