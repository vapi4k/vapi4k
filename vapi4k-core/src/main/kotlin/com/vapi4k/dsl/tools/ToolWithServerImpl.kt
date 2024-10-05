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

package com.vapi4k.dsl.tools

import com.vapi4k.api.tools.Server
import com.vapi4k.common.DuplicateInvokeChecker
import com.vapi4k.dsl.vapi4k.ServerImpl
import com.vapi4k.dtos.tools.ToolDto

open class ToolWithServerImpl internal constructor(
  callerName: String,
  toolDto: ToolDto,
) : BaseToolImpl(callerName, toolDto),
  ToolWithServer {
  private val serverChecker = DuplicateInvokeChecker()

  override fun server(block: Server.() -> Unit): Server {
    serverChecker.check("${this.callerName}{} contains multiple calls to server{}")
    return ServerImpl(toolDto.server).apply(block)
  }

  internal fun checkIfServerCalled() {
    if (!serverChecker.wasCalled)
      error("${this.callerName}{} must contain a call to server{}")
  }
}
