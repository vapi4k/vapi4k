/*
 * Copyright © 2024 Matthew Ambrose (mattbobambrose@gmail.com)
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

import com.vapi4k.dsl.model.enums.ModelType
import com.vapi4k.dtos.functions.FunctionDto
import com.vapi4k.dtos.tools.ToolDto
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ModelSerializer::class)
interface CommonModelDto {
  val provider: ModelType
  val tools: MutableList<ToolDto>
  val functions: MutableList<FunctionDto>
  var knowledgeBaseDto: KnowledgeBaseDto?
  val messages: MutableList<RoleMessageDto>
}

private object ModelSerializer : KSerializer<CommonModelDto> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("AbstractModelDto")

  override fun serialize(
    encoder: Encoder,
    value: CommonModelDto,
  ) {
    when (value) {
      is AnyscaleModelDto -> encoder.encodeSerializableValue(AnyscaleModelDto.serializer(), value)

      is AnthropicModelDto -> {
        value.assignEnumOverrides()
        encoder.encodeSerializableValue(AnthropicModelDto.serializer(), value)
      }

      is CustomLLMModelDto -> encoder.encodeSerializableValue(CustomLLMModelDto.serializer(), value)

      is DeepInfraModelDto -> encoder.encodeSerializableValue(DeepInfraModelDto.serializer(), value)

      is GroqModelDto -> {
        value.assignEnumOverrides()
        encoder.encodeSerializableValue(GroqModelDto.serializer(), value)
      }

      is OpenAIModelDto -> {
        value.assignEnumOverrides()
        encoder.encodeSerializableValue(OpenAIModelDto.serializer(), value)
      }

      is OpenRouterModelDto -> encoder.encodeSerializableValue(OpenRouterModelDto.serializer(), value)

      is PerplexityAIModelDto -> encoder.encodeSerializableValue(PerplexityAIModelDto.serializer(), value)

      is TogetherAIModelDto -> encoder.encodeSerializableValue(TogetherAIModelDto.serializer(), value)

      is VapiModelDto -> encoder.encodeSerializableValue(VapiModelDto.serializer(), value)

      else -> error("Invalid model provider: ${value::class.simpleName}")
    }
  }

  override fun deserialize(decoder: Decoder): CommonModelDto =
    throw NotImplementedError("Deserialization is not supported")
}
