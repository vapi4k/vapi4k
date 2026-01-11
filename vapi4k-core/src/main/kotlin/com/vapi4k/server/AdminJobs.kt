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

package com.vapi4k.server

import com.vapi4k.api.vapi4k.ServerRequestType.Companion.getServerRequestType
import com.vapi4k.common.CoreEnvVars.TOOL_CACHE_CLEAN_PAUSE_MINS
import com.vapi4k.common.CoreEnvVars.TOOL_CACHE_MAX_AGE_MINS
import com.vapi4k.common.Utils.errorMsg
import com.vapi4k.dsl.vapi4k.RequestResponseType.REQUEST
import com.vapi4k.dsl.vapi4k.RequestResponseType.RESPONSE
import com.vapi4k.dsl.vapi4k.Vapi4kConfigImpl
import com.vapi4k.plugin.Vapi4kServer.logger
import com.vapi4k.server.RequestResponseCallback.Companion.requestCallback
import com.vapi4k.server.RequestResponseCallback.Companion.responseCallback
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonElement
import kotlin.concurrent.thread
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

internal object AdminJobs {
  fun startCacheCleaningThread(config: Vapi4kConfigImpl) {
    thread {
      val pause = TOOL_CACHE_CLEAN_PAUSE_MINS.toInt().minutes
      val maxAge = TOOL_CACHE_MAX_AGE_MINS.toInt().minutes
      while (true) {
        runCatching {
          Thread.sleep(pause.inWholeMilliseconds)
          config.allApplications.forEach { application ->
            logger.debug { "Purging cache for ${application.serverPath}" }
            with(application) {
              purgeServiceToolCache(maxAge)
              purgeFunctionCache(maxAge)
            }
          }
        }.onFailure { e ->
          logger.error(e) { "Error clearing cache: ${e.errorMsg}" }
        }
      }
    }
  }

  fun startCallbackThread(config: Vapi4kConfigImpl) {
    thread {
      while (true) {
        runCatching {
          runBlocking {
            for (callback in config.callbackChannel) {
              coroutineScope {
                when (callback.type) {
                  REQUEST -> {
                    config.allApplications
                      .filter { it.applicationId == callback.applicationId }
                      .forEach { app ->
                        with(app) {
                          applicationAllRequests.forEach { launch { it.invoke(callback.toRequestContext(config)) } }
                          applicationPerRequests
                            .filter { it.first == callback.request.getServerRequestType("startCallbackThread1") }
                            .forEach { (_, block) -> launch { block(callback.toRequestContext(config)) } }
                        }
                      }

                    with(config) {
                      globalAllRequests.forEach { launch { it.invoke(callback.toRequestContext(config)) } }
                      globalPerRequests
                        .filter { it.first == callback.request.getServerRequestType("startCallbackThread2") }
                        .forEach { (_, block) -> launch { block(callback.toRequestContext(config)) } }
                    }
                  }

                  RESPONSE -> {
                    config.allApplications.forEach { application ->
                      with(application) {
                        if (applicationAllResponses.isNotEmpty() || applicationPerResponses.isNotEmpty()) {
                          val resp =
                            runCatching {
                              callback.response.invoke()
                            }.onFailure { e ->
                              logger.error { "Error creating response" }
                              error("Error creating response")
                            }.getOrThrow()

                          applicationAllResponses.forEach {
                            launch { it.invoke(callback.toResponseContext(config, resp)) }
                          }
                          applicationPerResponses
                            .filter { it.first == callback.request.getServerRequestType("startCallbackThread3") }
                            .forEach { (_, block) ->
                              launch { block(callback.toResponseContext(config, resp)) }
                            }
                        }
                      }
                    }

                    with(config) {
                      if (globalAllResponses.isNotEmpty() || globalPerResponses.isNotEmpty()) {
                        val resp =
                          runCatching {
                            callback.response.invoke()
                          }.onFailure { e ->
                            logger.error { "Error creating response" }
                            error("Error creating response")
                          }.getOrThrow()

                        globalAllResponses.forEach {
                          launch { it.invoke(callback.toResponseContext(config, resp)) }
                        }

                        globalPerResponses
                          .filter { it.first == callback.request.getServerRequestType("startCallbackThread4") }
                          .forEach { (_, block) ->
                            launch { block(callback.toResponseContext(config, resp)) }
                          }
                      }
                    }
                  }
                }
              }
            }
          }
        }.onFailure { e ->
          logger.error(e) { "Error processing request response callback: ${e.errorMsg}" }
        }
      }
    }
  }

  suspend fun invokeRequestCallbacks(
    config: Vapi4kConfigImpl,
    requestContext: RequestContextImpl,
  ) = config.callbackChannel.send(requestCallback(requestContext))

  suspend fun invokeResponseCallbacks(
    config: Vapi4kConfigImpl,
    requestContext: RequestContextImpl,
    response: () -> JsonElement,
    elapsed: Duration,
  ) = config.callbackChannel.send(responseCallback(requestContext, response, elapsed))
}
