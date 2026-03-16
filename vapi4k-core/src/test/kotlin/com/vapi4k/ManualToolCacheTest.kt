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

package com.vapi4k

import com.vapi4k.common.FunctionName.Companion.toFunctionName
import com.vapi4k.dsl.tools.ManualToolCache
import com.vapi4k.dsl.tools.ToolWithServerImpl
import com.vapi4k.dtos.tools.ToolDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ManualToolCacheTest : StringSpec() {
  init {
    "addToCache stores tool and containsTool returns true" {
      val cache = ManualToolCache { "/test" }
      val toolName = "myTool_a1".toFunctionName()
      cache.addToCache(toolName, ToolWithServerImpl("myTool", ToolDto()))
      cache.containsTool(toolName) shouldBe true
    }

    "addToCache skips duplicate tool name silently" {
      val cache = ManualToolCache { "/test" }
      val toolName = "myTool_a1".toFunctionName()
      val tool1 = ToolWithServerImpl("myTool", ToolDto())
      val tool2 = ToolWithServerImpl("myTool-v2", ToolDto())
      cache.addToCache(toolName, tool1)
      cache.addToCache(toolName, tool2)
      cache.getTool(toolName) shouldBe tool1
    }

    "containsTool returns false for unknown name" {
      val cache = ManualToolCache { "/test" }
      cache.containsTool("unknown_a1".toFunctionName()) shouldBe false
    }

    "getTool throws for unknown function name" {
      val cache = ManualToolCache { "/test" }
      shouldThrow<IllegalStateException> {
        cache.getTool("unknown_a1".toFunctionName())
      }.message shouldBe "Manual tool not found: unknown_a1"
    }

    "cacheAsJson reflects cache size" {
      val cache = ManualToolCache { "/test" }
      cache.cacheAsJson().cacheSize shouldBe 0
      cache.addToCache("tool1_a1".toFunctionName(), ToolWithServerImpl("tool1", ToolDto()))
      cache.cacheAsJson().cacheSize shouldBe 1
    }
  }
}
