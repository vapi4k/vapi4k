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
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlin.reflect.KFunction

class FunctionUtilsTest : StringSpec() {
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

  init {
    "verifyIsToolCall rejects unannotated function" {
      val function = NoAnnotationService().memberFunction("doSomething")
      shouldThrow<IllegalStateException> {
        verifyIsToolCall(true, function)
      }.message shouldContain "missing @ToolCall annotation"
    }

    "verifyObjectHasOnlyOneToolCall rejects zero annotations" {
      shouldThrow<IllegalStateException> {
        verifyObjectHasOnlyOneToolCall(NoAnnotationService())
      }.message shouldContain "No method with @ToolCall annotation found"
    }

    "verifyObjectHasOnlyOneToolCall rejects two annotations" {
      shouldThrow<IllegalStateException> {
        verifyObjectHasOnlyOneToolCall(TwoAnnotationService())
      }.message shouldContain "Only one method with @ToolCall annotation is allowed"
    }

    "verifyObjectHasOnlyOneToolCall accepts valid service" {
      verifyObjectHasOnlyOneToolCall(ValidService())
    }

    "verifyIsValidReturnType rejects List return" {
      val function = ListReturnService().memberFunction("getItems")
      shouldThrow<IllegalStateException> {
        verifyIsValidReturnType(true, function)
      }.message shouldContain "Allowed return types are String, Int, Double, Boolean or Unit"
    }

    "verifyIsValidReturnType accepts String return" {
      verifyIsValidReturnType(true, ValidService().memberFunction("doWork"))
    }

    "verifyIsValidReturnType accepts Unit return" {
      verifyIsValidReturnType(true, UnitReturnService().memberFunction("doWork"))
    }

    "verifyIsValidReturnType accepts Int return" {
      verifyIsValidReturnType(true, IntReturnService().memberFunction("getCount"))
    }

    "verifyIsValidReturnType accepts Boolean return" {
      verifyIsValidReturnType(true, BooleanReturnService().memberFunction("isReady"))
    }

    "verifyIsValidReturnType accepts Double return" {
      verifyIsValidReturnType(true, DoubleReturnService().memberFunction("getScore"))
    }

    "llmType mapping for String" {
      String::class.llmType shouldBe "string"
    }

    "llmType mapping for Int" {
      Int::class.llmType shouldBe "integer"
    }

    "llmType mapping for Double" {
      Double::class.llmType shouldBe "double"
    }

    "llmType mapping for Boolean" {
      Boolean::class.llmType shouldBe "boolean"
    }

    "llmType mapping for unknown type" {
      Any::class.llmType shouldBe "object"
    }

    "allowedParamTypes contains expected types" {
      FunctionUtils.allowedParamTypes shouldBe setOf(String::class, Int::class, Double::class, Boolean::class)
    }
  }
}
