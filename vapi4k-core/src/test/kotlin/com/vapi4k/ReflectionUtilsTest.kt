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
import com.vapi4k.api.vapi4k.RequestContext
import com.vapi4k.utils.ReflectionUtils.hasToolCallAnnotation
import com.vapi4k.utils.ReflectionUtils.isUnitReturnType
import com.vapi4k.utils.ReflectionUtils.parameterSignature
import com.vapi4k.utils.ReflectionUtils.toolCallAnnotation
import com.vapi4k.utils.ReflectionUtils.toolCallFunction
import com.vapi4k.utils.ReflectionUtils.valueParameters
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.reflect.KFunction

class ReflectionUtilsTest : StringSpec() {
  class MultiMethodService {
    fun regularMethod(): String = "hello"

    @ToolCall("Annotated method")
    fun annotatedMethod(name: String): String = name

    fun anotherRegularMethod(): Int = 42
  }

  class UnitMethodService {
    @ToolCall("Unit method")
    fun doWork() {}
  }

  class ContextParamService {
    @ToolCall("With context")
    fun withContext(
      name: String,
      requestContext: RequestContext,
    ): String = name
  }

  private inline fun <reified T : Any> T.memberFunction(name: String): KFunction<*> =
    this::class.members.first { it.name == name } as KFunction<*>

  init {
    "toolCallFunction finds first ToolCall annotated method" {
      val service = MultiMethodService()
      val func = service.toolCallFunction
      func.name shouldBe "annotatedMethod"
    }

    "valueParameters returns only non-instance parameters" {
      val service = MultiMethodService()
      val func = service.memberFunction("annotatedMethod")
      val params = func.valueParameters
      params.size shouldBe 1
      params.first().first shouldBe "name"
    }

    "valueParameters includes RequestContext in parameter set" {
      val service = ContextParamService()
      val func = service.memberFunction("withContext")
      val params = func.valueParameters
      params.size shouldBe 2
      params.map { it.first } shouldBe listOf("name", "requestContext")
    }

    "isUnitReturnType returns true for Unit-returning function" {
      val func = UnitMethodService().memberFunction("doWork")
      func.isUnitReturnType shouldBe true
    }

    "isUnitReturnType returns false for String-returning function" {
      val func = MultiMethodService().memberFunction("annotatedMethod")
      func.isUnitReturnType shouldBe false
    }

    "hasToolCallAnnotation returns true for annotated function" {
      val func = MultiMethodService().memberFunction("annotatedMethod")
      func.hasToolCallAnnotation shouldBe true
    }

    "hasToolCallAnnotation returns false for unannotated function" {
      val func = MultiMethodService().memberFunction("regularMethod")
      func.hasToolCallAnnotation shouldBe false
    }

    "toolCallAnnotation returns annotation for annotated function" {
      val func = MultiMethodService().memberFunction("annotatedMethod")
      val annotation = func.toolCallAnnotation
      annotation?.description shouldBe "Annotated method"
    }

    "parameterSignature formats correctly" {
      val func = MultiMethodService().memberFunction("annotatedMethod")
      func.parameterSignature shouldBe "name: String"
    }
  }
}
