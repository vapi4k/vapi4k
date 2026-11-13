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

import com.vapi4k.api.transcriber.TranscriberType
import com.vapi4k.dtos.api.destination.CommonDestinationDto
import com.vapi4k.dtos.api.destination.DestinationSerializer
import com.vapi4k.dtos.transcriber.CommonTranscriberDto
import com.vapi4k.dtos.transcriber.TranscriberSerializer
import com.vapi4k.dtos.voice.CommonVoiceDto
import com.vapi4k.dtos.voice.VoiceSerializer
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * The provider serializers throw [SerializationException] for a DTO class they have no serializer for.
 * kotlinx-serialization passes [SerializationException] through unchanged (1.12.0 wrapped any other exception
 * in a `JsonEncodingException`; 1.13.0 reverted that), so these tests pin the exact type and message.
 * [ModelSerializerTest] covers `ModelSerializer`.
 */
class ProviderSerializerErrorTest : StringSpec() {
  init {
    "unregistered CommonVoiceDto throws Invalid voice provider" {
      shouldThrowExactly<SerializationException> {
        Json.encodeToString(VoiceSerializer, FakeVoiceDto())
      }.message shouldBe "Invalid voice provider: FakeVoiceDto"
    }

    "unregistered CommonTranscriberDto throws Invalid transcriber provider" {
      shouldThrowExactly<SerializationException> {
        Json.encodeToString(TranscriberSerializer, FakeTranscriberDto())
      }.message shouldBe "Invalid transcriber provider: FakeTranscriberDto"
    }

    "unregistered CommonDestinationDto throws Invalid destination provider" {
      shouldThrowExactly<SerializationException> {
        Json.encodeToString(DestinationSerializer, FakeDestinationDto())
      }.message shouldBe "Invalid destination provider: FakeDestinationDto"
    }
  }

  private class FakeVoiceDto : CommonVoiceDto {
    override fun verifyValues() {}
  }

  private class FakeTranscriberDto : CommonTranscriberDto {
    override val provider = TranscriberType.DEEPGRAM
  }

  private class FakeDestinationDto : CommonDestinationDto
}
