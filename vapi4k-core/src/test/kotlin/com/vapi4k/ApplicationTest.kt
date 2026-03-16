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

import com.vapi4k.common.CoreEnvVars.defaultServerPath
import com.vapi4k.dsl.vapi4k.Vapi4kConfigImpl
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class ApplicationTest : StringSpec() {
  init {
    "test for serverPath and serverSecret" {
      val str = "/something_else"
      val app =
        with(Vapi4kConfigImpl()) {
          inboundCallApplication {
            serverPath = str
            serverSecret = "12345"
          }
        }
      app.serverPath shouldBe str
      app.serverSecret shouldBe "12345"
    }

    "test for default serverPath" {
      val app =
        with(Vapi4kConfigImpl()) {
          inboundCallApplication {
          }
        }
      app.serverPath shouldBe defaultServerPath.removePrefix("/")
      app.serverSecret shouldBe ""
    }

    "test for duplicate default serverPaths" {
      val str = "/something_else"
      shouldThrow<IllegalStateException> {
        with(Vapi4kConfigImpl()) {
          inboundCallApplication {
          }
          inboundCallApplication {
          }
        }
      }.message shouldContain "already exists"
    }

    "test for duplicate serverPaths" {
      val str = "/something_else"
      shouldThrow<IllegalStateException> {
        with(Vapi4kConfigImpl()) {
          inboundCallApplication {
            serverPath = str
          }
          inboundCallApplication {
            serverPath = str
          }
        }
      }.message shouldContain "already exists"
    }
  }
}
