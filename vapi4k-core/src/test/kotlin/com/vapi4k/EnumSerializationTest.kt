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

import com.vapi4k.api.assistant.BackgroundSoundType
import com.vapi4k.api.assistant.FirstMessageModeType
import com.vapi4k.api.model.AnthropicModelType
import com.vapi4k.api.model.GroqModelType
import com.vapi4k.api.model.OpenAIModelType
import com.vapi4k.api.transcriber.DeepgramLanguageType
import com.vapi4k.api.voice.AzureVoiceIdType
import com.vapi4k.api.voice.CartesiaVoiceModelType
import com.vapi4k.api.voice.DeepGramVoiceIdType
import com.vapi4k.api.voice.ElevenLabsVoiceIdType
import com.vapi4k.api.voice.LMNTVoiceIdType
import com.vapi4k.api.voice.NeetsVoiceIdType
import com.vapi4k.api.voice.OpenAIVoiceIdType
import com.vapi4k.api.voice.PlayHTVoiceIdType
import com.vapi4k.api.voice.RimeAIVoiceIdType
import com.vapi4k.common.Constants.UNSPECIFIED_DEFAULT
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EnumSerializationTest : StringSpec() {
  private fun <T> assertUniqueDescs(
    entries: List<T>,
    descExtractor: (T) -> String,
  ) {
    val descs = entries.map(descExtractor)
    descs.size shouldBe descs.toSet().size
  }

  init {
    "OpenAIModelType entries have unique desc values" {
      assertUniqueDescs(OpenAIModelType.entries) { it.desc }
    }

    "AnthropicModelType entries have unique desc values" {
      assertUniqueDescs(AnthropicModelType.entries) { it.desc }
    }

    "GroqModelType entries have unique desc values" {
      assertUniqueDescs(GroqModelType.entries) { it.desc }
    }

    "ElevenLabsVoiceIdType entries have unique desc values" {
      assertUniqueDescs(ElevenLabsVoiceIdType.entries) { it.desc }
    }

    "PlayHTVoiceIdType entries have unique desc values" {
      assertUniqueDescs(PlayHTVoiceIdType.entries) { it.desc }
    }

    "DeepGramVoiceIdType entries have unique desc values" {
      assertUniqueDescs(DeepGramVoiceIdType.entries) { it.desc }
    }

    "OpenAIVoiceIdType entries have unique desc values" {
      assertUniqueDescs(OpenAIVoiceIdType.entries) { it.desc }
    }

    "AzureVoiceIdType entries have unique desc values" {
      assertUniqueDescs(AzureVoiceIdType.entries) { it.desc }
    }

    "LMNTVoiceIdType entries have unique desc values" {
      assertUniqueDescs(LMNTVoiceIdType.entries) { it.desc }
    }

    "RimeAIVoiceIdType entries have unique desc values" {
      assertUniqueDescs(RimeAIVoiceIdType.entries) { it.desc }
    }

    "NeetsVoiceIdType entries have unique desc values" {
      assertUniqueDescs(NeetsVoiceIdType.entries) { it.desc }
    }

    "OpenAIModelType UNSPECIFIED has correct desc" {
      OpenAIModelType.UNSPECIFIED.desc shouldBe UNSPECIFIED_DEFAULT
    }

    "OpenAIModelType isSpecified returns false for UNSPECIFIED" {
      OpenAIModelType.UNSPECIFIED.isSpecified() shouldBe false
    }

    "OpenAIModelType isNotSpecified returns true for UNSPECIFIED" {
      OpenAIModelType.UNSPECIFIED.isNotSpecified() shouldBe true
    }

    "OpenAIModelType isSpecified returns true for real entry" {
      OpenAIModelType.GPT_4O.isSpecified() shouldBe true
    }

    "CartesiaVoiceModelType entries have unique desc values" {
      assertUniqueDescs(CartesiaVoiceModelType.entries) { it.desc }
    }

    "DeepgramLanguageType entries have unique desc values" {
      assertUniqueDescs(DeepgramLanguageType.entries) { it.desc }
    }

    "BackgroundSoundType entries have unique desc values" {
      assertUniqueDescs(BackgroundSoundType.entries) { it.desc }
    }

    "BackgroundSoundType UNSPECIFIED has correct desc" {
      BackgroundSoundType.UNSPECIFIED.desc shouldBe UNSPECIFIED_DEFAULT
    }

    "FirstMessageModeType entries have unique desc values" {
      assertUniqueDescs(FirstMessageModeType.entries) { it.desc }
    }

    "FirstMessageModeType UNSPECIFIED has correct desc" {
      FirstMessageModeType.UNSPECIFIED.desc shouldBe UNSPECIFIED_DEFAULT
    }

    "FirstMessageModeType isSpecified returns false for UNSPECIFIED" {
      FirstMessageModeType.UNSPECIFIED.isSpecified() shouldBe false
    }

    "FirstMessageModeType isSpecified returns true for real entry" {
      FirstMessageModeType.ASSISTANT_SPEAKS_FIRST.isSpecified() shouldBe true
    }

    "CartesiaVoiceModelType UNSPECIFIED has correct desc" {
      CartesiaVoiceModelType.UNSPECIFIED.desc shouldBe UNSPECIFIED_DEFAULT
    }

    "CartesiaVoiceModelType isSpecified returns true for real entry" {
      CartesiaVoiceModelType.SONIC.isSpecified() shouldBe true
    }

    "DeepgramLanguageType isSpecified returns true for ENGLISH" {
      DeepgramLanguageType.ENGLISH.isSpecified() shouldBe true
    }

    "DeepgramLanguageType isNotSpecified returns true for UNSPECIFIED" {
      DeepgramLanguageType.UNSPECIFIED.isNotSpecified() shouldBe true
    }
  }
}
