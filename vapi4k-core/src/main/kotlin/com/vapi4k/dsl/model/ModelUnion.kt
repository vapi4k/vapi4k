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

package com.vapi4k.dsl.model

import com.vapi4k.common.AssistantId
import com.vapi4k.dtos.assistant.AnalysisPlanDto
import com.vapi4k.dtos.assistant.ArtifactPlanDto
import com.vapi4k.dtos.assistant.VoicemailDetectionDto
import com.vapi4k.dtos.model.CommonModelDto
import com.vapi4k.dtos.transcriber.CommonTranscriberDto
import com.vapi4k.dtos.voice.CommonVoiceDto
import com.vapi4k.server.RequestContextImpl
import com.vapi4k.utils.DuplicateInvokeChecker

interface ModelUnion {
  val requestContext: RequestContextImpl
  val assistantId: AssistantId
  val modelChecker: DuplicateInvokeChecker
  val modelDtoUnion: ModelDtoUnion
  val analysisPlanDto: AnalysisPlanDto
  val artifactPlanDto: ArtifactPlanDto
  val voicemailDetectionDto: VoicemailDetectionDto
  val transcriberChecker: DuplicateInvokeChecker
  val voiceChecker: DuplicateInvokeChecker
}

interface ModelDtoUnion {
  var transcriberDto: CommonTranscriberDto?
  var modelDto: CommonModelDto?
  var voiceDto: CommonVoiceDto?
}
