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

import com.github.pambrose.common.json.toJsonString
import com.vapi4k.common.CssNames.ACTIVE
import com.vapi4k.common.CssNames.ERROR_MSG
import com.vapi4k.common.CssNames.FUNCTIONS
import com.vapi4k.common.CssNames.MANUAL_TOOLS
import com.vapi4k.common.CssNames.MESSAGE_RESPONSE
import com.vapi4k.common.CssNames.SERVICE_TOOLS
import com.vapi4k.common.CssNames.VALIDATION_DATA
import com.vapi4k.console.ValidateTools.displayTools
import com.vapi4k.dsl.vapi4k.AbstractApplicationImpl
import com.vapi4k.server.RequestContextImpl
import com.vapi4k.utils.HtmlUtils.attribs
import com.vapi4k.utils.HtmlUtils.html
import io.ktor.http.HttpStatusCode
import kotlinx.html.DIV
import kotlinx.html.TagConsumer
import kotlinx.html.a
import kotlinx.html.classes
import kotlinx.html.code
import kotlinx.html.div
import kotlinx.html.h6
import kotlinx.html.id
import kotlinx.html.li
import kotlinx.html.nav
import kotlinx.html.pre
import kotlinx.html.span
import kotlinx.html.style
import kotlinx.html.ul

object ValidateAssistant {
  fun validateAssistant(
    application: AbstractApplicationImpl,
    requestContext: RequestContextImpl,
    status: HttpStatusCode,
    responseBody: String,
  ): String =
    html {
      navBar {
        if (status == HttpStatusCode.OK) {
          validNavItems(requestContext)
        } else {
          singleNavItem()
        }
      }

      if (status == HttpStatusCode.OK) {
        displayResponse(application, responseBody)
        displayTools(responseBody, requestContext)
      } else {
        displayError(application, status, responseBody)
      }
    }

  internal fun TagConsumer<StringBuilder>.navBar(block: DIV.() -> Unit) {
    nav {
      classes = setOf("navbar", "navbar-expand-lg", "bg-body-tertiary")
      style = "padding-top: 10px; padding-bottom: 0px;"
      div {
        classes += "container-fluid"
        block()
      }
    }
  }

  private fun DIV.validNavItems(requestContext: RequestContextImpl) {
    div {
      classes = setOf("collapse", "navbar-collapse")
      id = "navbarNav"
      ul {
        // classes += "navbar-nav"
        classes = setOf("nav", "nav-tabs")
        li {
          classes += "nav-item"
          a {
            classes = setOf("nav-link", ACTIVE)
            id = "$MESSAGE_RESPONSE-tab"
            attribs(
              "hx-on:click" to "selectApplicationTab('$MESSAGE_RESPONSE')",
              "aria-current" to "page",
            )
            +"Assistant Response"
          }
        }

        li {
          classes += "nav-item"
          a {
            val hasServiceTools = requestContext.application.hasServiceTools()
            classes = setOf("nav-link", if (hasServiceTools) "" else "disabled")
            id = "$SERVICE_TOOLS-tab"
            attribs("hx-on:click" to "selectApplicationTab('$SERVICE_TOOLS')")
            +"Service Tools"
          }
        }

        li {
          classes += "nav-item"
          a {
            val hasManualTools = requestContext.application.hasManualTools()
            classes = setOf("nav-link", if (hasManualTools) "" else "disabled")
            id = "$MANUAL_TOOLS-tab"
            attribs("hx-on:click" to "selectApplicationTab('$MANUAL_TOOLS')")
            +"Manual Tools"
          }
        }

        li {
          classes += "nav-item"
          a {
            val hasFunctions = requestContext.application.hasFunctions()
            classes = setOf("nav-link", if (hasFunctions) "" else "disabled")
            id = "$FUNCTIONS-tab"
            attribs("hx-on:click" to "selectApplicationTab('$FUNCTIONS')")
            +"Functions"
          }
        }
      }
    }
  }

  internal fun DIV.singleNavItem(msg: String = "Error Message") {
    div {
      classes = setOf("collapse", "navbar-collapse")
      id = "navbarNav"
      ul {
        classes = setOf("nav", "nav-tabs")
        li {
          classes += "nav-item"
          a {
            classes = setOf("nav-link", ACTIVE)
            id = "$MESSAGE_RESPONSE-tab"
            attribs("aria-current" to "page")
            +msg
          }
        }
      }
    }
  }

  private fun TagConsumer<*>.displayResponse(
    application: AbstractApplicationImpl,
    responseBody: String,
  ) {
    div {
      classes = setOf(VALIDATION_DATA, "$MESSAGE_RESPONSE-data")
      div {
        id = "response-header"
        +"Vapi Server URL: "
        span {
          style = "padding-left: 4px;"
          +application.serverUrl
        }
      }
      pre {
        code {
          // "line-numbers" is added in the JS code. It is a work-around for it getting dropped on the 2nd selection
          classes = setOf("language-json", "match-braces")
          id = "response-main"
          +responseBody.toJsonString()
        }
      }
    }
  }

  private fun TagConsumer<*>.displayError(
    application: AbstractApplicationImpl,
    status: HttpStatusCode,
    responseBody: String,
  ) {
    div {
      classes = setOf(VALIDATION_DATA, "$MESSAGE_RESPONSE-data")

      div {
        classes += ERROR_MSG
        h6 {
          style = "padding-top: 10px;"
          +"Vapi Server URL: "
          a {
            href = application.serverUrl
            target = "_blank"
            +application.serverUrl
          }
        }
        h6 { +"Status: $status" }
      }

      if (responseBody.isNotEmpty()) {
        if (responseBody.length < 80) {
          h6 {
            classes += ERROR_MSG
            +"Error: $responseBody"
          }
        } else {
          h6 {
            classes += ERROR_MSG
            +"Error:"
          }
          pre { +"  $responseBody" }
        }
      } else {
        h6 {
          classes += ERROR_MSG
          +"Check the ktor log for error information."
        }
      }
    }
  }
}
