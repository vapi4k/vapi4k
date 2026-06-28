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

import com.pambrose.common.json.stringValue
import com.pambrose.common.json.toJsonElement
import com.vapi4k.AssistantTest.Companion.newRequestContext
import com.vapi4k.api.assistant.Assistant
import com.vapi4k.api.model.AnthropicBedrockModelType
import com.vapi4k.api.model.AnthropicModelType
import com.vapi4k.api.model.CerebrasModelType
import com.vapi4k.api.model.DeepSeekModelType
import com.vapi4k.api.model.GoogleModelType
import com.vapi4k.api.model.GroqModelType
import com.vapi4k.api.model.InflectionAIModelType
import com.vapi4k.api.model.OpenAIModelType
import com.vapi4k.api.model.XaiModelType
import com.vapi4k.dsl.model.ModelType
import com.vapi4k.dsl.vapi4k.Vapi4kConfigImpl
import com.vapi4k.dtos.functions.FunctionDto
import com.vapi4k.dtos.model.CommonModelDto
import com.vapi4k.dtos.model.KnowledgeBaseDto
import com.vapi4k.dtos.model.ModelSerializer
import com.vapi4k.dtos.model.RoleMessageDto
import com.vapi4k.dtos.tools.ToolDto
import com.vapi4k.utils.assistantResponse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

/**
 * Exercises every concrete model provider through [ModelSerializer]'s class-keyed registry.
 *
 * Each case asserts the serialized `provider` and `model` wire values, which catches a mispaired
 * registry entry (`X::class to Y.serializer()`) and confirms `assignEnumOverrides()` runs for the
 * enum-model providers while leaving the raw-model providers' `model` untouched (the no-op default).
 */
class ModelSerializerTest : StringSpec() {
  init {
    Vapi4kConfigImpl()
  }

  private class ModelCase(
    val label: String,
    val provider: String,
    val model: String,
    val block: Assistant.() -> Unit,
  )

  private val modelCases =
    listOf(
      // Enum-model providers (assignEnumOverrides copies modelType.desc -> model)
      ModelCase("anthropicBedrock", "anthropic-bedrock", "claude-3-opus-20240229") {
        anthropicBedrockModel { modelType = AnthropicBedrockModelType.CLAUDE_3_OPUS_20240229 }
      },
      ModelCase("anthropic", "anthropic", "claude-3-opus-20240229") {
        anthropicModel { modelType = AnthropicModelType.CLAUDE_3_OPUS_20240229 }
      },
      ModelCase("cerebras", "cerebras", "llama3.1-8b") {
        cerebrasModel { modelType = CerebrasModelType.LLAMA3_1_8B }
      },
      ModelCase("deepSeek", "deep-seek", "deepseek-chat") {
        deepSeekModel { modelType = DeepSeekModelType.DEEPSEEK_CHAT }
      },
      ModelCase("google", "google", "gemini-2.5-flash") {
        googleModel { modelType = GoogleModelType.GEMINI_2_5_FLASH }
      },
      ModelCase("groq", "groq", "llama3-70b-8192") {
        groqModel { modelType = GroqModelType.LLAMA3_70B_8192 }
      },
      ModelCase("inflectionAI", "inflection-ai", "inflection_3_pi") {
        inflectionAIModel { modelType = InflectionAIModelType.INFLECTION_3_PI }
      },
      ModelCase("openAI", "openai", "gpt-4o-mini") {
        openAIModel { modelType = OpenAIModelType.GPT_4O_MINI }
      },
      ModelCase("xai", "xai", "grok-2") {
        xaiModel { modelType = XaiModelType.GROK_2 }
      },
      // Raw-model providers (no enum; the no-op assignEnumOverrides must leave model untouched)
      ModelCase("anyscale", "anyscale", "meta-llama/Llama-2-70b-chat-hf") {
        anyscaleModel { model = "meta-llama/Llama-2-70b-chat-hf" }
      },
      ModelCase("customLLM", "custom-llm", "my-custom-model") {
        customLLMModel {
          model = "my-custom-model"
          url = "https://my-llm.example.com/v1"
        }
      },
      ModelCase("deepInfra", "deepinfra", "meta-llama/Llama-2-70b-chat-hf") {
        deepInfraModel { model = "meta-llama/Llama-2-70b-chat-hf" }
      },
      ModelCase("openRouter", "openrouter", "openai/gpt-4") {
        openRouterModel { model = "openai/gpt-4" }
      },
      ModelCase("perplexityAI", "perplexity-ai", "sonar-pro") {
        perplexityAIModel { model = "sonar-pro" }
      },
      ModelCase("togetherAI", "together-ai", "meta-llama/Llama-2-70b-chat-hf") {
        togetherAIModel { model = "meta-llama/Llama-2-70b-chat-hf" }
      },
    )

  init {
    modelCases.forEach { case ->
      "${case.label} model routes to its serializer and emits provider + model" {
        val je =
          assistantResponse(newRequestContext()) {
            assistant(case.block)
          }.toJsonElement()
        je.stringValue("messageResponse.assistant.model.provider") shouldBe case.provider
        je.stringValue("messageResponse.assistant.model.model") shouldBe case.model
      }
    }

    "registry covers every model provider exactly once" {
      modelCases.size shouldBe 15
      modelCases.map { it.provider }.toSet().size shouldBe 15
    }

    "unregistered CommonModelDto throws Invalid model provider" {
      shouldThrow<IllegalStateException> {
        Json.encodeToString(ModelSerializer, FakeModelDto())
      }.message shouldBe "Invalid model provider: FakeModelDto"
    }
  }

  private class FakeModelDto : CommonModelDto {
    override val provider = ModelType.OPEN_AI
    override val tools = mutableListOf<ToolDto>()
    override val functions = mutableListOf<FunctionDto>()
    override var knowledgeBaseDto: KnowledgeBaseDto? = null
    override val messages = mutableListOf<RoleMessageDto>()
  }
}
