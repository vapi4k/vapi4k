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

package com.vapi4k.dsl.web

import com.vapi4k.api.web.MethodType
import com.vapi4k.api.web.TalkButton
import com.vapi4k.common.CoreEnvVars.vapi4kBaseUrl
import com.vapi4k.common.CoreEnvVars.vapiPublicKey
import com.vapi4k.dsl.vapi4k.ApplicationType.WEB
import com.vapi4k.utils.JsonUtils
import com.vapi4k.utils.MiscUtils.removeEnds
import kotlinx.serialization.json.JsonElement

internal class TalkButtonProperties(
  override var serverPath: String = "",
  override var serverSecret: String = "",
  override var vapiPublicApiKey: String = vapiPublicKey,
  override var method: MethodType = MethodType.POST,
  override var postArgs: JsonElement = JsonUtils.EMPTY_JSON_ELEMENT,
) : TalkButton {
  fun verifyTalkButtonValues() {
    require(serverPath.isNotBlank()) { "serverPath must be assigned in talkButton{}" }
    require(vapiPublicApiKey.isNotBlank()) { "vapiPublicApiKey must be assigned in talkButton{}" }
  }

  fun invokeJsFunction(): String =
    buildString {
      appendLine()
      append("\t\taddVapiButton(\n$INDENT")
      appendLine(
        listOf(
          "'$vapi4kBaseUrl/${WEB.pathPrefix}/${serverPath.removeEnds("/")}'",
          "'$serverSecret'",
          "'$vapiPublicApiKey'",
          "'${method.name}'",
          "JSON.parse('$postArgs')",
        ).joinToString(",\n$INDENT"),
      )
      appendLine("\t\t);")
    }

  companion object {
    private const val INDENT = "\t\t\t"
  }
}
