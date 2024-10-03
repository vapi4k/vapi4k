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

package com.vapi4k.dsl.model

import com.vapi4k.api.model.enums.GroqModelType

interface GroqModelProperties {
  /**
  This is the name of the model.
   */
  var modelType: GroqModelType

  /**
  This enables specifying a model that doesn't already exist as an GroqModelType enum.
   */
  var customModel: String

  /**
  This determines whether we detect user's emotion while they speak and send it as an additional info to model.
  Default `false` because the model is usually good at understanding the user's emotion from text.
   */
  var emotionRecognitionEnabled: Boolean?

  /**
  This is the max number of tokens that the assistant will be allowed to generate in each turn of the conversation. Default is 250.
   */
  var maxTokens: Int

  /**
  This sets how many turns at the start of the conversation to use a smaller, faster model from the same provider
  before switching to the primary model. Example, gpt-3.5-turbo if provider is openai.
  Default is 0.
   */
  var numFastTurns: Int

  /**
  This is the temperature that will be used for calls.
   */
  var temperature: Double

  /**
  These are the tools that the assistant can use during the call. To use transient tools, use `tools`.
  Both `tools` and `toolIds` can be used together.
   */
  val toolIds: MutableSet<String>
}
