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

package com.vapi4k.api.tools

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

@Retention(RUNTIME)
@Target(AnnotationTarget.FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER)
annotation class ToolCall(
  val description: String = "",
  val name: String = "",
)

@Retention(RUNTIME)
@Target(VALUE_PARAMETER)
annotation class Param(
  val description: String,
)
