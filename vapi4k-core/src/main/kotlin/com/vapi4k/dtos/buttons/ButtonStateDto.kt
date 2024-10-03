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

package com.vapi4k.dtos.buttons

import com.vapi4k.api.buttons.ButtonColor
import com.vapi4k.api.buttons.enums.ButtonType
import com.vapi4k.dsl.buttons.ButtonStateProperties
import kotlinx.serialization.Serializable

@Serializable
data class ButtonStateDto(
  override var color: ButtonColor? = null,
  override var type: ButtonType = ButtonType.UNSPECIFIED,
  override var title: String = "",
  override var subtitle: String = "",
  override var icon: String = "",
) : ButtonStateProperties
