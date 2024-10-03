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

import com.vapi4k.api.functions.Functions
import com.vapi4k.dsl.functions.FunctionUtils.populateFunctionDto
import com.vapi4k.dsl.functions.FunctionUtils.verifyIsToolCall
import com.vapi4k.dsl.functions.FunctionUtils.verifyIsValidReturnType
import com.vapi4k.dsl.functions.FunctionUtils.verifyObjectHasOnlyOneToolCall
import com.vapi4k.dsl.model.AbstractModelImpl
import com.vapi4k.dsl.vapi4k.AbstractApplicationImpl
import com.vapi4k.dtos.functions.FunctionDto
import com.vapi4k.utils.ReflectionUtils.toolCallFunction
import kotlin.reflect.KFunction

class FunctionsImpl internal constructor(
  internal val model: AbstractModelImpl,
) : Functions {
  override fun function(
    obj: Any,
    vararg functions: KFunction<*>,
  ) {
    val app = model.application
    if (functions.isEmpty()) {
      verifyObjectHasOnlyOneToolCall(obj)
      val function = obj.toolCallFunction
      verifyIsValidReturnType(false, function)
      addFunction(app, obj, function)
    } else {
      functions.forEach { function ->
        verifyIsToolCall(false, function)
        verifyIsValidReturnType(false, function)
        addFunction(app, obj, function)
      }
    }
  }

  private fun addFunction(
    application: AbstractApplicationImpl,
    obj: Any,
    function: KFunction<*>,
  ) {
    model.functionDtos +=
      FunctionDto().also { functionDto ->
        populateFunctionDto(model, obj, function, functionDto)
        application.addFunctionToCache(model, obj, function)
      }.also { func ->
        if (model.functionDtos.any { func.name == it.name }) {
          error("Duplicate function name declared: ${func.name}")
        }
      }
  }
}
