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

package com.vapi4k.console

import com.github.pambrose.common.json.toJsonElement
import com.github.pambrose.common.json.toJsonString
import com.vapi4k.common.Version.Companion.versionDesc
import com.vapi4k.console.ValidateAssistant.navBar
import com.vapi4k.console.ValidateAssistant.singleNavItem
import com.vapi4k.envvar.EnvVar.Companion.jsonEnvVarValues
import com.vapi4k.plugin.Vapi4kServer
import com.vapi4k.utils.HtmlUtils.html
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h5
import kotlinx.html.id
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr
import kotlinx.serialization.json.jsonObject

object ConsoleInfo {
  internal fun envVarsInfo(): String =
    html {
      navBar { singleNavItem("Environment Variables") }
      table {
        classes += "table"
        id = "envTable"
        thead {
          tr {
            th { +"Environment Variable" }
            th { +"Value" }
          }
        }
        tbody {
          jsonEnvVarValues().forEach { (key, value) ->
            tr {
              td { +key }
              td { +value.toJsonString(false) }
            }
          }
        }
      }
    }

  internal fun versionInfo(): String =
    html {
      navBar { singleNavItem("System Info") }
      div {
        id = "version-info"
        val json = Vapi4kServer::class.versionDesc(true).toJsonElement()
        for ((key, value) in json.jsonObject) {
          h5 { +"$key: $value" }
        }
      }
    }
}
