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

package com.vapi4k.dsl.functions

import com.vapi4k.common.AssistantId
import com.vapi4k.common.FunctionName
import com.vapi4k.common.SessionId
import com.vapi4k.dsl.functions.FunctionDetails.FunctionDetailsDto
import com.vapi4k.dsl.functions.FunctionDetails.FunctionDetailsDto.Companion.toFunctionDetailsDto
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.DurationUnit

class FunctionInfo internal constructor(
  val sessionId: SessionId,
  val assistantId: AssistantId,
) {
  private val created: Instant = Clock.System.now()
  private val functions = ConcurrentHashMap<FunctionName, FunctionDetails>()

  internal val age get() = Clock.System.now() - created
  internal val ageSecs get() = age.toString(unit = DurationUnit.SECONDS)
  internal val size get() = functions.size

  internal fun containsFunction(funcName: FunctionName) = functions.containsKey(funcName)

  internal fun addFunction(
    funcName: FunctionName,
    funcDetails: FunctionDetails,
  ) {
    functions[funcName] = funcDetails
  }

  internal fun getFunctionOrNull(funcName: FunctionName) = functions[funcName]

  internal fun getFunction(funcName: FunctionName) =
    getFunctionOrNull(funcName) ?: error("Function not found: \"$funcName\"")

  fun toFunctionInfoDto() =
    FunctionInfoDto(
      created.toString(),
      age.toString(),
      functions.mapValues { it.value.toFunctionDetailsDto() },
    )

  override fun toString() = "FunctionInfo(age=$age, functions=$functions)"
}

@Serializable
class FunctionInfoDto(
  val created: String = "",
  val age: String = "",
  val functions: Map<FunctionName, FunctionDetailsDto>? = null,
)
