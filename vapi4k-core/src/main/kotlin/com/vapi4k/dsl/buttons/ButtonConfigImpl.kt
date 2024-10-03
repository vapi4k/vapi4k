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

package com.vapi4k.dsl.buttons

import com.vapi4k.api.buttons.ButtonConfig
import com.vapi4k.api.buttons.ButtonState
import com.vapi4k.dtos.buttons.ButtonConfigDto

class ButtonConfigImpl internal constructor(
  private val dto: ButtonConfigDto,
) : ButtonConfigProperties by dto,
  ButtonConfig {
  override fun idle(block: ButtonState.() -> Unit) {
    ButtonStateImpl(dto.idle).apply(block)
  }

  override fun loading(block: ButtonState.() -> Unit) {
    ButtonStateImpl(dto.loading).apply(block)
  }

  override fun active(block: ButtonState.() -> Unit) {
    ButtonStateImpl(dto.active).apply(block)
  }
}
