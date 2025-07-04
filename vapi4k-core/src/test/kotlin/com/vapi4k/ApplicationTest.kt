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
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test

class ApplicationTest {
  @Test
  fun `test for serverPath and serverSecret`() {
    val str = "/something_else"
    val app =
      with(Vapi4kConfigImpl()) {
        inboundCallApplication {
          serverPath = str
          serverSecret = "12345"
        }
      }
    app.serverPath shouldBeEqualTo str
    app.serverSecret shouldBeEqualTo "12345"
  }

  @Test
  fun `test for default serverPath`() {
    val app =
      with(Vapi4kConfigImpl()) {
        inboundCallApplication {
        }
      }
    app.serverPath shouldBeEqualTo defaultServerPath.removePrefix("/")
    app.serverSecret shouldBeEqualTo ""
  }

  @Test
  fun `test for duplicate default serverPaths`() {
    val str = "/something_else"
    assertThrows(IllegalStateException::class.java) {
      with(Vapi4kConfigImpl()) {
        inboundCallApplication {
        }
        inboundCallApplication {
        }
      }
    }.also {
      it.message.orEmpty().contains("already exists") shouldBeEqualTo true
    }
  }

  @Test
  fun `test for duplicate serverPaths`() {
    val str = "/something_else"
    assertThrows(IllegalStateException::class.java) {
      with(Vapi4kConfigImpl()) {
        inboundCallApplication {
          serverPath = str
        }
        inboundCallApplication {
          serverPath = str
        }
      }
    }.also {
      it.message.orEmpty().contains("already exists") shouldBeEqualTo true
    }
  }
}
