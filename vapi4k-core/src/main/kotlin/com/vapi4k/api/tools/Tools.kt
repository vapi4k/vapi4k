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

import com.vapi4k.dsl.tools.BaseTool
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker
import kotlin.reflect.KFunction

@Vapi4KDslMarker
interface Tools {
  /**
  Uses kotlin methods for implementing tool actions.
   */
  fun serviceTool(
    obj: Any,
    vararg functions: KFunction<*>,
    block: BaseTool.() -> Unit = {},
  )

  /**
  The tool action is defined in the block with the onInvoke{} function.
   */
  fun manualTool(block: ManualTool.() -> Unit)

  /**
  Defines a tool implemented on a remote server.
   */
  fun externalTool(block: ExternalTool.() -> Unit)

  /**
  Transfers the call to another destination.
   */
  fun transferTool(block: TransferTool.() -> Unit = {})

  fun dtmfTool(block: DtmfTool.() -> Unit)

  fun endCallTool(block: EndCallTool.() -> Unit)

  fun voiceMailTool(block: VoiceMailTool.() -> Unit)

  fun ghlTool(block: GhlTool.() -> Unit)

  fun makeTool(block: MakeTool.() -> Unit)
}
