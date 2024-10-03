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

import com.vapi4k.api.json.containsKey
import com.vapi4k.api.json.get
import com.vapi4k.api.json.jsonElementList
import com.vapi4k.api.json.keys
import com.vapi4k.api.json.stringValue
import com.vapi4k.api.json.toJsonElement
import com.vapi4k.common.AssistantId
import com.vapi4k.common.AssistantId.Companion.EMPTY_ASSISTANT_ID
import com.vapi4k.common.AssistantId.Companion.getAssistantIdFromSuffix
import com.vapi4k.common.AssistantId.Companion.toAssistantId
import com.vapi4k.common.Constants.FUNCTION_NAME
import com.vapi4k.common.CssNames.FUNCTIONS
import com.vapi4k.common.CssNames.HIDDEN
import com.vapi4k.common.CssNames.MANUAL_TOOLS
import com.vapi4k.common.CssNames.ROUNDED
import com.vapi4k.common.CssNames.SERVICE_TOOLS
import com.vapi4k.common.CssNames.TOOLS_DIV
import com.vapi4k.common.CssNames.VALIDATION_DATA
import com.vapi4k.common.Endpoints.INVOKE_TOOL_PATH
import com.vapi4k.common.FunctionName
import com.vapi4k.common.FunctionName.Companion.toFunctionName
import com.vapi4k.common.QueryParams.APPLICATION_ID
import com.vapi4k.common.QueryParams.ASSISTANT_ID
import com.vapi4k.common.QueryParams.SESSION_ID
import com.vapi4k.common.QueryParams.TOOL_TYPE
import com.vapi4k.dsl.functions.FunctionDetails
import com.vapi4k.dsl.functions.ToolCallInfo.Companion.ID_SEPARATOR
import com.vapi4k.plugin.Vapi4kServer.logger
import com.vapi4k.server.RequestContextImpl
import com.vapi4k.utils.DslUtils.getRandomString
import com.vapi4k.utils.HtmlUtils.attribs
import com.vapi4k.utils.HtmlUtils.rawHtml
import com.vapi4k.utils.JsonUtils.getFunctionNames
import com.vapi4k.utils.JsonUtils.getToolNames
import com.vapi4k.utils.MiscUtils.appendQueryParams
import com.vapi4k.utils.ReflectionUtils.asKClass
import com.vapi4k.utils.ReflectionUtils.isNotRequestContextClass
import com.vapi4k.utils.ReflectionUtils.paramAnnotationWithDefault
import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.FORM
import kotlinx.html.InputType
import kotlinx.html.TBODY
import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.code
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h6
import kotlinx.html.hiddenInput
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.pre
import kotlinx.html.script
import kotlinx.html.style
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.tr
import kotlinx.serialization.json.JsonElement

object ValidateTools {
  fun TagConsumer<*>.displayTools(
    responseBody: String,
    requestContext: RequestContextImpl,
  ) {
    val topLevel = responseBody.toJsonElement()
    // Strip messageResponse if it exists
    val child = if (topLevel.containsKey("messageResponse")) topLevel["messageResponse"] else topLevel
    when {
      child.containsKey("assistant") -> {
        assistantRequestToolsBody(requestContext, child["assistant"], "model")
      }

      child.containsKey("squad") -> {
        if (child.containsKey("squad.members")) {
          child.jsonElementList("squad.members")
            .forEachIndexed { i, member ->
              val header = """Assistant "${getAssistantName(member, i + 1)}""""
              assistantRequestToolsBody(requestContext, member["assistant"], "model", header)
            }
        }

        if (child.containsKey("squad.membersOverrides")) {
          val header = "Member Overrides"
          assistantRequestToolsBody(requestContext, child["squad.membersOverrides"], "model", header)
        }
      }

      child.containsKey("assistantId") -> {
        if (child.containsKey("assistantOverrides")) {
          assistantRequestToolsBody(requestContext, child["assistantOverrides"], "model")
        }
      }

      child.containsKey("squadId") -> {
        // Nothing to do here
      }

      else -> {
        logger.error { "Unknown response type: ${responseBody.toJsonElement().keys}" }
      }
    }
  }

  private fun TagConsumer<*>.assistantRequestToolsBody(
    requestContext: RequestContextImpl,
    jsonElement: JsonElement,
    key: String,
    header: String = "",
  ) {
    val toolNames = jsonElement.getToolNames(key)
    val funcNames = jsonElement.getFunctionNames(key)

    if (requestContext.application.hasServiceTools()) {
      div {
        classes = setOf(VALIDATION_DATA, "$SERVICE_TOOLS-data", HIDDEN)
        displayServiceTools(requestContext, toolNames, header)
      }
    }

    if (requestContext.application.hasManualTools()) {
      div {
        classes = setOf(VALIDATION_DATA, "$MANUAL_TOOLS-data", HIDDEN)
        displayManualTools(requestContext, toolNames, header)
      }
    }

    if (requestContext.application.hasFunctions()) {
      div {
        classes = setOf(VALIDATION_DATA, "$FUNCTIONS-data", HIDDEN)
        displayFunctions(requestContext, funcNames, header)
      }
    }
  }

  private fun TagConsumer<*>.displayServiceTools(
    requestContext: RequestContextImpl,
    toolNames: List<String>,
    header: String,
  ) {
    val serviceCache = requestContext.application.serviceToolCache
    toolNames
      .mapIndexed { i, name ->
        // Grab assistantId from suffix of function name
        val assistantId = name.getAssistantIdFromSuffix()
        val newRequestContext = requestContext.copyWithNewAssistantId(assistantId)
        name.toFunctionName() to newRequestContext
      }
      .forEach { (toolName, newRequestContext) ->
        val functionInfo = serviceCache.getFromCache(newRequestContext)
        if (functionInfo.containsFunction(toolName)) {
          val functionDetails = functionInfo.getFunction(toolName)
          val divId = getRandomString()

          div {
            addToolHeader(header, functionDetails)
            addToolParams(ToolType.SERVICE_TOOL, "Tool", newRequestContext, divId, toolName, functionDetails)
            addToolResponse(divId)
          }
        }
      }
  }

  private fun TagConsumer<*>.displayManualTools(
    requestContext: RequestContextImpl,
    toolNames: List<String>,
    header: String,
  ) {
    toolNames
      .mapIndexed { i, name -> name.toFunctionName() to name.split(ID_SEPARATOR).last().toAssistantId() }
      .filter { (toolName, _) -> requestContext.application.containsManualTool(toolName) }
      .forEach { (funcName, assistantId) ->
        div {
          classes += TOOLS_DIV
          val manualToolImpl = requestContext.application.getManualTool(funcName)
          val divId = getRandomString()
          if (header.isNotBlank()) h6 { +header }
          h6 { +"$funcName(${manualToolImpl.signature})" }
          form {
            setHtmxTags(ToolType.MANUAL_TOOL, requestContext, divId)
            addHiddenFields(requestContext, funcName, true)

            table {
              tbody {
                manualToolImpl.properties
                  .forEach { (propertyName, propertyDesc) ->
                    tr {
                      td { +"$propertyName:" }
                      td {
                        input {
                          classes += "tools-input"
                          type =
                            when (propertyDesc.type) {
                              "string" -> InputType.text
                              "int" -> InputType.number
                              "double" -> InputType.number
                              "boolean" -> InputType.checkBox
                              else -> InputType.text
                            }
                          name = propertyName
                        }
                      }
                      td { +"(${propertyDesc.description})" }
                    }
                  }
                addInvokeButton("Tool")
              }
            }
          }
          addToolResponse(divId)
        }
      }
  }

  private fun TagConsumer<*>.displayFunctions(
    requestContext: RequestContextImpl,
    funcNames: List<String>,
    header: String,
  ) {
    val functionCache = requestContext.application.functionCache
    funcNames
      .mapIndexed { i, name ->
        // Grab assistantId from suffix of function name
        val assistantId = name.getAssistantIdFromSuffix()
        val newRequestContext = requestContext.copyWithNewAssistantId(assistantId)
        name.toFunctionName() to newRequestContext
      }
      .forEach { (funcName, newRequestContext) ->
        val functionInfo = functionCache.getFromCache(newRequestContext)
        if (functionInfo.containsFunction(funcName)) {
          val functionDetails = functionInfo.getFunction(funcName)
          val divId = getRandomString()

          div {
            addToolHeader(header, functionDetails)
            addToolParams(ToolType.FUNCTION, "Function", newRequestContext, divId, funcName, functionDetails)
            addToolResponse(divId)
          }
        }
      }
  }

  private fun DIV.addToolHeader(
    header: String,
    functionDetails: FunctionDetails,
  ) {
    classes += TOOLS_DIV
    if (header.isNotBlank()) h6 { +header }
    h6 { +functionDetails.toolCallInfo.llmDescription }
    div { +"${functionDetails.fqNameWithParams}: ${functionDetails.returnType}" }
  }

  private fun DIV.addToolParams(
    toolType: ToolType,
    invokeText: String,
    newRequestContext: RequestContextImpl,
    divId: String,
    toolName: FunctionName,
    functionDetails: FunctionDetails,
  ) {
    form {
      setHtmxTags(toolType, newRequestContext, divId)
      addHiddenFields(newRequestContext, toolName, false)

      table {
        tbody {
          functionDetails.params
            .filter { it.second.isNotRequestContextClass() }
            .forEach { functionDetail ->
              tr {
                td { +"${functionDetail.first}:" }
                td {
                  input {
                    classes += "tools-input"
                    type =
                      when (functionDetail.second.asKClass()) {
                        String::class -> InputType.text
                        Int::class -> InputType.number
                        Double::class -> InputType.number
                        Boolean::class -> InputType.checkBox
                        else -> InputType.text
                      }
                    name = functionDetail.first
                  }
                }
                td { +"(${functionDetail.second.paramAnnotationWithDefault})" }
              }
            }
          addInvokeButton(invokeText)
        }
      }
    }
  }

  private fun getAssistantName(
    assistantElement: JsonElement,
    index: Int,
  ): String =
    runCatching {
      assistantElement.stringValue("assistant.name")
    }.getOrElse { "Unnamed-$index" }

  private fun FORM.setHtmxTags(
    toolType: ToolType,
    requestContext: RequestContextImpl,
    divId: String,
  ) {
    attribs(
      "hx-get" to
        INVOKE_TOOL_PATH
          .appendQueryParams(
            TOOL_TYPE to toolType.name,
            SESSION_ID to requestContext.sessionId.value,
            ASSISTANT_ID to requestContext.assistantId.value,
          ),
      "hx-trigger" to "submit",
      "hx-target" to "#result-$divId",
    )
  }

  private fun FORM.addHiddenFields(
    requestContext: RequestContextImpl,
    funcName: FunctionName,
    emptyAssistantId: Boolean,
  ) {
    hiddenInput {
      name = APPLICATION_ID
      value = requestContext.application.applicationId.value
    }
    hiddenInput {
      name = SESSION_ID
      value = requestContext.sessionId.value
    }
    hiddenInput {
      name = ASSISTANT_ID
      value = (if (emptyAssistantId) EMPTY_ASSISTANT_ID else requestContext.assistantId).value
    }
    hiddenInput {
      name = FUNCTION_NAME
      value = funcName.value
    }
  }

  private fun TBODY.addInvokeButton(name: String) {
    tr {
      td {
        input {
          classes = setOf("btn", "btn-primary", "d-inline-flex", "align-items-center", ROUNDED, "border-0", "fs-6")
          style = "text-align: left;"
          id = "invoke-input"
          type = InputType.submit
          value = "Invoke $name"
        }
      }
      td {}
      td {}
    }
  }

  private fun DIV.addToolResponse(divId: String) {
    script { rawHtml("updateToolContent(`$divId`);\n") }

    div {
      classes = setOf("tools-response-div", HIDDEN)
      id = "display-$divId"

      button {
        classes += "btn-close"
        type = ButtonType.button
        attribs(
          "hx-on:click" to "closeToolContent('$divId')",
          "aria-label" to "Close",
        )
      }

      pre {
        code {
          classes = setOf("language-json", "line-numbers", "match-braces")
          id = "result-$divId"
        }
      }
    }
  }

  private fun RequestContextImpl.copyWithNewAssistantId(newAssistantId: AssistantId) =
    RequestContextImpl(application, request, sessionId, newAssistantId)
}
