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

import com.github.pambrose.common.json.stringValue
import com.github.pambrose.common.json.toJsonElement
import com.vapi4k.AssistantTest.Companion.newRequestContext
import com.vapi4k.api.model.GroqModelType
import com.vapi4k.api.voice.AzureVoiceIdType
import com.vapi4k.api.voice.DeepGramVoiceIdType
import com.vapi4k.api.voice.ElevenLabsVoiceIdType
import com.vapi4k.api.voice.ElevenLabsVoiceModelType
import com.vapi4k.api.voice.LMNTVoiceIdType
import com.vapi4k.api.voice.NeetsVoiceIdType
import com.vapi4k.api.voice.OpenAIVoiceIdType
import com.vapi4k.api.voice.RimeAIVoiceIdType
import com.vapi4k.api.voice.RimeAIVoiceModelType
import com.vapi4k.dsl.vapi4k.Vapi4kConfigImpl
import com.vapi4k.utils.assistantResponse
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test

class VoiceProviderTest {
  init {
    Vapi4kConfigImpl()
  }

  @Test
  fun `elevenLabs voice serializes provider and voiceId`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          groqModel {
            modelType = GroqModelType.LLAMA3_8B_8192
          }
          elevenLabsVoice {
            voiceIdType = ElevenLabsVoiceIdType.BURT
            modelType = ElevenLabsVoiceModelType.ELEVEN_TURBO_V2
          }
        }
      }
    val je = response.toJsonElement()
    je.stringValue("messageResponse.assistant.voice.provider") shouldBeEqualTo "11labs"
    je.stringValue("messageResponse.assistant.voice.voiceId") shouldBeEqualTo "burt"
    je.stringValue("messageResponse.assistant.voice.model") shouldBeEqualTo "eleven_turbo_v2"
  }

  @Test
  fun `elevenLabs voice with customVoiceId`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          groqModel {
            modelType = GroqModelType.LLAMA3_8B_8192
          }
          elevenLabsVoice {
            customVoiceId = "my-custom-voice"
            modelType = ElevenLabsVoiceModelType.ELEVEN_TURBO_V2
          }
        }
      }
    val je = response.toJsonElement()
    je.stringValue("messageResponse.assistant.voice.provider") shouldBeEqualTo "11labs"
    je.stringValue("messageResponse.assistant.voice.voiceId") shouldBeEqualTo "my-custom-voice"
  }

  @Test
  fun `elevenLabs voice both voiceIdType and customVoiceId throws`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          groqModel {
            modelType = GroqModelType.LLAMA3_8B_8192
          }
          elevenLabsVoice {
            voiceIdType = ElevenLabsVoiceIdType.BURT
            customVoiceId = "custom"
            modelType = ElevenLabsVoiceModelType.ELEVEN_TURBO_V2
          }
        }
      }
    }.also {
      it.message shouldBeEqualTo "elevenLabsVoice{} cannot have both voiceIdType and customVoiceId values"
    }
  }

  @Test
  fun `elevenLabs voice missing model throws`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          groqModel {
            modelType = GroqModelType.LLAMA3_8B_8192
          }
          elevenLabsVoice {
            voiceIdType = ElevenLabsVoiceIdType.BURT
          }
        }
      }
    }.also {
      it.message shouldBeEqualTo "elevenLabsVoice{} requires a modelType or customModel value"
    }
  }

  @Test
  fun `openAI voice serializes provider and voiceId`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          groqModel {
            modelType = GroqModelType.LLAMA3_8B_8192
          }
          openAIVoice {
            voiceIdType = OpenAIVoiceIdType.ALLOY
          }
        }
      }
    val je = response.toJsonElement()
    je.stringValue("messageResponse.assistant.voice.provider") shouldBeEqualTo "openai"
    je.stringValue("messageResponse.assistant.voice.voiceId") shouldBeEqualTo "alloy"
  }

  @Test
  fun `deepgram voice serializes provider and voiceId`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          groqModel {
            modelType = GroqModelType.LLAMA3_8B_8192
          }
          deepgramVoice {
            voiceIdType = DeepGramVoiceIdType.ANGUS
          }
        }
      }
    val je = response.toJsonElement()
    je.stringValue("messageResponse.assistant.voice.provider") shouldBeEqualTo "deepgram"
    je.stringValue("messageResponse.assistant.voice.voiceId") shouldBeEqualTo "angus"
  }

  @Test
  fun `azure voice serializes provider and voiceId`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          groqModel {
            modelType = GroqModelType.LLAMA3_8B_8192
          }
          azureVoice {
            voiceIdType = AzureVoiceIdType.ANDREW
          }
        }
      }
    val je = response.toJsonElement()
    je.stringValue("messageResponse.assistant.voice.provider") shouldBeEqualTo "azure"
    je.stringValue("messageResponse.assistant.voice.voiceId") shouldBeEqualTo "andrew"
  }

  @Test
  fun `lmnt voice serializes provider and voiceId`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          groqModel {
            modelType = GroqModelType.LLAMA3_8B_8192
          }
          lmntVoice {
            voiceIdType = LMNTVoiceIdType.DANIEL
          }
        }
      }
    val je = response.toJsonElement()
    je.stringValue("messageResponse.assistant.voice.provider") shouldBeEqualTo "lmnt"
    je.stringValue("messageResponse.assistant.voice.voiceId") shouldBeEqualTo "daniel"
  }

  @Test
  fun `rimeAI voice serializes provider and voiceId`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          groqModel {
            modelType = GroqModelType.LLAMA3_8B_8192
          }
          rimeAIVoice {
            voiceIdType = RimeAIVoiceIdType.MARSH
            modelType = RimeAIVoiceModelType.MIST
          }
        }
      }
    val je = response.toJsonElement()
    je.stringValue("messageResponse.assistant.voice.provider") shouldBeEqualTo "rime-ai"
    je.stringValue("messageResponse.assistant.voice.voiceId") shouldBeEqualTo "marsh"
  }

  @Test
  fun `neets voice serializes provider and voiceId`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          groqModel {
            modelType = GroqModelType.LLAMA3_8B_8192
          }
          neetsVoice {
            voiceIdType = NeetsVoiceIdType.VITS
          }
        }
      }
    val je = response.toJsonElement()
    je.stringValue("messageResponse.assistant.voice.provider") shouldBeEqualTo "neets"
    je.stringValue("messageResponse.assistant.voice.voiceId") shouldBeEqualTo "vits"
  }

  @Test
  fun `double voice blocks throws error`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          groqModel {
            modelType = GroqModelType.LLAMA3_8B_8192
          }
          openAIVoice {
            voiceIdType = OpenAIVoiceIdType.ALLOY
          }
          deepgramVoice {
            voiceIdType = DeepGramVoiceIdType.ANGUS
          }
        }
      }
    }.also {
      it.message shouldBeEqualTo "openAIVoice{} already called"
    }
  }
}
