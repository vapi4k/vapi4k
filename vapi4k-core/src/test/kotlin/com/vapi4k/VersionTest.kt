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

import com.vapi4k.common.Constants.UNKNOWN
import com.vapi4k.common.Version
import com.vapi4k.common.Version.Companion.version
import com.vapi4k.common.Version.Companion.versionDesc
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class VersionTest : StringSpec() {
  @Version(version = "1.0.0", releaseDate = "2026-01-01", buildTime = 1735689600000L)
  class AnnotatedClass

  class UnannotatedClass

  init {
    "version extracts version string from annotated class" {
      AnnotatedClass::class.version() shouldBe "1.0.0"
    }

    "version returns UNKNOWN for unannotated class" {
      UnannotatedClass::class.version() shouldBe UNKNOWN
    }

    "versionDesc returns plain text by default" {
      val desc = AnnotatedClass::class.versionDesc()
      desc shouldContain "Version: 1.0.0"
      desc shouldContain "Release Date: 2026-01-01"
    }

    "versionDesc returns JSON when asJson is true" {
      val json = AnnotatedClass::class.versionDesc(asJson = true)
      json shouldContain "\"version\""
      json shouldContain "1.0.0"
      json shouldContain "\"release_date\""
    }

    "versionDesc for unannotated class returns UNKNOWN in plain text" {
      val desc = UnannotatedClass::class.versionDesc()
      desc shouldContain UNKNOWN
    }

    "versionDesc for unannotated class returns UNKNOWN in JSON" {
      val json = UnannotatedClass::class.versionDesc(asJson = true)
      json shouldContain UNKNOWN
    }
  }
}
