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

package com.vapi4k.api.assistant

import com.vapi4k.api.model.AnthropicModel
import com.vapi4k.api.model.AnyscaleModel
import com.vapi4k.api.model.CustomLLMModel
import com.vapi4k.api.model.DeepInfraModel
import com.vapi4k.api.model.GroqModel
import com.vapi4k.api.model.OpenAIModel
import com.vapi4k.api.model.OpenRouterModel
import com.vapi4k.api.model.PerplexityAIModel
import com.vapi4k.api.model.TogetherAIModel
import com.vapi4k.api.model.VapiModel

interface AssistantModels {
  /**
  Builder for the Anyscale model.
   */
  fun anyscaleModel(block: AnyscaleModel.() -> Unit): AnyscaleModel

  /**
  Builder for the Anthropic model.
   */
  fun anthropicModel(block: AnthropicModel.() -> Unit): AnthropicModel

  /**
  Builder for the CustomLLM model.
   */
  fun customLLMModel(block: CustomLLMModel.() -> Unit): CustomLLMModel

  /**
  Builder for the DeepInfra model.
   */
  fun deepInfraModel(block: DeepInfraModel.() -> Unit): DeepInfraModel

  /**
  Builder for the Groq model.
   */
  fun groqModel(block: GroqModel.() -> Unit): GroqModel

  /**
  Builder for the OpenAI model.
   */
  fun openAIModel(block: OpenAIModel.() -> Unit): OpenAIModel

  /**
  Builder for the OpenRouter model.
   */
  fun openRouterModel(block: OpenRouterModel.() -> Unit): OpenRouterModel

  /**
  Builder for the PerplexityAI model.
   */
  fun perplexityAIModel(block: PerplexityAIModel.() -> Unit): PerplexityAIModel

  /**
  Builder for the TogetherAI model.
   */
  fun togetherAIModel(block: TogetherAIModel.() -> Unit): TogetherAIModel

  /**
  Builder for the Vapi model.
   */
  fun vapiModel(block: VapiModel.() -> Unit): VapiModel
}
