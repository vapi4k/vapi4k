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

package tools

import com.vapi4k.api.conditions.eq
import com.vapi4k.api.tools.Param
import com.vapi4k.api.tools.ToolCall
import com.vapi4k.api.tools.enums.ToolMessageRoleType
import com.vapi4k.api.toolservice.ToolCallService
import com.vapi4k.api.vapi4k.RequestContext
import com.vapi4k.dsl.assistant.ToolMessageCompleteImpl
import kotlin.math.absoluteValue

object ToolCalls {
  class AddTwoNumbers {
    fun addTwoNumbers(
      a: Int,
      b: Int,
      requestContext: RequestContext, // This is optional
    ): Int {
      return a + b
    }
  }

  class MultiplyTwoNumbers {
    @ToolCall("Multiply two numbers")
    fun add(
      @Param("First number to multiply")
      a: Int,
      @Param("Second number to Multiply")
      b: Int,
    ): Int {
      return a + b
    }
  }

  object AbsoluteValue {
    @ToolCall("Absolute value of a number")
    fun absolute(
      @Param("Number")
      a: Int,
    ): Int {
      return a.absoluteValue
    }
  }

  class WeatherLookupService : ToolCallService() {
    @ToolCall("Look up the weather for a city")
    fun getWeatherByCity(
      city: String,
      state: String,
    ) = "The weather in city $city and state $state is windy"

    @ToolCall("Look up the weather for a zip code")
    fun getWeatherByZipCode(zipCode: String) = "The weather in zip code $zipCode is rainy"

    override fun onToolCallComplete(
      requestContext: RequestContext,
      result: String,
    ): List<ToolMessageCompleteImpl> =
      requestCompleteMessages {
        condition("city" eq "Chicago", "state" eq "Illinois") {
          requestCompleteMessage {
            content = "The Chicago weather lookup override request complete"
          }
        }
        requestCompleteMessage {
          role = ToolMessageRoleType.ASSISTANT
          content = "The generic weather lookup override request complete"
        }
      }
  }
}
