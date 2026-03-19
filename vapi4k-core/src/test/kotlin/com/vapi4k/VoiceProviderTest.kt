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
import com.vapi4k.api.voice.OpenAIVoiceIdType
import com.vapi4k.api.voice.RimeAIVoiceIdType
import com.vapi4k.api.voice.RimeAIVoiceModelType
import com.vapi4k.dsl.vapi4k.Vapi4kConfigImpl
import com.vapi4k.utils.assistantResponse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class VoiceProviderTest : StringSpec() {
  init {
    Vapi4kConfigImpl()
  }

  init {
    "elevenLabs voice serializes provider and voiceId" {
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
      je.stringValue("messageResponse.assistant.voice.provider") shouldBe "11labs"
      je.stringValue("messageResponse.assistant.voice.voiceId") shouldBe "burt"
      je.stringValue("messageResponse.assistant.voice.model") shouldBe "eleven_turbo_v2"
    }

    "elevenLabs voice with customVoiceId" {
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
      je.stringValue("messageResponse.assistant.voice.provider") shouldBe "11labs"
      je.stringValue("messageResponse.assistant.voice.voiceId") shouldBe "my-custom-voice"
    }

    "elevenLabs voice both voiceIdType and customVoiceId throws" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "elevenLabsVoice{} cannot have both voiceIdType and customVoiceId values"
    }

    "elevenLabs voice missing model throws" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "elevenLabsVoice{} requires a modelType or customModel value"
    }

    "openAI voice serializes provider and voiceId" {
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
      je.stringValue("messageResponse.assistant.voice.provider") shouldBe "openai"
      je.stringValue("messageResponse.assistant.voice.voiceId") shouldBe "alloy"
    }

    "deepgram voice serializes provider and voiceId" {
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
      je.stringValue("messageResponse.assistant.voice.provider") shouldBe "deepgram"
      je.stringValue("messageResponse.assistant.voice.voiceId") shouldBe "angus"
    }

    "azure voice serializes provider and voiceId" {
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
      je.stringValue("messageResponse.assistant.voice.provider") shouldBe "azure"
      je.stringValue("messageResponse.assistant.voice.voiceId") shouldBe "andrew"
    }

    "lmnt voice serializes provider and voiceId" {
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
      je.stringValue("messageResponse.assistant.voice.provider") shouldBe "lmnt"
      je.stringValue("messageResponse.assistant.voice.voiceId") shouldBe "daniel"
    }

    "rimeAI voice serializes provider and voiceId" {
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
      je.stringValue("messageResponse.assistant.voice.provider") shouldBe "rime-ai"
      je.stringValue("messageResponse.assistant.voice.voiceId") shouldBe "marsh"
    }

    "double voice blocks throws error" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "openAIVoice{} already called"
    }
  }
}
