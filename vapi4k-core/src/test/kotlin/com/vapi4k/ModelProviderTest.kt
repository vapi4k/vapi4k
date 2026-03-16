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

import com.github.pambrose.common.json.jsonElementList
import com.github.pambrose.common.json.stringValue
import com.github.pambrose.common.json.toJsonElement
import com.vapi4k.AssistantTest.Companion.newRequestContext
import com.vapi4k.api.model.AnthropicModelType
import com.vapi4k.api.model.GroqModelType
import com.vapi4k.api.model.OpenAIModelType
import com.vapi4k.dsl.vapi4k.Vapi4kConfigImpl
import com.vapi4k.utils.assistantResponse
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test

class ModelProviderTest {
  init {
    Vapi4kConfigImpl()
  }

  @Test
  fun `openAI model serializes provider and modelType`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
          }
        }
      }
    val je = response.toJsonElement()
    je.stringValue("messageResponse.assistant.model.provider") shouldBeEqualTo "openai"
    je.stringValue("messageResponse.assistant.model.model") shouldBeEqualTo "gpt-3.5-turbo"
  }

  @Test
  fun `anthropic model serializes provider and modelType`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          anthropicModel {
            modelType = AnthropicModelType.CLAUDE_3_5_SONNET_20241022
          }
        }
      }
    val je = response.toJsonElement()
    je.stringValue("messageResponse.assistant.model.provider") shouldBeEqualTo "anthropic"
    je.stringValue("messageResponse.assistant.model.model") shouldBeEqualTo "claude-3-5-sonnet-20241022"
  }

  @Test
  fun `groq model serializes provider and modelType`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          groqModel {
            modelType = GroqModelType.LLAMA3_70B_8192
          }
        }
      }
    val je = response.toJsonElement()
    je.stringValue("messageResponse.assistant.model.provider") shouldBeEqualTo "groq"
    je.stringValue("messageResponse.assistant.model.model") shouldBeEqualTo "llama3-70b-8192"
  }

  @Test
  fun `customLLM model serializes provider and model`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          customLLMModel {
            model = "my-model"
            url = "https://my-llm.example.com/v1"
            systemMessage = "You are a helpful assistant"
          }
        }
      }
    val je = response.toJsonElement()
    je.stringValue("messageResponse.assistant.model.provider") shouldBeEqualTo "custom-llm"
    je.stringValue("messageResponse.assistant.model.model") shouldBeEqualTo "my-model"
    je.stringValue("messageResponse.assistant.model.url") shouldBeEqualTo "https://my-llm.example.com/v1"
  }

  @Test
  fun `openAI model with systemMessage`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
            systemMessage = "You are a test assistant"
          }
        }
      }
    val je = response.toJsonElement()
    val messages = je.jsonElementList("messageResponse.assistant.model.messages")
    messages.first().stringValue("content") shouldBeEqualTo "You are a test assistant"
  }

  @Test
  fun `multiple model blocks throws error`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
          }
          anthropicModel {
            modelType = AnthropicModelType.CLAUDE_3_5_SONNET_20241022
          }
        }
      }
    }.also {
      it.message shouldBeEqualTo "openAIModel{} already called"
    }
  }

  @Test
  fun `anthropic model with customModel`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          anthropicModel {
            customModel = "claude-custom-model"
          }
        }
      }
    val je = response.toJsonElement()
    je.stringValue("messageResponse.assistant.model.provider") shouldBeEqualTo "anthropic"
    je.stringValue("messageResponse.assistant.model.model") shouldBeEqualTo "claude-custom-model"
  }
}
