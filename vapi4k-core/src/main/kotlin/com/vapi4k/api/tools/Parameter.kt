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

import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker
import kotlin.reflect.KClass

/**
This is a parameter that the function accepts.
 */
@Vapi4KDslMarker
interface Parameter {
  /**
  This is the name of the parameter
   */
  var name: String

  /**
  This is the description to help the model understand what it needs to output.
   */
  var description: String

  /**
  This is the type of the parameter.
   */
  var type: KClass<*>

  /**
  This determines if the parameter is required or not.
   */
  var required: Boolean
}
