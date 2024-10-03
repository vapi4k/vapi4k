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

import com.vapi4k.api.prompt.Prompt.Companion.prompt
import kotlin.test.Test
import kotlin.test.assertEquals

class StringTest {
  @Test
  fun `unaryPlus single test`() {
    val str =
      prompt {
        +"test text"
      }

    assertEquals("test text", str)
  }

  @Test
  fun `unaryPlus multi2a test`() {
    val str =
      prompt {
        +"test text"
        +"test text"
      }

    val goal =
      """test text

test text"""

    assertEquals(goal, str)
  }

  @Test
  fun `unaryPlus multi2b test`() {
    val str =
      prompt {
        +"""test
text"""
        +"""test
text"""
      }

    val goal =
      """test
text

test
text"""

    assertEquals(goal, str)
  }

  @Test
  fun `unaryPlus multi3 test`() {
    val str =
      prompt {
        +"test text"
        +"test text"
        +"test text"
      }

    val goal =
      """test text

test text

test text"""

    assertEquals(goal, str)
  }

  @Test
  fun `singleLine no cr test`() {
    val str =
      prompt {
        singleLine(
          """Hello
         team
         later
      """,
        )
      }

    val goal = "Hello team later"

    assertEquals(goal, str)
  }

  @Test
  fun `singleLine single with cr test`() {
    val str =
      prompt {
        singleLine(
          """Welcome
            everyone

         team
         later
      """,
        )
      }

    val goal = """Welcome everyone

team later"""
    assertEquals(goal, str)
  }

  @Test
  fun `singleLine multi with cr test`() {
    val str =
      prompt {
        singleLine(
          """Welcome
            everyone

         team
         later
      """,
        )

        singleLine(
          """Welcome
            everyone

         team
         later
      """,
        )
      }

    val goal = """Welcome everyone

team later

Welcome everyone

team later"""

    assertEquals(goal, str)
  }

  @Test
  fun `singleLine varargs multi with cr test`() {
    val str =
      prompt {
        singleLine(
          """Welcome
            everyone

         team
         later
      """,
          """Welcome
            everyone

         team
         later
      """,
        )
      }

    val goal = """Welcome everyone

team later

Welcome everyone

team later"""

    assertEquals(goal, str)
  }

  @Test
  fun `trimPrefix single line test`() {
    val str =
      prompt {
        trimPrefix("    test text")
      }

    assertEquals("test text", str)
  }

  @Test
  fun `trimPrefix single multi line test`() {
    val str =
      prompt {
        trimPrefix(
          """Welcome
         team
         later""",
        )
      }
    val goal = """Welcome
team
later"""
    assertEquals(goal, str)
  }

  @Test
  fun `trimPrefix multi multi line test`() {
    val str =
      prompt {
        trimPrefix(
          """Welcome
         team
         later""",
        )

        trimPrefix(
          """Welcome
         team
         later""",
        )
      }
    val goal = """Welcome
team
later

Welcome
team
later"""
    assertEquals(goal, str)
  }

  @Test
  fun `trimPrefix varargs multi line test`() {
    val str =
      prompt {
        trimPrefix(
          """Welcome
         team
         later""",
          """Welcome
         team
         later""",
        )
      }
    val goal = """Welcome
team
later

Welcome
team
later"""
    assertEquals(goal, str)
  }
}
