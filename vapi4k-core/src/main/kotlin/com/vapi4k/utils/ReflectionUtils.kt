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

package com.vapi4k.utils

import com.vapi4k.api.tools.Param
import com.vapi4k.api.tools.ToolCall
import com.vapi4k.api.vapi4k.RequestContext
import com.vapi4k.common.Utils.isNotNull
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.declaredFunctions

internal object ReflectionUtils {
  val Any.functions get() = this::class.declaredFunctions
  val Any.toolCallFunction get() = functions.first { it.hasToolCallAnnotation }
  val KParameter.paramAnnotation: Param? get() = annotations.firstOrNull { it is Param } as Param?
  val KParameter.paramAnnotationWithDefault: String get() = paramAnnotation?.description ?: "The $name parameter"
  val KFunction<*>.toolCallAnnotation: ToolCall? get() = annotations.firstOrNull { it is ToolCall } as ToolCall?
  val KFunction<*>.hasToolCallAnnotation get() = toolCallAnnotation.isNotNull()
  val KFunction<*>.isUnitReturnType get() = returnType.asKClass() == Unit::class
  val KFunction<*>.valueParameters: List<Pair<String, KParameter>>
    get() = parameters
      .filter { it.kind == KParameter.Kind.VALUE }
      .map { it.name.orEmpty() to it }

  val KFunction<*>.instanceParameter: KParameter?
    get() = parameters.firstOrNull { it.kind == KParameter.Kind.INSTANCE }

  val KFunction<*>.parameterSignature
    get() =
      valueParameters.joinToString(", ") { (name, param) ->
        "$name: ${param.asKClass().simpleName}"
      }

  fun KType.asKClass() = classifier as KClass<*>

  fun KParameter.asKClass() = type.classifier as KClass<*>

  fun KType.isRequestContextClass() = asKClass() == RequestContext::class

  fun KType.isNotRequestContextClass() = !isRequestContextClass()

  fun KParameter.isRequestContextClass() = asKClass() == RequestContext::class

  fun KParameter.isNotRequestContextClass() = !isRequestContextClass()
}
