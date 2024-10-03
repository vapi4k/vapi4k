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

package com.vapi4k.api.model.enums

import com.vapi4k.common.Constants.UNSPECIFIED_DEFAULT
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class MetaDataSendModeType(
  val desc: String,
) {
  /**
  `off` will not send any `metadata.payload` will look like `{ messages }`
   */
  OFF("off"),

  /**
  `variable` will send `assistant.metadata` as a variable on the payload. payload will look like `{ messages, metadata }`
   */
  VARIABLE("variable"),

  /**
  `destructured` will send `assistant.metadata` fields directly on the payload. payload will look like `{ messages, ...metadata }`
   */
  DESTRUCTURED("destructured"),
  UNSPECIFIED(UNSPECIFIED_DEFAULT),
  ;

  fun isSpecified() = this != UNSPECIFIED

  fun isNotSpecified() = this == UNSPECIFIED
}

private object MetaDataSendModeTypeSerializer : KSerializer<MetaDataSendModeType> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("MetaDataSendModeType", STRING)

  override fun serialize(
    encoder: Encoder,
    value: MetaDataSendModeType,
  ) = encoder.encodeString(value.desc)

  override fun deserialize(decoder: Decoder) = MetaDataSendModeType.entries.first { it.desc == decoder.decodeString() }
}
