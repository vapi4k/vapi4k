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

package com.vapi4k.dsl.vapi4k

import com.vapi4k.common.SessionId.Companion.toSessionId
import com.vapi4k.utils.DslUtils.getRandomSecret

enum class ApplicationType(
  internal val displayName: String,
  internal val pathPrefix: String,
  private val paramName: String,
) {
  INBOUND_CALL("inboundCallApplication", "inboundCall", "InboundCall"),
  OUTBOUND_CALL("outboundCallApplication", "outboundCall", "OutboundCall"),
  WEB("webApplication", "web", "Web"),
  ;

  internal val functionName get() = "$displayName{}"

  internal fun getRandomSessionId() = "${getRandomSecret(8, 4, 4, 12)}-$paramName".toSessionId()
}
