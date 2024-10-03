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

import com.vapi4k.api.assistant.enums.AssistantClientMessageType
import com.vapi4k.api.assistant.enums.AssistantServerMessageType
import com.vapi4k.api.assistant.enums.BackgroundSoundType
import com.vapi4k.api.assistant.enums.FirstMessageModeType
import com.vapi4k.dsl.assistant.AssistantOverridesProperties
import com.vapi4k.dsl.model.ModelDtoUnion
import com.vapi4k.dtos.model.CommonModelDto
import com.vapi4k.dtos.transcriber.CommonTranscriberDto
import com.vapi4k.dtos.voice.CommonVoiceDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssistantOverridesDto(
  override var backchannelingEnabled: Boolean? = null,
  override var backgroundDenoisingEnabled: Boolean? = null,
  override var backgroundSound: BackgroundSoundType = BackgroundSoundType.UNSPECIFIED,
  override var endCallMessage: String = "",
  override val endCallPhrases: MutableSet<String> = mutableSetOf(),
  override var firstMessage: String = "",
  override var firstMessageMode: FirstMessageModeType = FirstMessageModeType.UNSPECIFIED,
  override var hipaaEnabled: Boolean? = null,
  override var llmRequestDelaySeconds: Double = -1.0,
  override var llmRequestNonPunctuatedDelaySeconds: Double = -1.0,
  override var maxDurationSeconds: Int = -1,
  override val metadata: MutableMap<String, String> = mutableMapOf(),
  override var modelOutputInMessagesEnabled: Boolean? = null,
  override var name: String = "",
  override var numWordsToInterruptAssistant: Int = -1,
  override var recordingEnabled: Boolean? = null,
  override var responseDelaySeconds: Double = -1.0,
  override var serverUrl: String = "",
  override var serverUrlSecret: String = "",
  override var silenceTimeoutSeconds: Int = -1,
  override var voicemailMessage: String = "",
  // Need a copy of DEFAULT_CLIENT_MESSAGES and DEFAULT_SERVER_MESSAGES here, so call toMutableSet()
  override var clientMessages: MutableSet<AssistantClientMessageType> = DEFAULT_CLIENT_MESSAGES.toMutableSet(),
  override var serverMessages: MutableSet<AssistantServerMessageType> = DEFAULT_SERVER_MESSAGES.toMutableSet(),
  // TODO: Came from squad assistant
  override val transportConfigurations: MutableList<TransportConfigurationDto> = mutableListOf(),
  // TODO: This needs to be added to docs - https://docs.vapi.ai/assistants/function-calling
  override var forwardingPhoneNumber: String = "",
  // TODO: Not in docs or squad - https://docs.vapi.ai/assistants/function-calling
  override var endCallFunctionEnabled: Boolean? = null,
  // TODO: Not in docs or squad - https://docs.vapi.ai/assistants/function-calling
  override var dialKeypadFunctionEnabled: Boolean? = null,
  // Used only with AssistantOverrides
  override val variableValues: MutableMap<String, String> = mutableMapOf(),
  @SerialName("transcriber")
  override var transcriberDto: CommonTranscriberDto? = null,
  @SerialName("model")
  override var modelDto: CommonModelDto? = null,
  @SerialName("voice")
  override var voiceDto: CommonVoiceDto? = null,
  @SerialName("voicemailDetection")
  val voicemailDetectionDto: VoicemailDetectionDto = VoicemailDetectionDto(),
  @SerialName("analysisPlan")
  val analysisPlanDto: AnalysisPlanDto = AnalysisPlanDto(),
  @SerialName("artifactPlan")
  val artifactPlanDto: ArtifactPlanDto = ArtifactPlanDto(),
  @SerialName("messagePlan")
  val messagePlanDto: MessagePlanDto = MessagePlanDto(),
) : AssistantOverridesProperties,
  ModelDtoUnion
