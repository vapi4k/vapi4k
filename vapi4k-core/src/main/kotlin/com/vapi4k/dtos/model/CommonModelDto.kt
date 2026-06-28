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

package com.vapi4k.dtos.model

import com.vapi4k.dsl.model.ModelType
import com.vapi4k.dtos.functions.FunctionDto
import com.vapi4k.dtos.tools.ToolDto
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.reflect.KClass

@Serializable(with = ModelSerializer::class)
interface CommonModelDto {
  val provider: ModelType
  val tools: MutableList<ToolDto>
  val functions: MutableList<FunctionDto>
  var knowledgeBaseDto: KnowledgeBaseDto?
  val messages: MutableList<RoleMessageDto>

  fun assignEnumOverrides() {} // providers with an enum model type override this
}

object ModelSerializer : KSerializer<CommonModelDto> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("AbstractModelDto")

  private val serializers: Map<KClass<out CommonModelDto>, KSerializer<out CommonModelDto>> =
    mapOf(
      AnthropicBedrockModelDto::class to AnthropicBedrockModelDto.serializer(),
      AnthropicModelDto::class to AnthropicModelDto.serializer(),
      AnyscaleModelDto::class to AnyscaleModelDto.serializer(),
      CerebrasModelDto::class to CerebrasModelDto.serializer(),
      CustomLLMModelDto::class to CustomLLMModelDto.serializer(),
      DeepInfraModelDto::class to DeepInfraModelDto.serializer(),
      DeepSeekModelDto::class to DeepSeekModelDto.serializer(),
      GoogleModelDto::class to GoogleModelDto.serializer(),
      GroqModelDto::class to GroqModelDto.serializer(),
      InflectionAIModelDto::class to InflectionAIModelDto.serializer(),
      OpenAIModelDto::class to OpenAIModelDto.serializer(),
      OpenRouterModelDto::class to OpenRouterModelDto.serializer(),
      PerplexityAIModelDto::class to PerplexityAIModelDto.serializer(),
      TogetherAIModelDto::class to TogetherAIModelDto.serializer(),
      XaiModelDto::class to XaiModelDto.serializer(),
    )

  override fun serialize(
    encoder: Encoder,
    value: CommonModelDto,
  ) {
    value.assignEnumOverrides()
    val serializer = serializers[value::class]
      ?: error("Invalid model provider: ${value::class.simpleName}")
    @Suppress("UNCHECKED_CAST")
    encoder.encodeSerializableValue(serializer as KSerializer<CommonModelDto>, value)
  }

  override fun deserialize(decoder: Decoder): CommonModelDto =
    throw NotImplementedError("Deserialization is not supported")
}
