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

package com.vapi4k.dtos.tools

import com.vapi4k.api.tools.ToolMessageRoleType
import com.vapi4k.api.tools.ToolMessageType
import com.vapi4k.dsl.tools.ToolMessageCompleteProperties
import com.vapi4k.dsl.tools.ToolMessageDelayedProperties
import com.vapi4k.dsl.tools.ToolMessageFailedProperties
import com.vapi4k.dsl.tools.ToolMessageStartProperties
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ToolMessageSerializer::class)
sealed interface CommonToolMessageDto {
  val conditions: MutableSet<ToolMessageCondition>
}

private object ToolMessageSerializer : KSerializer<CommonToolMessageDto> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("CommonToolMessage")

  override fun serialize(
    encoder: Encoder,
    value: CommonToolMessageDto,
  ) {
    when (value) {
      is ToolMessageStartDto -> encoder.encodeSerializableValue(ToolMessageStartDto.serializer(), value)
      is ToolMessageCompleteDto -> encoder.encodeSerializableValue(ToolMessageCompleteDto.serializer(), value)
      is ToolMessageFailedDto -> encoder.encodeSerializableValue(ToolMessageFailedDto.serializer(), value)
      is ToolMessageDelayedDto -> encoder.encodeSerializableValue(ToolMessageDelayedDto.serializer(), value)
    }
  }

  override fun deserialize(decoder: Decoder): ToolMessageStartDto =
    throw NotImplementedError("Deserialization is not supported")
}

@Serializable
abstract class AbstractToolMessageDto {
  abstract var content: String

  fun verifyValues() {
    if (content.isEmpty()) error("Content is required for ToolMessage")
  }
}

@Serializable
class ToolMessageStartDto(
  override var content: String = "",
  override val conditions: MutableSet<ToolMessageCondition> = mutableSetOf(),
) : AbstractToolMessageDto(),
  ToolMessageStartProperties,
  CommonToolMessageDto {
  @EncodeDefault
  val type: ToolMessageType = ToolMessageType.REQUEST_START
}

@Serializable
class ToolMessageCompleteDto(
  override var content: String = "",
  override val conditions: MutableSet<ToolMessageCondition> = mutableSetOf(),
  override var role: ToolMessageRoleType = ToolMessageRoleType.UNSPECIFIED,
  override var endCallAfterSpokenEnabled: Boolean? = null,
) : AbstractToolMessageDto(),
  ToolMessageCompleteProperties,
  CommonToolMessageDto {
  @EncodeDefault
  val type: ToolMessageType = ToolMessageType.REQUEST_COMPLETE
}

@Serializable
class ToolMessageFailedDto(
  override var content: String = "",
  override val conditions: MutableSet<ToolMessageCondition> = mutableSetOf(),
  override var endCallAfterSpokenEnabled: Boolean? = null,
) : AbstractToolMessageDto(),
  ToolMessageFailedProperties,
  CommonToolMessageDto {
  @EncodeDefault
  val type: ToolMessageType = ToolMessageType.REQUEST_FAILED
}

@Serializable
class ToolMessageDelayedDto(
  override var content: String = "",
  override val conditions: MutableSet<ToolMessageCondition> = mutableSetOf(),
  override var timingMilliseconds: Int = -1,
) : AbstractToolMessageDto(),
  ToolMessageDelayedProperties,
  CommonToolMessageDto {
  @EncodeDefault
  val type: ToolMessageType = ToolMessageType.REQUEST_RESPONSE_DELAYED
}
