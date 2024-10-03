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

import com.vapi4k.common.CacheKey
import com.vapi4k.common.CacheKey.Companion.cacheKeyValue
import com.vapi4k.common.Utils.ensureStartsWith
import com.vapi4k.common.Utils.isNull
import com.vapi4k.dsl.functions.FunctionDetails
import com.vapi4k.dsl.functions.FunctionInfo
import com.vapi4k.dsl.functions.FunctionInfoDto
import com.vapi4k.dsl.functions.ToolCallInfo
import com.vapi4k.dsl.model.AbstractModelImpl
import com.vapi4k.plugin.Vapi4kServer.logger
import com.vapi4k.server.RequestContextImpl
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KFunction
import kotlin.time.Duration

internal class ServiceCache(
  private val pathBlock: () -> String,
) {
  private var lastCacheCleanInstant = Clock.System.now()
  private val cacheMap = ConcurrentHashMap<CacheKey, FunctionInfo>()

  private val path get() = pathBlock().ensureStartsWith("/")

  fun isNotEmpty() = cacheMap.isNotEmpty()

  fun addToCache(
    model: AbstractModelImpl,
    obj: Any,
    function: KFunction<*>,
  ) {
    val sessionId = model.modelUnion.requestContext.sessionId
    val assistantId = model.assistantId
    val cacheKey = cacheKeyValue(sessionId, assistantId)
    val toolCallInfo = ToolCallInfo(assistantId, function)
    val funcName = toolCallInfo.llmName
    val funcInfo = cacheMap.computeIfAbsent(cacheKey) { FunctionInfo(sessionId, assistantId) }
    val funcDetails = funcInfo.getFunctionOrNull(funcName)

    if (funcDetails.isNull()) {
      val newFuncDetails = FunctionDetails(obj, function, toolCallInfo)
      funcInfo.addFunction(funcName, newFuncDetails)
      logger.info { "Added \"$funcName\" (${newFuncDetails.fqName}) to $path serviceTool cache [$cacheKey]" }
    } else {
      error("\"$funcName\" already declared in cache at ${funcDetails.fqName} [$cacheKey]")
    }
  }

  fun containsIds(requestContext: RequestContextImpl) = cacheMap.containsKey(cacheKeyValue(requestContext))

  fun getFromCache(requestContext: RequestContextImpl): FunctionInfo =
    cacheKeyValue(requestContext).let { key -> cacheMap[key] ?: error("Cache key not found: $key") }

  fun removeFromCache(
    requestContext: RequestContextImpl,
    block: (FunctionInfo) -> Unit,
  ): FunctionInfo? =
    cacheKeyValue(requestContext).let { key ->
      cacheMap.remove(key)
        ?.also { block(it) }
        .also {
          if (it.isNull())
            logger.debug { "Entry not found in serviceTool cache: $key" }
        }
    }

  fun clearToolCache() {
    cacheMap.clear()
  }

  fun purgeToolCache(maxAge: Duration): Int {
    var count = 0
    lastCacheCleanInstant = Clock.System.now()
    cacheMap.entries.removeIf { (cacheKey, funcInfo) ->
      (funcInfo.age > maxAge).also { isOld ->
        if (isOld) {
          count++
          logger.debug { "Purging serviceTool cache entry $cacheKey: $funcInfo" }
        }
      }
    }
    logger.debug { "Purged serviceTool cache ($count)" }
    return count
  }

  private val asDtoMap: Map<CacheKey, FunctionInfoDto>
    get() = cacheMap.map { (k, v) -> k to v.toFunctionInfoDto() }.toMap()

  fun cacheAsJson() = ServiceCacheInfoDto(lastCacheCleanInstant.toString(), asDtoMap.size, asDtoMap)
}

@Serializable
class ServiceCacheInfoDto(
  val lastPurgeTime: String = "",
  val cacheSize: Int = -1,
  val cache: Map<CacheKey, FunctionInfoDto>? = null,
)
