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

package com.vapi4k.dtos.assistant

import com.vapi4k.api.assistant.enums.SuccessEvaluationRubricType
import com.vapi4k.dsl.assistant.AnalysisPlanProperties
import kotlinx.serialization.Serializable

@Serializable
data class AnalysisPlanDto(
  override var summaryPrompt: String = "",
  override var summaryRequestTimeoutSeconds: Double = -1.0,
  override var structuredDataRequestTimeoutSeconds: Double = -1.0,
  override var successEvaluationPrompt: String = "",
  override var successEvaluationRubric: SuccessEvaluationRubricType = SuccessEvaluationRubricType.UNSPECIFIED,
  override var successEvaluationRequestTimeoutSeconds: Double = -1.0,
  override var structuredDataPrompt: String = "",
  val structuredDataSchema: StructuredDataSchemaDto = StructuredDataSchemaDto(),
) : AnalysisPlanProperties
