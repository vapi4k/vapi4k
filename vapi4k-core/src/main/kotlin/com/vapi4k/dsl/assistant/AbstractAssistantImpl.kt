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

package com.vapi4k.dsl.assistant

import com.vapi4k.api.assistant.AnalysisPlan
import com.vapi4k.api.assistant.ArtifactPlan
import com.vapi4k.api.assistant.VoicemailDetection
import com.vapi4k.api.model.AnthropicModel
import com.vapi4k.api.model.AnyscaleModel
import com.vapi4k.api.model.CustomLLMModel
import com.vapi4k.api.model.DeepInfraModel
import com.vapi4k.api.model.GroqModel
import com.vapi4k.api.model.OpenAIModel
import com.vapi4k.api.model.OpenRouterModel
import com.vapi4k.api.model.PerplexityAIModel
import com.vapi4k.api.model.TogetherAIModel
import com.vapi4k.api.model.VapiModel
import com.vapi4k.api.transcriber.DeepgramTranscriber
import com.vapi4k.api.transcriber.GladiaTranscriber
import com.vapi4k.api.transcriber.TalkscriberTranscriber
import com.vapi4k.api.voice.AzureVoice
import com.vapi4k.api.voice.CartesiaVoice
import com.vapi4k.api.voice.DeepgramVoice
import com.vapi4k.api.voice.ElevenLabsVoice
import com.vapi4k.api.voice.LMNTVoice
import com.vapi4k.api.voice.NeetsVoice
import com.vapi4k.api.voice.OpenAIVoice
import com.vapi4k.api.voice.PlayHTVoice
import com.vapi4k.api.voice.RimeAIVoice
import com.vapi4k.dsl.model.AnthropicModelImpl
import com.vapi4k.dsl.model.AnyscaleModelImpl
import com.vapi4k.dsl.model.CustomLLMModelImpl
import com.vapi4k.dsl.model.DeepInfraModelImpl
import com.vapi4k.dsl.model.GroqModelImpl
import com.vapi4k.dsl.model.ModelUnion
import com.vapi4k.dsl.model.OpenAIModelImpl
import com.vapi4k.dsl.model.OpenRouterModelImpl
import com.vapi4k.dsl.model.PerplexityAIModelImpl
import com.vapi4k.dsl.model.TogetherAIModelImpl
import com.vapi4k.dsl.model.VapiModelImpl
import com.vapi4k.dsl.transcriber.DeepgramTranscriberImpl
import com.vapi4k.dsl.transcriber.GladiaTranscriberImpl
import com.vapi4k.dsl.transcriber.TalkscriberTranscriberImpl
import com.vapi4k.dsl.voice.AzureVoiceImpl
import com.vapi4k.dsl.voice.CartesiaVoiceImpl
import com.vapi4k.dsl.voice.DeepgramVoiceImpl
import com.vapi4k.dsl.voice.ElevenLabsVoiceImpl
import com.vapi4k.dsl.voice.LMNTVoiceImpl
import com.vapi4k.dsl.voice.NeetsVoiceImpl
import com.vapi4k.dsl.voice.OpenAIVoiceImpl
import com.vapi4k.dsl.voice.PlayHTVoiceImpl
import com.vapi4k.dsl.voice.RimeAIVoiceImpl
import com.vapi4k.dtos.model.AnthropicModelDto
import com.vapi4k.dtos.model.AnyscaleModelDto
import com.vapi4k.dtos.model.CustomLLMModelDto
import com.vapi4k.dtos.model.DeepInfraModelDto
import com.vapi4k.dtos.model.GroqModelDto
import com.vapi4k.dtos.model.OpenAIModelDto
import com.vapi4k.dtos.model.OpenRouterModelDto
import com.vapi4k.dtos.model.PerplexityAIModelDto
import com.vapi4k.dtos.model.TogetherAIModelDto
import com.vapi4k.dtos.model.VapiModelDto
import com.vapi4k.dtos.transcriber.DeepgramTranscriberDto
import com.vapi4k.dtos.transcriber.GladiaTranscriberDto
import com.vapi4k.dtos.transcriber.TalkscriberTranscriberDto
import com.vapi4k.dtos.voice.AzureVoiceDto
import com.vapi4k.dtos.voice.CartesiaVoiceDto
import com.vapi4k.dtos.voice.DeepgramVoiceDto
import com.vapi4k.dtos.voice.ElevenLabsVoiceDto
import com.vapi4k.dtos.voice.LMNTVoiceDto
import com.vapi4k.dtos.voice.NeetsVoiceDto
import com.vapi4k.dtos.voice.OpenAIVoiceDto
import com.vapi4k.dtos.voice.PlayHTVoiceDto
import com.vapi4k.dtos.voice.RimeAIVoiceDto

abstract class AbstractAssistantImpl : ModelUnion {
  var videoRecordingEnabled: Boolean
    get() = artifactPlanDto.videoRecordingEnabled ?: false
    set(value) = run { artifactPlanDto.videoRecordingEnabled = value }

  fun voicemailDetection(block: VoicemailDetection.() -> Unit): VoicemailDetection =
    VoicemailDetectionImpl(voicemailDetectionDto).apply(block)

  // Transcribers
  fun deepgramTranscriber(block: DeepgramTranscriber.() -> Unit): DeepgramTranscriber {
    transcriberChecker.check("deepGramTranscriber{} already called")
    val transcriberDto = DeepgramTranscriberDto().also { modelDtoUnion.transcriberDto = it }
    return DeepgramTranscriberImpl(transcriberDto)
      .apply(block)
      .apply { transcriberDto.verifyValues() }
  }

  fun gladiaTranscriber(block: GladiaTranscriber.() -> Unit): GladiaTranscriber {
    transcriberChecker.check("gladiaTranscriber{} already called")
    val transcriberDto = GladiaTranscriberDto().also { modelDtoUnion.transcriberDto = it }
    return GladiaTranscriberImpl(transcriberDto)
      .apply(block)
      .apply { transcriberDto.verifyValues() }
  }

  fun talkscriberTranscriber(block: TalkscriberTranscriber.() -> Unit): TalkscriberTranscriber {
    transcriberChecker.check("talkscriberTranscriber{} already called")
    val transcriberDto = TalkscriberTranscriberDto().also { modelDtoUnion.transcriberDto = it }
    return TalkscriberTranscriberImpl(transcriberDto)
      .apply(block)
      .apply { transcriberDto.verifyValues() }
  }

  // Models
  fun anyscaleModel(block: AnyscaleModel.() -> Unit): AnyscaleModel {
    modelChecker.check("anyscaleModel{} already called")
    val modelDto = AnyscaleModelDto().also { modelDtoUnion.modelDto = it }
    return AnyscaleModelImpl(this, modelDto)
      .apply(block)
      .apply { modelDto.verifyValues() }
  }

  fun anthropicModel(block: AnthropicModel.() -> Unit): AnthropicModel {
    modelChecker.check("anthropicModel{} already called")
    val modelDto = AnthropicModelDto().also { modelDtoUnion.modelDto = it }
    return AnthropicModelImpl(this, modelDto)
      .apply(block)
      .apply { modelDto.verifyValues() }
  }

  fun customLLMModel(block: CustomLLMModel.() -> Unit): CustomLLMModel {
    modelChecker.check("customLLMModel{} already called")
    val modelDto = CustomLLMModelDto().also { modelDtoUnion.modelDto = it }
    return CustomLLMModelImpl(this, modelDto)
      .apply(block)
      .apply { modelDto.verifyValues() }
  }

  fun deepInfraModel(block: DeepInfraModel.() -> Unit): DeepInfraModel {
    modelChecker.check("deepInfraModel{} already called")
    val modelDto = DeepInfraModelDto().also { modelDtoUnion.modelDto = it }
    return DeepInfraModelImpl(this, modelDto)
      .apply(block)
      .apply { modelDto.verifyValues() }
  }

  fun groqModel(block: GroqModel.() -> Unit): GroqModel {
    modelChecker.check("groqModel{} already called")
    val modelDto = GroqModelDto().also { modelDtoUnion.modelDto = it }
    return GroqModelImpl(this, modelDto)
      .apply(block)
      .apply { modelDto.verifyValues() }
  }

  fun openAIModel(block: OpenAIModel.() -> Unit): OpenAIModel {
    modelChecker.check("openAIModel{} already called")
    val modelDto = OpenAIModelDto().also { modelDtoUnion.modelDto = it }
    return OpenAIModelImpl(this, modelDto)
      .apply(block)
      .apply { modelDto.verifyValues() }
  }

  fun openRouterModel(block: OpenRouterModel.() -> Unit): OpenRouterModel {
    modelChecker.check("openRouterModel{} already called")
    val modelDto = OpenRouterModelDto().also { modelDtoUnion.modelDto = it }
    return OpenRouterModelImpl(this, modelDto)
      .apply(block)
      .apply { modelDto.verifyValues() }
  }

  fun perplexityAIModel(block: PerplexityAIModel.() -> Unit): PerplexityAIModel {
    modelChecker.check("perplexityAIModel{} already called")
    val modelDto = PerplexityAIModelDto().also { modelDtoUnion.modelDto = it }
    return PerplexityAIModelImpl(this, modelDto)
      .apply(block)
      .apply { modelDto.verifyValues() }
  }

  fun togetherAIModel(block: TogetherAIModel.() -> Unit): TogetherAIModel {
    modelChecker.check("togetherAIModel{} already called")
    val modelDto = TogetherAIModelDto().also { modelDtoUnion.modelDto = it }
    return TogetherAIModelImpl(this, modelDto)
      .apply(block)
      .apply { modelDto.verifyValues() }
  }

  fun vapiModel(block: VapiModel.() -> Unit): VapiModel {
    modelChecker.check("vapiModel{} already called")
    val modelDto = VapiModelDto().also { modelDtoUnion.modelDto = it }
    return VapiModelImpl(this, modelDto)
      .apply(block)
      .apply { modelDto.verifyValues() }
  }

  // Voices
  fun azureVoice(block: AzureVoice.() -> Unit): AzureVoice {
    voiceChecker.check("azureVoice{} already called")
    val voiceDto = AzureVoiceDto().also { modelDtoUnion.voiceDto = it }
    return AzureVoiceImpl(voiceDto)
      .apply(block)
      .apply { voiceDto.verifyValues() }
  }

  fun cartesiaVoice(block: CartesiaVoice.() -> Unit): CartesiaVoice {
    voiceChecker.check("cartesiaVoice{} already called")
    val voiceDto = CartesiaVoiceDto().also { modelDtoUnion.voiceDto = it }
    return CartesiaVoiceImpl(voiceDto)
      .apply(block)
      .apply { voiceDto.verifyValues() }
  }

  fun deepgramVoice(block: DeepgramVoice.() -> Unit): DeepgramVoice {
    voiceChecker.check("deepgramVoice{} already called")
    val voiceDto = DeepgramVoiceDto().also { modelDtoUnion.voiceDto = it }
    return DeepgramVoiceImpl(voiceDto)
      .apply(block)
      .apply { voiceDto.verifyValues() }
  }

  fun elevenLabsVoice(block: ElevenLabsVoice.() -> Unit): ElevenLabsVoice {
    voiceChecker.check("elevenLabsVoice{} already called")
    val voiceDto = ElevenLabsVoiceDto().also { modelDtoUnion.voiceDto = it }
    return ElevenLabsVoiceImpl(voiceDto)
      .apply(block)
      .apply { voiceDto.verifyValues() }
  }

  fun lmntVoice(block: LMNTVoice.() -> Unit): LMNTVoice {
    voiceChecker.check("lmntVoice{} already called")
    val voiceDto = LMNTVoiceDto().also { modelDtoUnion.voiceDto = it }
    return LMNTVoiceImpl(voiceDto)
      .apply(block)
  }

  fun neetsVoice(block: NeetsVoice.() -> Unit): NeetsVoice {
    voiceChecker.check("neetsVoice{} already called")
    val voiceDto = NeetsVoiceDto().also { modelDtoUnion.voiceDto = it }
    return NeetsVoiceImpl(voiceDto)
      .apply(block)
      .apply { voiceDto.verifyValues() }
  }

  fun openAIVoice(block: OpenAIVoice.() -> Unit): OpenAIVoice {
    voiceChecker.check("openAIVoice{} already called")
    val voiceDto = OpenAIVoiceDto().also { modelDtoUnion.voiceDto = it }
    return OpenAIVoiceImpl(voiceDto)
      .apply(block)
      .apply { voiceDto.verifyValues() }
  }

  fun playHTVoice(block: PlayHTVoice.() -> Unit): PlayHTVoice {
    voiceChecker.check("playHTVoice{} already called")
    val voiceDto = PlayHTVoiceDto().also { modelDtoUnion.voiceDto = it }
    return PlayHTVoiceImpl(voiceDto)
      .apply(block)
      .apply { voiceDto.verifyValues() }
  }

  fun rimeAIVoice(block: RimeAIVoice.() -> Unit): RimeAIVoice {
    voiceChecker.check("rimeAIVoice{} already called")
    val voiceDto = RimeAIVoiceDto().also { modelDtoUnion.voiceDto = it }
    return RimeAIVoiceImpl(voiceDto)
      .apply(block)
      .apply { voiceDto.verifyValues() }
  }

  fun analysisPlan(block: AnalysisPlan.() -> Unit) = AnalysisPlanImpl(analysisPlanDto).apply(block)

  fun artifactPlan(block: ArtifactPlan.() -> Unit) = ArtifactPlanImpl(artifactPlanDto).apply(block)
}
