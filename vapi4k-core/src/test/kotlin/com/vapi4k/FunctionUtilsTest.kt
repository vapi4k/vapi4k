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
import org.amshove.kluent.shouldContain
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.reflect.KFunction
import kotlin.test.Test

class FunctionUtilsTest {
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

  private inline fun <reified T : Any> T.memberFunction(name: String): KFunction<*> =
    this::class.members.first { it.name == name } as KFunction<*>

  @Test
  fun `verifyIsToolCall rejects unannotated function`() {
    val function = NoAnnotationService().memberFunction("doSomething")
    assertThrows(IllegalStateException::class.java) {
      verifyIsToolCall(true, function)
    }.also {
      it.message.orEmpty() shouldContain "missing @ToolCall annotation"
    }
  }

  @Test
  fun `verifyObjectHasOnlyOneToolCall rejects zero annotations`() {
    assertThrows(IllegalStateException::class.java) {
      verifyObjectHasOnlyOneToolCall(NoAnnotationService())
    }.also {
      it.message.orEmpty() shouldContain "No method with @ToolCall annotation found"
    }
  }

  @Test
  fun `verifyObjectHasOnlyOneToolCall rejects two annotations`() {
    assertThrows(IllegalStateException::class.java) {
      verifyObjectHasOnlyOneToolCall(TwoAnnotationService())
    }.also {
      it.message.orEmpty() shouldContain "Only one method with @ToolCall annotation is allowed"
    }
  }

  @Test
  fun `verifyObjectHasOnlyOneToolCall accepts valid service`() {
    verifyObjectHasOnlyOneToolCall(ValidService())
  }

  @Test
  fun `verifyIsValidReturnType rejects List return`() {
    val function = ListReturnService().memberFunction("getItems")
    assertThrows(IllegalStateException::class.java) {
      verifyIsValidReturnType(true, function)
    }.also {
      it.message.orEmpty() shouldContain "Allowed return types are String, Int, Double, Boolean or Unit"
    }
  }

  @Test
  fun `verifyIsValidReturnType accepts String return`() {
    verifyIsValidReturnType(true, ValidService().memberFunction("doWork"))
  }

  @Test
  fun `verifyIsValidReturnType accepts Unit return`() {
    verifyIsValidReturnType(true, UnitReturnService().memberFunction("doWork"))
  }

  @Test
  fun `verifyIsValidReturnType accepts Int return`() {
    verifyIsValidReturnType(true, IntReturnService().memberFunction("getCount"))
  }

  @Test
  fun `verifyIsValidReturnType accepts Boolean return`() {
    verifyIsValidReturnType(true, BooleanReturnService().memberFunction("isReady"))
  }

  @Test
  fun `verifyIsValidReturnType accepts Double return`() {
    verifyIsValidReturnType(true, DoubleReturnService().memberFunction("getScore"))
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
