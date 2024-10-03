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

package com.vapi4k.api.tools

import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

/**
<p>These are the parameters the functions accepts, described as a JSON Schema object.
<br>See the <a href="https://platform.openai.com/docs/guides/function-calling" target="_blank">OpenAI guide</a> for examples, and the <a href="https://json-schema.org/understanding-json-schema" target="_blank">JSON Schema reference</a> for documentation about the format.
<br>Omitting parameters defines a function with an empty parameter list.
</p>
 */
@Vapi4KDslMarker
interface Parameters {
  /**
  This adds a parameter to the list of parameters.
   */
  fun parameter(block: Parameter.() -> Unit)
}
