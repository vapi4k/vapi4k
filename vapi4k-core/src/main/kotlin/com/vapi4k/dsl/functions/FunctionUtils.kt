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

package com.vapi4k.dsl.functions

import com.vapi4k.api.tools.ToolCall
import com.vapi4k.api.vapi4k.RequestContext
import com.vapi4k.dsl.model.AbstractModelProperties
import com.vapi4k.dtos.functions.FunctionDto
import com.vapi4k.dtos.functions.FunctionPropertyDescDto
import com.vapi4k.utils.ReflectionUtils.asKClass
import com.vapi4k.utils.ReflectionUtils.functions
import com.vapi4k.utils.ReflectionUtils.hasToolCallAnnotation
import com.vapi4k.utils.ReflectionUtils.isNotRequestContextClass
import com.vapi4k.utils.ReflectionUtils.isRequestContextClass
import com.vapi4k.utils.ReflectionUtils.paramAnnotationWithDefault
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

internal object FunctionUtils {
  internal val allowedParamTypes = setOf(String::class, Int::class, Double::class, Boolean::class)
  private val allowedToolParamTypes =
    setOf(String::class, Int::class, Double::class, Boolean::class, RequestContext::class)
  private val allowedReturnTypes = setOf(String::class, Int::class, Double::class, Boolean::class, Unit::class)
  private val tcName by lazy { ToolCall::class.simpleName.orEmpty() }

  fun verifyIsToolCall(
    isTool: Boolean,
    function: KFunction<*>,
  ) {
    if (!function.hasToolCallAnnotation) {
      val str = if (isTool) "Tool" else "Function"
      error("$str ${function.name} is missing @$tcName annotation")
    }
  }

  fun verifyObjectHasOnlyOneToolCall(obj: Any) {
    val cnt = obj.functions.count { it.hasToolCallAnnotation }
    when {
      cnt == 0 -> error("No method with @$tcName annotation found in class ${obj::class.qualifiedName}")
      cnt > 1 -> error("Only one method with @$tcName annotation is allowed in class ${obj::class.qualifiedName}")
    }
  }

  fun verifyIsValidReturnType(
    isTool: Boolean,
    function: KFunction<*>,
  ) {
    val returnClass = function.returnType.asKClass()
    if (returnClass !in allowedReturnTypes) {
      val str = if (isTool) "Tool" else "Function"
      error(
        "$str ${function.name} returns a ${returnClass.qualifiedName}. " +
          "Allowed return types are String, Int, Double, Boolean or Unit",
      )
    }
  }

  fun populateFunctionDto(
    model: AbstractModelProperties,
    obj: Any,
    function: KFunction<*>,
    dto: FunctionDto,
  ) {
    ToolCallInfo(model.assistantId, function).also { toolCallInfo ->
      dto.name = toolCallInfo.llmName.value
      dto.description = toolCallInfo.llmDescription

      if (function.parameters.count { it.type.isRequestContextClass() } > 1)
        error("Function ${function.name} may have only one parameter of type RequestContext")

      function.parameters
        .filter { it.kind == KParameter.Kind.VALUE }
        .filter { it.type.isNotRequestContextClass() }
        .forEach { param ->
          val kclass = param.asKClass()
          if (kclass !in allowedToolParamTypes) {
            val fqName = FunctionDetails(obj, function, toolCallInfo).fqName
            val simpleName = kclass.simpleName
            error(
              "Parameter \"${param.name}\" in $fqName is a $simpleName. Allowed types are " +
                "String, Int, Double, Boolean, and RequestContext",
            )
          }

          val name = param.name ?: error("Parameter name is null")
          if (!param.isOptional)
            dto.parametersDto.required += name
          dto.parametersDto.properties[name] = FunctionPropertyDescDto(
            type = param.type.asKClass().llmType,
            description = param.paramAnnotationWithDefault,
          )
        }
    }
  }

  internal val KClass<*>.llmType: String
    get() = when (this) {
      String::class -> "string"
      Int::class -> "integer"
      Double::class -> "double"
      Boolean::class -> "boolean"
      else -> "object"
    }
}
