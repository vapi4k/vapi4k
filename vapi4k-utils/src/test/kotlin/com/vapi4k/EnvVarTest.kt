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

import com.vapi4k.envvar.EnvVar
import com.vapi4k.envvar.EnvVar.Companion.getWithDefault
import com.vapi4k.envvar.EnvVar.Companion.isDefined
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class EnvVarTest : StringSpec() {
  init {
    "getEnv returns default when env var is not set" {
      val envVar = EnvVar(
        name = "TEST_ENVVAR_NONEXISTENT_${System.nanoTime()}",
        src = getWithDefault("fallback-value"),
        reportOnBoot = false,
      )
      envVar.value shouldBe "fallback-value"
    }

    "getEnvOrNull returns null when env var is not set" {
      val envVar = EnvVar(
        name = "TEST_ENVVAR_MISSING_${System.nanoTime()}",
        src = getWithDefault("default"),
        reportOnBoot = false,
      )
      envVar.getEnvOrNull() shouldBe null
    }

    "required EnvVar throws on missing environment variable" {
      shouldThrow<IllegalStateException> {
        EnvVar(
          name = "TEST_REQUIRED_MISSING_${System.nanoTime()}",
          src = { getRequired() },
          required = true,
          reportOnBoot = false,
        )
      }.message shouldContain "Missing"
    }

    "toBoolean converts value correctly" {
      val envVar = EnvVar(
        name = "TEST_BOOL_${System.nanoTime()}",
        src = getWithDefault(true),
        reportOnBoot = false,
      )
      envVar.toBoolean() shouldBe true
    }

    "toInt converts value correctly" {
      val envVar = EnvVar(
        name = "TEST_INT_${System.nanoTime()}",
        src = getWithDefault(42),
        reportOnBoot = false,
      )
      envVar.toInt() shouldBe 42
    }

    "isDefined returns false for unset environment variable" {
      "TEST_UNDEFINED_VAR_${System.nanoTime()}".isDefined() shouldBe false
    }

    "isDefined returns true for PATH which is always set" {
      "PATH".isDefined() shouldBe true
    }

    "toString returns the value" {
      val envVar = EnvVar(
        name = "TEST_TOSTRING_${System.nanoTime()}",
        src = getWithDefault("hello"),
        reportOnBoot = false,
      )
      envVar.toString() shouldBe "hello"
    }

    "obfuscate masks characters at given frequency" {
      val maskFunc = EnvVar.obfuscate(2)
      maskFunc("abcdef") shouldBe "*b*d*f"
    }

    "getEnv with boolean default returns default when not set" {
      val envVar = EnvVar(
        name = "TEST_BOOL_DEFAULT_${System.nanoTime()}",
        src = getWithDefault(false),
        reportOnBoot = false,
      )
      envVar.getEnv(true) shouldBe true
    }

    "getEnv with int default returns default when not set" {
      val envVar = EnvVar(
        name = "TEST_INT_DEFAULT_${System.nanoTime()}",
        src = getWithDefault(99),
        reportOnBoot = false,
      )
      envVar.getEnv(100) shouldBe 100
    }
  }
}
