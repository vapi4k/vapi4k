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

import com.vapi4k.common.FunctionName
import com.vapi4k.common.Utils.ensureStartsWith
import com.vapi4k.dtos.tools.ToolDto
import com.vapi4k.plugin.Vapi4kServer.logger
import kotlinx.serialization.Serializable

internal class ManualToolCache(
  private val pathBlock: () -> String,
) {
  private val manualTools = mutableMapOf<FunctionName, ToolWithServerImpl>()

  val functions get() = manualTools.values
  private val path get() = pathBlock().ensureStartsWith("/")

  fun addToCache(
    toolName: FunctionName,
    toolImpl: ToolWithServerImpl,
  ) {
    if (manualTools.containsKey(toolName)) {
      // Manual tools do not get stored per session
      // Skip adding the tool after the first time it is declared
      logger.debug { "Manual tool name already declared: $toolName" }
    } else {
      manualTools[toolName] = toolImpl
      logger.info { "Added \"$toolName\" to $path manualTool cache" }
    }
  }

  fun containsTool(toolName: FunctionName) = manualTools.containsKey(toolName)

  fun getTool(toolName: FunctionName): ToolWithServerImpl =
    manualTools[toolName] ?: error("Manual tool name found: $toolName")

  private val asDtoMap: Map<FunctionName, ToolDto>
    get() = manualTools.map { (k, v) -> k to v.toolDto }.toMap()

  fun cacheAsJson() = ManualCacheInfoDto(manualTools.size, asDtoMap)
}

@Serializable
class ManualCacheInfoDto(
  val cacheSize: Int = -1,
  val cache: Map<FunctionName, ToolDto>? = null,
)
