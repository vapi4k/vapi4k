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

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.pambrose.common.json.toJsonElement
import com.vapi4k.api.tools.ToolCall
import com.vapi4k.api.toolservice.ToolCallService
import com.vapi4k.api.vapi4k.RequestContext
import com.vapi4k.common.AssistantId.Companion.toAssistantId
import com.vapi4k.common.SessionId.Companion.toSessionId
import com.vapi4k.dsl.functions.FunctionDetails
import com.vapi4k.dsl.functions.ToolCallInfo
import com.vapi4k.dsl.vapi4k.InboundCallApplicationImpl
import com.vapi4k.dtos.tools.CommonToolMessageDto
import com.vapi4k.server.RequestContextImpl
import com.vapi4k.utils.ReflectionUtils.toolCallFunction
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory

class FunctionDetailsTest : StringSpec() {
  class StringService {
    @ToolCall("Returns greeting")
    fun greet(name: String): String = "Hello, $name!"
  }

  class UnitService {
    @ToolCall("Does nothing")
    fun doNothing() {}
  }

  class IntService {
    @ToolCall("Adds numbers")
    fun add(
      a: Int,
      b: Int,
    ): Int = a + b
  }

  class DoubleService {
    @ToolCall("Calculates area")
    fun area(
      width: Double,
      height: Double,
    ): Double = width * height
  }

  class BooleanService {
    @ToolCall("Checks eligibility")
    fun isEligible(
      age: Int,
      hasLicense: Boolean,
    ): Boolean = age >= 18 && hasLicense
  }

  class ThrowingService {
    @ToolCall("Always fails")
    fun fail(): String = throw IllegalArgumentException("Intentional failure")
  }

  class ContextService {
    @ToolCall("Uses request context")
    fun withContext(
      name: String,
      requestContext: RequestContext,
    ): String = "Hello $name from session ${requestContext.sessionId}"
  }

  class CompleteService : ToolCallService() {
    @ToolCall("Weather lookup")
    fun getWeather(city: String): String = "Sunny in $city"

    override fun onToolCallComplete(
      requestContext: RequestContext,
      result: String,
    ) = requestCompleteMessages {
      requestCompleteMessage {
        content = "Complete: $result"
      }
    }
  }

  @Suppress("TooGenericExceptionThrown")
  class FailingToolCallService : ToolCallService() {
    @ToolCall("Always fails")
    fun fail(): String = throw RuntimeException("Service error")

    override fun onToolCallFailed(
      requestContext: RequestContext,
      errorMessage: String,
    ) = requestFailedMessages {
      requestFailedMessage {
        content = "Failed: $errorMessage"
      }
    }
  }

  class SuspendService {
    @ToolCall("Suspends then greets")
    suspend fun slowGreet(name: String): String {
      delay(1)
      return "Hi, $name!"
    }
  }

  // Captures the log lines emitted during [block], keeping only the "...tool service:..." line
  // produced by FunctionDetails.formatArgs() (its only observable surface).
  private suspend fun captureToolServiceLogs(block: suspend () -> Unit): List<String> {
    val rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    val appender = ListAppender<ILoggingEvent>()
    appender.start()
    rootLogger.addAppender(appender)
    return try {
      block()
      appender.list.map { it.formattedMessage }.filter { it.contains("tool service:") }
    } finally {
      rootLogger.detachAppender(appender)
      appender.stop()
    }
  }

  private fun createRequestContext(): RequestContextImpl {
    val app = InboundCallApplicationImpl()
    return RequestContextImpl(
      application = app,
      request = """{"message":{"type":"tool-calls"}}""".toJsonElement(),
      sessionId = "test-session".toSessionId(),
      assistantId = "test-assistant".toAssistantId(),
    )
  }

  private fun createFunctionDetails(obj: Any): FunctionDetails {
    val function = obj.toolCallFunction
    val toolCallInfo = ToolCallInfo("test-assistant".toAssistantId(), function)
    return FunctionDetails(obj, function, toolCallInfo)
  }

  init {
    "invokeToolMethod returns string result for String-returning ToolCall" {
      val service = StringService()
      val details = createFunctionDetails(service)
      val requestContext = createRequestContext()
      val args = """{"name":"World"}""".toJsonElement()
      var result = ""

      details.invokeToolMethod(
        isTool = true,
        requestContext = requestContext,
        invokeArgs = args,
        successAction = { result = it },
        errorAction = { result = it },
      )

      result shouldBe "Hello, World!"
      details.invokeCount shouldBe 1
    }

    "invokeToolMethod returns empty string for Unit-returning ToolCall" {
      val service = UnitService()
      val details = createFunctionDetails(service)
      val requestContext = createRequestContext()
      val args = """{}""".toJsonElement()
      var result = "not-set"

      details.invokeToolMethod(
        isTool = true,
        requestContext = requestContext,
        invokeArgs = args,
        successAction = { result = it },
        errorAction = { result = it },
      )

      result shouldBe ""
    }

    "invokeToolMethod handles Int parameters and return type" {
      val service = IntService()
      val details = createFunctionDetails(service)
      val requestContext = createRequestContext()
      val args = """{"a":3,"b":4}""".toJsonElement()
      var result = ""

      details.invokeToolMethod(
        isTool = true,
        requestContext = requestContext,
        invokeArgs = args,
        successAction = { result = it },
        errorAction = { result = it },
      )

      result shouldBe "7"
    }

    "invokeToolMethod calls errorAction on exception" {
      val service = ThrowingService()
      val details = createFunctionDetails(service)
      val requestContext = createRequestContext()
      val args = """{}""".toJsonElement()
      var errorResult = ""

      details.invokeToolMethod(
        isTool = true,
        requestContext = requestContext,
        invokeArgs = args,
        successAction = {},
        errorAction = { errorResult = it },
      )

      errorResult shouldContain "Intentional failure"
    }

    "invokeToolMethod injects RequestContext when parameter is present" {
      val service = ContextService()
      val details = createFunctionDetails(service)
      val requestContext = createRequestContext()
      val args = """{"name":"Alice"}""".toJsonElement()
      var result = ""

      details.invokeToolMethod(
        isTool = true,
        requestContext = requestContext,
        invokeArgs = args,
        successAction = { result = it },
        errorAction = { result = it },
      )

      result shouldContain "Hello Alice from session test-session"
    }

    "invokeToolMethod calls onToolCallComplete for ToolCallService on success" {
      val service = CompleteService()
      val details = createFunctionDetails(service)
      val requestContext = createRequestContext()
      val args = """{"city":"Boston"}""".toJsonElement()
      val messageDtos = mutableListOf<CommonToolMessageDto>()
      var result = ""

      details.invokeToolMethod(
        isTool = true,
        requestContext = requestContext,
        invokeArgs = args,
        messageDtos = messageDtos,
        successAction = { result = it },
        errorAction = {},
      )

      result shouldBe "Sunny in Boston"
      messageDtos.shouldNotBeEmpty()
    }

    "invokeToolMethod calls onToolCallFailed for ToolCallService on error" {
      val service = FailingToolCallService()
      val details = createFunctionDetails(service)
      val requestContext = createRequestContext()
      val args = """{}""".toJsonElement()
      val messageDtos = mutableListOf<CommonToolMessageDto>()
      var errorResult = ""

      details.invokeToolMethod(
        isTool = true,
        requestContext = requestContext,
        invokeArgs = args,
        messageDtos = messageDtos,
        successAction = {},
        errorAction = { errorResult = it },
      )

      errorResult shouldContain "Service error"
      messageDtos.shouldNotBeEmpty()
    }

    "invokeCount increments on each invocation" {
      val service = StringService()
      val details = createFunctionDetails(service)
      val requestContext = createRequestContext()
      val args = """{"name":"Test"}""".toJsonElement()

      details.invokeCount shouldBe 0

      details.invokeToolMethod(
        isTool = false,
        requestContext = requestContext,
        invokeArgs = args,
        successAction = {},
        errorAction = {},
      )
      details.invokeCount shouldBe 1

      details.invokeToolMethod(
        isTool = false,
        requestContext = requestContext,
        invokeArgs = args,
        successAction = {},
        errorAction = {},
      )
      details.invokeCount shouldBe 2
    }

    "fqName includes class and function name" {
      val service = StringService()
      val details = createFunctionDetails(service)
      details.fqName shouldContain "StringService"
      details.fqName shouldContain "greet"
    }

    "invokeToolMethod handles Double parameters and return type" {
      val service = DoubleService()
      val details = createFunctionDetails(service)
      val requestContext = createRequestContext()
      val args = """{"width":3.5,"height":2.0}""".toJsonElement()
      var result = ""

      details.invokeToolMethod(
        isTool = true,
        requestContext = requestContext,
        invokeArgs = args,
        successAction = { result = it },
        errorAction = { result = it },
      )

      result shouldBe "7.0"
    }

    "invokeToolMethod handles Boolean parameters and return type" {
      val service = BooleanService()
      val details = createFunctionDetails(service)
      val requestContext = createRequestContext()
      val args = """{"age":21,"hasLicense":true}""".toJsonElement()
      var result = ""

      details.invokeToolMethod(
        isTool = true,
        requestContext = requestContext,
        invokeArgs = args,
        successAction = { result = it },
        errorAction = { result = it },
      )

      result shouldBe "true"
    }

    "invokeToolMethod with Boolean false result" {
      val service = BooleanService()
      val details = createFunctionDetails(service)
      val requestContext = createRequestContext()
      val args = """{"age":15,"hasLicense":false}""".toJsonElement()
      var result = ""

      details.invokeToolMethod(
        isTool = true,
        requestContext = requestContext,
        invokeArgs = args,
        successAction = { result = it },
        errorAction = { result = it },
      )

      result shouldBe "false"
    }

    "invokeToolMethod handles suspend ToolCall functions" {
      val service = SuspendService()
      val details = createFunctionDetails(service)
      val requestContext = createRequestContext()
      val args = """{"name":"World"}""".toJsonElement()
      var result = ""

      details.invokeToolMethod(
        isTool = true,
        requestContext = requestContext,
        invokeArgs = args,
        successAction = { result = it },
        errorAction = { result = it },
      )

      result shouldBe "Hi, World!"
    }

    "invokeToolMethod calls errorAction when a JSON arg has no matching parameter" {
      val service = StringService()
      val details = createFunctionDetails(service)
      val requestContext = createRequestContext()
      val args = """{"unknown":"x"}""".toJsonElement()
      var errorResult = ""

      details.invokeToolMethod(
        isTool = true,
        requestContext = requestContext,
        invokeArgs = args,
        successAction = {},
        errorAction = { errorResult = it },
      )

      errorResult shouldContain "Parameter unknown not found"
    }

    "logs 'with no args' for a zero-arg tool" {
      val details = createFunctionDetails(UnitService())
      val requestContext = createRequestContext()

      val logs =
        captureToolServiceLogs {
          details.invokeToolMethod(
            isTool = true,
            requestContext = requestContext,
            invokeArgs = """{}""".toJsonElement(),
            successAction = {},
            errorAction = {},
          )
        }

      logs.single { it.contains("doNothing(") } shouldContain "doNothing(with no args)"
    }

    "logs numeric arguments without quotes" {
      val details = createFunctionDetails(IntService())
      val requestContext = createRequestContext()

      val logs =
        captureToolServiceLogs {
          details.invokeToolMethod(
            isTool = true,
            requestContext = requestContext,
            invokeArgs = """{"a":3,"b":4}""".toJsonElement(),
            successAction = {},
            errorAction = {},
          )
        }

      logs.single { it.contains("add(") } shouldContain "add(a: 3, b: 4)"
    }

    "logs string arguments quoted and the injected RequestContext label" {
      val details = createFunctionDetails(ContextService())
      val requestContext = createRequestContext()

      val logs =
        captureToolServiceLogs {
          details.invokeToolMethod(
            isTool = true,
            requestContext = requestContext,
            invokeArgs = """{"name":"Alice"}""".toJsonElement(),
            successAction = {},
            errorAction = {},
          )
        }

      val line = logs.single { it.contains("withContext(") }
      line shouldContain "name: \"Alice\""
      line shouldContain "requestContext: RequestContext Value"
    }
  }
}
