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

import com.github.pambrose.common.json.toJsonElement
import com.vapi4k.common.AssistantId.Companion.toAssistantId
import com.vapi4k.common.SessionId.Companion.toSessionId
import com.vapi4k.dsl.vapi4k.InboundCallApplicationImpl
import com.vapi4k.server.RequestContextImpl
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain

class TransferDestinationTest : StringSpec() {
  private fun createRequestContext(app: InboundCallApplicationImpl): RequestContextImpl =
    RequestContextImpl(
      application = app,
      request = """{"message":{"type":"transfer-destination-request"}}""".toJsonElement(),
      sessionId = "test-session".toSessionId(),
      assistantId = "test-assistant".toAssistantId(),
    )

  init {
    "getTransferDestinationResponse errors when no handler assigned" {
      val app = InboundCallApplicationImpl()
      val requestContext = createRequestContext(app)
      shouldThrow<IllegalStateException> {
        app.getTransferDestinationResponse(requestContext)
      }.message shouldContain "onTransferDestinationRequest{} not called"
    }

    "onTransferDestinationRequest errors on duplicate assignment" {
      val app = InboundCallApplicationImpl()
      app.onTransferDestinationRequest { }
      shouldThrow<IllegalStateException> {
        app.onTransferDestinationRequest { }
      }.message shouldContain "onTransferDestinationRequest{} can be called only once"
    }

    "getTransferDestinationResponse errors when no destination set in handler" {
      val app = InboundCallApplicationImpl()
      app.onTransferDestinationRequest {
        // Deliberately not calling numberDestination{}, sipDestination{}, or assistantDestination{}
      }
      val requestContext = createRequestContext(app)
      shouldThrow<IllegalStateException> {
        app.getTransferDestinationResponse(requestContext)
      }.message shouldContain "missing a call to numberDestination{}"
    }
  }
}
