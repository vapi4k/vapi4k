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

package com.vapi4k.dsl.transcriber

import com.vapi4k.api.transcriber.enums.DeepgramLanguageType
import com.vapi4k.api.transcriber.enums.DeepgramModelType

interface DeepgramTranscriberProperties {
  /**
  This is the Deepgram model that will be used. A list of models can be found
  here: <a href="https://developers.deepgram.com/docs/models-languages-overview" target="_blank">https://developers.deepgram.com/docs/models-languages-overview</a>
   */
  var transcriberModel: DeepgramModelType

  /**
  This is the language that will be set for the transcription. The list of languages Deepgram supports can be found
  here: <a href="https://developers.deepgram.com/docs/models-languages-overview" target="_blank">https://developers.deepgram.com/docs/models-languages-overview</a>
   */
  var transcriberLanguage: DeepgramLanguageType

  /**
  This enables specifying a model that doesn't already exist as an DeepgramModelType enum.
   */
  var customModel: String

  /**
  This enables specifying a language that doesn't already exist as an DeepgramLanguageType enum.
   */
  var customLanguage: String

  /**
  These keywords are passed to the transcription model to help it pick up use-case specific words. Anything that may not
  be a common word, like your company name, should be added here.
   */
  val keywords: MutableSet<String>

  /**
  This will be use smart format option provided by Deepgram. It's default disabled because it can sometimes format
  numbers as times sometimes, but it's getting better.
   */
  var smartFormat: Boolean?
}
