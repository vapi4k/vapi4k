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

import com.vapi4k.common.Constants.UNSPECIFIED_DEFAULT
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = SuccessEvaluationRubricTypeSerializer::class)
enum class SuccessEvaluationRubricType(
  val desc: String,
) {
  NUMERICSCALE("numericScale"),
  DESCRIPTIVESCALE("descriptiveScale"),
  CHECKLIST("checklist"),
  MATRIX("matrix"),
  PERCENTAGESCALE("percentageScale"),
  LIKERTSCALE("likertScale"),
  AUTOMATICRUBRIC("automaticRubric"),
  PASSFAIL("passFail"),
  UNSPECIFIED(UNSPECIFIED_DEFAULT),
}

private object SuccessEvaluationRubricTypeSerializer : KSerializer<SuccessEvaluationRubricType> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("SuccessEvaluationRubricType", PrimitiveKind.STRING)

  override fun serialize(
    encoder: Encoder,
    value: SuccessEvaluationRubricType,
  ) = encoder.encodeString(value.desc)

  override fun deserialize(decoder: Decoder) =
    SuccessEvaluationRubricType.entries.first { it.desc == decoder.decodeString() }
}
