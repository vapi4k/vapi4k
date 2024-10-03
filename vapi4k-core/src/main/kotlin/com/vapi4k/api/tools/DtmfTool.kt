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

package com.vapi4k.api.tools

import com.vapi4k.api.reponse.DtmfToolResponse
import com.vapi4k.dsl.tools.ToolWithParameters
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker
import kotlinx.serialization.json.JsonElement

@Vapi4KDslMarker
interface DtmfTool : ToolWithParameters {
  /**
  <p>This is the name of the function to be called.
  <br>Must be a-z, A-Z, 0-9, or contain underscores and dashes, with a maximum length of 64.
  </p>
   */
  var name: String

  /**
  This is the description of what the function does, used by the AI to choose when and how to call the function.
   */
  var description: String

  /**
  The block that will be executed when the tool is called.
   */
  fun onInvoke(block: suspend DtmfToolResponse.(JsonElement) -> Unit)
}
