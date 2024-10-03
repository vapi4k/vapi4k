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

package com.vapi4k.api.buttons.enums

import com.vapi4k.common.Constants.UNSPECIFIED_DEFAULT
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ButtonPositionSerializer::class)
enum class ButtonPosition(
  val desc: String,
) {
  TOP("top"),
  BOTTOM("bottom"),
  LEFT("left"),
  RIGHT("right"),
  TOP_LEFT("top-left"),
  TOP_RIGHT("top-right"),
  BOTTOM_LEFT("bottom-left"),
  BOTTOM_RIGHT("bottom-right"),
  UNSPECIFIED(UNSPECIFIED_DEFAULT),
}

private object ButtonPositionSerializer : KSerializer<ButtonPosition> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ButtonPosition", STRING)

  override fun serialize(
    encoder: Encoder,
    value: ButtonPosition,
  ) = encoder.encodeString(value.desc)

  override fun deserialize(decoder: Decoder) = ButtonPosition.entries.first { it.desc == decoder.decodeString() }
}
