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

package com.vapi4k.utils

import com.vapi4k.api.json.containsKey
import com.vapi4k.api.json.get
import com.vapi4k.api.json.jsonElementList
import com.vapi4k.api.json.stringValueOrNull
import com.vapi4k.api.json.toJsonElement
import com.vapi4k.api.json.toJsonElementList
import com.vapi4k.api.vapi4k.ServerRequestType.Companion.isToolCall
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

object JsonUtils {
  inline fun <reified T> JsonElement.toObject() = Json.decodeFromJsonElement<T>(this)

  inline fun <reified T> String.toObject() = Json.decodeFromString<T>(this)

  inline fun <reified T> JsonElement.toObjectList() = jsonArray.map { Json.decodeFromJsonElement<T>(it) }

  fun JsonElement.firstInList() = toJsonElementList().first()

  fun Map<String, JsonElement>.toJsonObject() = JsonObject(this)

  fun List<JsonElement>.toJsonArray() = JsonArray(this)

  fun JsonElement.modifyObjectWith(
    key: String,
    block: (MutableMap<String, JsonElement>) -> Unit,
  ): JsonObject = this[key].jsonObject.toMutableMap().also(block).toJsonObject()

  fun Map<String, Any>.toJsonPrimitives() =
    mapValues {
      if (it.value is String) JsonPrimitive(it.value as String) else it.value as JsonPrimitive
    }

  val JsonElement.toolCallList
    get() = if (isToolCall())
      jsonElementList("message.toolCallList")
    else
      error("JsonElement is not a tool call request")

  val EMPTY_JSON_ELEMENT = "{}".toJsonElement()

  internal fun JsonElement.getToolNames(key: String) =
    if (containsKey("$key.tools"))
      jsonElementList(key, "tools").mapNotNull { it.stringValueOrNull("function.name") }
    else
      emptyList()

  internal fun JsonElement.getFunctionNames(key: String) =
    if (containsKey("$key.functions"))
      jsonElementList(key, "functions").mapNotNull { it.stringValueOrNull("name") }
    else
      emptyList()
}
