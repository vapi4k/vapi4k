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

package com.vapi4k.api.buttons

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ButtonColorSerializer::class)
class ButtonColor {
  internal val red: Int
  internal val green: Int
  internal val blue: Int

  constructor(
    red: Int,
    green: Int,
    blue: Int,
  ) {
    this.red = red
    this.green = green
    this.blue = blue
    validateRGB()
  }

  constructor(hex: Int) : this(
    red = (hex shr 16) and 0xFF,
    green = (hex shr 8) and 0xFF,
    blue = hex and 0xFF,
  )

  constructor(hex: String) {
    require(hex.matches(Regex("^#([A-Fa-f0-9]{6})$"))) { "Invalid hex color code: $hex" }
    val hexColor = hex.removePrefix("#")
    this.red = hexColor.substring(0, 2).toInt(16)
    this.green = hexColor.substring(2, 4).toInt(16)
    this.blue = hexColor.substring(4, 6).toInt(16)
  }

  private fun validateRGB() {
    require(red in 0..255) { "Red value should be in the range 0-255" }
    require(green in 0..255) { "Green value should be in the range 0-255" }
    require(blue in 0..255) { "Blue value should be in the range 0-255" }
  }

  override fun toString(): String {
    return "Color(red=$red, green=$green, blue=$blue)"
  }
}

private object ButtonColorSerializer : KSerializer<ButtonColor> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ButtonColor", PrimitiveKind.STRING)

  override fun serialize(
    encoder: Encoder,
    value: ButtonColor,
  ) = encoder.encodeString("rgb(${value.red}, ${value.green}, ${value.blue})")

  override fun deserialize(decoder: Decoder) = throw NotImplementedError("Deserialization is not supported")
}
