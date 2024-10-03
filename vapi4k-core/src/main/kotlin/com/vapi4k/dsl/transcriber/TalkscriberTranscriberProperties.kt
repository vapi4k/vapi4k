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

import com.vapi4k.api.transcriber.enums.TalkscriberLanguageType
import com.vapi4k.api.transcriber.enums.TalkscriberModelType

interface TalkscriberTranscriberProperties {
  /**
  This is the model that will be used for the transcription.
   */
  var transcriberModel: TalkscriberModelType

  /**
  This is the language that will be set for the transcription. The list of languages Whisper supports can be found
  here: <a href="https://github.com/openai/whisper/blob/main/whisper/tokenizer.py" target="_blank">https://github.com/openai/whisper/blob/main/whisper/tokenizer.py</a>
   */
  var transcriberLanguage: TalkscriberLanguageType

  /**
  This enables specifying a model that doesn't already exist as an TalkscriberModelType enum.
   */
  var customModel: String

  /**
  This enables specifying a language that doesn't already exist as an TalkscriberLanguageType enum.
   */
  var customLanguage: String
}
