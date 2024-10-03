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

package com.vapi4k.api.model.enums

import com.vapi4k.common.Constants.UNSPECIFIED_DEFAULT

enum class AnthropicModelType(
  val desc: String,
) {
  CLAUDE_3_OPUS("claude-3-opus-20240229"),
  CLAUDE_3_SONNET("claude-3-sonnet-20240229"),
  CLAUDE_3_HAIKU("claude-3-haiku-20240307"),
  CLAUDE_3_5_SONNET("claude-3-5-sonnet-20240620"),
  UNSPECIFIED(UNSPECIFIED_DEFAULT),
  ;

  fun isSpecified() = this != UNSPECIFIED

  fun isNotSpecified() = this == UNSPECIFIED
}
