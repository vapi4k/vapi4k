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

import com.vapi4k.api.tools.ToolCall
import com.vapi4k.dsl.functions.FunctionUtils
import com.vapi4k.dsl.functions.FunctionUtils.llmType
import com.vapi4k.dsl.functions.FunctionUtils.verifyIsToolCall
import com.vapi4k.dsl.functions.FunctionUtils.verifyIsValidReturnType
import com.vapi4k.dsl.functions.FunctionUtils.verifyObjectHasOnlyOneToolCall
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test

class FunctionUtilsTest {
  // Test service classes for reflection tests

  class NoAnnotationService {
    fun doSomething(): String = "hello"
  }

  class TwoAnnotationService {
    @ToolCall("first")
    fun method1(): String = "a"

    @ToolCall("second")
    fun method2(): String = "b"
  }

  class ValidService {
    @ToolCall("valid tool")
    fun doWork(name: String): String = name
  }

  class ListReturnService {
    @ToolCall("returns list")
    fun getItems(): List<String> = emptyList()
  }

  class UnitReturnService {
    @ToolCall("returns unit")
    fun doWork() {}
  }

  class IntReturnService {
    @ToolCall("returns int")
    fun getCount(): Int = 42
  }

  class BooleanReturnService {
    @ToolCall("returns boolean")
    fun isReady(): Boolean = true
  }

  class DoubleReturnService {
    @ToolCall("returns double")
    fun getScore(): Double = 3.14
  }

  @Test
  fun `verifyIsToolCall rejects unannotated function`() {
    val service = NoAnnotationService()
    val function = service::class.members.first { it.name == "doSomething" }
    assertThrows(IllegalStateException::class.java) {
      verifyIsToolCall(true, function as kotlin.reflect.KFunction<*>)
    }.also {
      assert(it.message.orEmpty().contains("missing @ToolCall annotation"))
    }
  }

  @Test
  fun `verifyObjectHasOnlyOneToolCall rejects zero annotations`() {
    assertThrows(IllegalStateException::class.java) {
      verifyObjectHasOnlyOneToolCall(NoAnnotationService())
    }.also {
      assert(it.message.orEmpty().contains("No method with @ToolCall annotation found"))
    }
  }

  @Test
  fun `verifyObjectHasOnlyOneToolCall rejects two annotations`() {
    assertThrows(IllegalStateException::class.java) {
      verifyObjectHasOnlyOneToolCall(TwoAnnotationService())
    }.also {
      assert(it.message.orEmpty().contains("Only one method with @ToolCall annotation is allowed"))
    }
  }

  @Test
  fun `verifyObjectHasOnlyOneToolCall accepts valid service`() {
    // Should not throw
    verifyObjectHasOnlyOneToolCall(ValidService())
  }

  @Test
  fun `verifyIsValidReturnType rejects List return`() {
    val service = ListReturnService()
    val function = service::class.members.first { it.name == "getItems" }
    assertThrows(IllegalStateException::class.java) {
      verifyIsValidReturnType(true, function as kotlin.reflect.KFunction<*>)
    }.also {
      assert(it.message.orEmpty().contains("Allowed return types are String, Int, Double, Boolean or Unit"))
    }
  }

  @Test
  fun `verifyIsValidReturnType accepts String return`() {
    val service = ValidService()
    val function = service::class.members.first { it.name == "doWork" }
    // Should not throw
    verifyIsValidReturnType(true, function as kotlin.reflect.KFunction<*>)
  }

  @Test
  fun `verifyIsValidReturnType accepts Unit return`() {
    val service = UnitReturnService()
    val function = service::class.members.first { it.name == "doWork" }
    // Should not throw
    verifyIsValidReturnType(true, function as kotlin.reflect.KFunction<*>)
  }

  @Test
  fun `verifyIsValidReturnType accepts Int return`() {
    val service = IntReturnService()
    val function = service::class.members.first { it.name == "getCount" }
    verifyIsValidReturnType(true, function as kotlin.reflect.KFunction<*>)
  }

  @Test
  fun `verifyIsValidReturnType accepts Boolean return`() {
    val service = BooleanReturnService()
    val function = service::class.members.first { it.name == "isReady" }
    verifyIsValidReturnType(true, function as kotlin.reflect.KFunction<*>)
  }

  @Test
  fun `verifyIsValidReturnType accepts Double return`() {
    val service = DoubleReturnService()
    val function = service::class.members.first { it.name == "getScore" }
    verifyIsValidReturnType(true, function as kotlin.reflect.KFunction<*>)
  }

  @Test
  fun `llmType mapping for String`() {
    String::class.llmType shouldBeEqualTo "string"
  }

  @Test
  fun `llmType mapping for Int`() {
    Int::class.llmType shouldBeEqualTo "integer"
  }

  @Test
  fun `llmType mapping for Double`() {
    Double::class.llmType shouldBeEqualTo "double"
  }

  @Test
  fun `llmType mapping for Boolean`() {
    Boolean::class.llmType shouldBeEqualTo "boolean"
  }

  @Test
  fun `llmType mapping for unknown type`() {
    Any::class.llmType shouldBeEqualTo "object"
  }

  @Test
  fun `allowedParamTypes contains expected types`() {
    FunctionUtils.allowedParamTypes shouldBeEqualTo setOf(String::class, Int::class, Double::class, Boolean::class)
  }
}
