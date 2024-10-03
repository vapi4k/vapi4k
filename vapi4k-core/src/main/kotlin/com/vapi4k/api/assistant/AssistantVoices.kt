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

import com.vapi4k.api.voice.AzureVoice
import com.vapi4k.api.voice.CartesiaVoice
import com.vapi4k.api.voice.DeepgramVoice
import com.vapi4k.api.voice.ElevenLabsVoice
import com.vapi4k.api.voice.LMNTVoice
import com.vapi4k.api.voice.NeetsVoice
import com.vapi4k.api.voice.OpenAIVoice
import com.vapi4k.api.voice.PlayHTVoice
import com.vapi4k.api.voice.RimeAIVoice

interface AssistantVoices {
  /**
  Builder for the Azure voice.
   */
  fun azureVoice(block: AzureVoice.() -> Unit): AzureVoice

  /**
  Builder for the Cartesia voice.
   */
  fun cartesiaVoice(block: CartesiaVoice.() -> Unit): CartesiaVoice

  /**
  Builder for the Deepgram voice.
   */
  fun deepgramVoice(block: DeepgramVoice.() -> Unit): DeepgramVoice

  /**
  Builder for the ElevenLabs voice.
   */
  fun elevenLabsVoice(block: ElevenLabsVoice.() -> Unit): ElevenLabsVoice

  /**
  Builder for the LMNT voice.
   */
  fun lmntVoice(block: LMNTVoice.() -> Unit): LMNTVoice

  /**
  Builder for the Neets voice.
   */
  fun neetsVoice(block: NeetsVoice.() -> Unit): NeetsVoice

  /**
  Builder for the OpenAI voice.
   */
  fun openAIVoice(block: OpenAIVoice.() -> Unit): OpenAIVoice

  /**
  Builder for the PlayHT voice.
   */
  fun playHTVoice(block: PlayHTVoice.() -> Unit): PlayHTVoice

  /**
  Builder for the RimeAI voice.
   */
  fun rimeAIVoice(block: RimeAIVoice.() -> Unit): RimeAIVoice
}
