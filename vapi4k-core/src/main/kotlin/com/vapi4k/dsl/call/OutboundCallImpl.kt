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

package com.vapi4k.dsl.call

import com.vapi4k.api.call.OutboundCall
import com.vapi4k.api.web.MethodType
import com.vapi4k.common.Constants.PHONE_NUMBER_ID_PROPERTY
import com.vapi4k.common.CoreEnvVars.VAPI_PHONE_NUMBER_ID
import com.vapi4k.dtos.api.OutboundCallRequestDto
import com.vapi4k.envvar.EnvVar.Companion.getSystemValue
import com.vapi4k.utils.JsonUtils
import kotlinx.serialization.json.JsonElement

class OutboundCallImpl internal constructor(
  internal val outboundCallRequestDto: OutboundCallRequestDto,
) : OutboundCallProperties by outboundCallRequestDto,
  OutboundCall {
  override var serverPath = ""
  override var serverSecret = ""
  override var method: MethodType = MethodType.POST
  override var postArgs: JsonElement = JsonUtils.EMPTY_JSON_ELEMENT

  override var phoneNumber: String
    get() = outboundCallRequestDto.customerDto.number
    set(value) {
      outboundCallRequestDto.customerDto.number = value
    }

  internal fun verifyValues() {
    require(serverPath.isNotBlank()) { "serverPath must be assigned in outboundCall{}" }
    require(phoneNumber.isNotBlank()) { "phoneNumber must be assigned in outboundCall{}" }

    if (outboundCallRequestDto.phoneNumberId.isBlank())
      outboundCallRequestDto.phoneNumberId =
        getSystemValue(VAPI_PHONE_NUMBER_ID.value, PHONE_NUMBER_ID_PROPERTY) {
          "Missing phoneNumberId value. It can be defined with $PHONE_NUMBER_ID_PROPERTY in " +
            "application.conf, VAPI_PHONE_NUMBER_ID, or by assigning phoneNumberId in outboundCall{}"
        }
  }
}
