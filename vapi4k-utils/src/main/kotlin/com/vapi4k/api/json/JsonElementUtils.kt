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

package com.vapi4k.api.json

import com.vapi4k.common.Utils.prettyFormat
import com.vapi4k.common.Utils.rawFormat
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

val JsonElement.keys get() = jsonObject.keys

val JsonElement.stringValue get() = jsonPrimitive.content
val JsonElement.intValue get() = jsonPrimitive.content.toInt()
val JsonElement.doubleValue get() = jsonPrimitive.content.toDouble()
val JsonElement.booleanValue get() = jsonPrimitive.content.toBoolean()

operator fun JsonElement.get(vararg keys: String): JsonElement =
  keys.flatMap { it.split(".") }
    .fold(this) { acc, key -> acc.element(key) }

fun JsonElement.getOrNull(vararg keys: String): JsonElement? = if (containsKey(*keys)) get(*keys) else null

fun JsonElement.stringValue(vararg keys: String) = get(*keys).stringValue

fun JsonElement.stringValueOrNull(vararg keys: String) = getOrNull(*keys)?.stringValue

fun JsonElement.intValue(vararg keys: String) = get(*keys).intValue

fun JsonElement.intValueOrNull(vararg keys: String) = getOrNull(*keys)?.intValue

fun JsonElement.doubleValue(vararg keys: String) = get(*keys).doubleValue

fun JsonElement.doubleValueOrNull(vararg keys: String) = getOrNull(*keys)?.doubleValue

fun JsonElement.booleanValue(vararg keys: String) = get(*keys).booleanValue

fun JsonElement.booleanValueOrNull(vararg keys: String) = getOrNull(*keys)?.booleanValue

fun JsonElement.jsonElementList(vararg keys: String) = get(*keys).toJsonElementList()

fun JsonElement.jsonElementListOrNull(vararg keys: String) = getOrNull(*keys)?.toJsonElementList()

fun JsonElement.containsKey(vararg keys: String): Boolean {
  val ks = keys.flatMap { it.split(".") }
  var currElement: JsonElement = this
  for (k in ks) {
    if (currElement is JsonObject && k in currElement.keys)
      currElement = (currElement as JsonElement)[k]
    else
      return false
  }
  return true
}

val JsonElement.size get() = jsonObject.size

fun JsonElement.isEmpty() = if (this is JsonPrimitive) true else jsonObject.isEmpty()

fun JsonElement.isNotEmpty() = !isEmpty()

fun String.toJsonString() = toJsonElement().toJsonString(true)

inline fun <reified T> T.toJsonString(prettyPrint: Boolean = true) =
  (if (prettyPrint) prettyFormat else rawFormat).encodeToString(this)

inline fun <reified T> T.toJsonElement() = Json.encodeToJsonElement(this)

fun String.toJsonElement() = Json.parseToJsonElement(this)

fun JsonElement.toJsonElementList() = jsonArray.toList()

fun JsonElement.toMap(): Map<String, Any?> {
  if (this !is JsonObject) {
    throw IllegalArgumentException("Can only convert JsonObject to Map")
  }

  return entries.associate { (key, value) ->
    key to when (value) {
      is JsonPrimitive -> value.content
      is JsonArray -> value.map { it.toMap() }
      is JsonObject -> value.toMap()
      JsonNull -> null
    }
  }
}

internal fun JsonElement.element(key: String) =
  elementOrNull(key) ?: throw IllegalArgumentException("""JsonElement key "$key" not found""")

private fun JsonElement.elementOrNull(key: String) = jsonObject.get(key)
