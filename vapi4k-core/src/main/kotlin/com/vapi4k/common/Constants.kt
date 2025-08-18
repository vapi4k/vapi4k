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

package com.vapi4k.common

import com.vapi4k.common.Constants.FUNCTION_NAME

object Constants {
  const val HTMX_SOURCE_URL = "https://unpkg.com/htmx.org@2.0.1"
  const val HTMX_WS_SOURCE_URL = "https://unpkg.com/htmx-ext-ws@2.0.1/ws.js"

  const val UNSPECIFIED_DEFAULT = "unspecified"
  const val UNKNOWN = "unknown"
  const val STATIC_BASE = "/core_static"
  const val BS_BASE = "/bootstrap"
  const val PRISM_BASE = "/prism"

  const val FUNCTION_NAME = "functionName"

  const val APP_TYPE = "appType"
  const val APP_NAME = "appName"

  const val PRIVATE_KEY_PROPERTY = "vapi.api.privateKey"
  const val PHONE_NUMBER_ID_PROPERTY = "vapi.phoneNumberId"

  const val QUERY_ARGS = "queryArgs"
  const val POST_ARGS = "postArgs"

  const val ASSISTANT_ID_WIDTH = 3

  const val AUTH_BASIC = "auth-basic"
}

object QueryParams {
  const val SECRET_PARAM = "secret"

  private const val ID_PREFIX = "__"
  const val APPLICATION_ID = "${ID_PREFIX}applicationId"
  const val SESSION_ID = "${ID_PREFIX}sessionId"
  const val ASSISTANT_ID = "${ID_PREFIX}assistantId"
  const val TOOL_TYPE = "${ID_PREFIX}toolType"

  val SYSTEM_IDS = setOf(APPLICATION_ID, SESSION_ID, ASSISTANT_ID, TOOL_TYPE, FUNCTION_NAME)
}

object Headers {
  const val VAPI_SECRET_HEADER = "x-vapi-secret"
  const val VALIDATE_HEADER = "x-vapi4k-validate"
  const val VALIDATE_VALUE = "true"
}

object Endpoints {
  const val PING_PATH = "/ping"
  const val VERSION_PATH = "/version"
  const val ENV_PATH = "/env"
  const val METRICS_PATH = "/metrics"
  const val CACHES_PATH = "/caches"
  const val CLEAR_CACHES_PATH = "/clear-caches"
  const val ADMIN_PATH = "/admin"
  const val VALIDATE_PATH = "/validate"
  const val INVOKE_TOOL_PATH = "/invokeTool"
  const val ADMIN_LOG_ENDPOINT = "/admin-log"
  const val ADMIN_ENV_PATH = "/admin-env-vars"
  const val ADMIN_VERSION_PATH = "/admin-version"
}

object CssNames {
  const val MAIN_DIV = "main-div"
  const val SYS_INFO_DIV = "sys-info-div"
  const val TOOLS_DIV = "tools-div"
  const val LOG_DIV = "log-div"

  const val MESSAGE_RESPONSE = "message-response"
  const val SERVICE_TOOLS = "service-tools"
  const val MANUAL_TOOLS = "manual-tools"
  const val FUNCTIONS = "functions"

  const val VALIDATION_DATA = "validation-data"
  const val ERROR_MSG = "error-msg"
  const val HIDDEN = "hidden"
  const val ACTIVE = "active"
  const val ROUNDED = "rounded"
  const val COLLAPSED = "collapsed"

  const val CONNECT_ERROR = "connect-error"
}

object ErrorMessages {
  const val INVALID_BASE_URL = "Invalid VAPI4K_BASE_URL env var value"
}
