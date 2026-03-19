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

package com.vapi4k.dsl.voice

import com.vapi4k.api.voice.PunctuationBoundaryType

interface ChunkPlanProperties {
  /**
  <p>This determines whether the model output is preprocessed into chunks before being sent to the voice provider.
  <br>Default <code>true</code> because voice generation sounds better with chunking (and reformatting them).
  <br>To send every token from the model output directly to the voice provider and rely on the voice provider's audio
  generation logic, set this to <code>false</code>.
  <br>If disabled, vapi-provided audio control tokens like &lt;flush /&gt; will not work.
  </p>
   */
  var enabled: Boolean?

  /**
  <p>This is the minimum number of characters before a chunk is created. The chunks that are sent to the voice provider
  for the voice generation as the model tokens are streaming in. Defaults to 30.
  <br>Increasing this value might add latency as it waits for the model to output a full chunk before sending it to the
  voice provider. On the other hand, increasing might be a good idea if you want to give voice provider bigger chunks,
  so it can pronounce them better.
  <br>Decreasing this value might decrease latency but might also decrease quality if the voice provider struggles to
  pronounce the text correctly.
  </p>
   */
  var minCharacters: Int

  /**
  <p>These are the punctuations that are considered valid boundaries before a chunk is created. The chunks that are sent
  to the voice provider for the voice generation as the model tokens are streaming in. Defaults are chosen differently
  for each provider.
  <br>Constraining the delimiters might add latency as it waits for the model to output a full chunk before sending it to
  the voice provider. On the other hand, constraining might be a good idea if you want to give voice provider longer
  chunks, so it can sound less disjointed across chunks. Eg. ['.'].
  </p>
   */
  val punctuationBoundaries: MutableSet<PunctuationBoundaryType>
}
