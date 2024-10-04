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

package com.vapi4k.api.model

import com.vapi4k.common.Constants.UNSPECIFIED_DEFAULT

enum class OpenAIModelType(
  val desc: String,
) {
  GPT_4O_MINI("gpt-4o-mini"),
  GPT_4O_MINI_2024_07_18("gpt-4o-mini-2024-07-18"),
  GPT_4O("gpt-4o"),
  GPT_4O_2024_05_13("gpt-4o-2024-05-13"),
  GPT_4_TURBO("gpt-4-turbo"),
  GPT_4_TURBO_2024_04_09("gpt-4-turbo-2024-04-09"),
  GPT_4_TURBO_PREVIEW("gpt-4-turbo-preview"),
  GPT_4_0125_PREVIEW("gpt-4-0125-preview"),
  GPT_4_1106_PREVIEW("gpt-4-1106-preview"),
  GPT_4("gpt-4"),
  GPT_4_0613("gpt-4-0613"),
  GPT_3_5_TURBO("gpt-3.5-turbo"),
  GPT_3_5_TURBO_0125("gpt-3.5-turbo-0125"),
  GPT_3_5_TURBO_1106("gpt-3.5-turbo-1106"),
  GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k"),
  GPT_3_5_TURBO_0613("gpt-3.5-turbo-0613"),
  UNSPECIFIED(UNSPECIFIED_DEFAULT),
  ;

  fun isSpecified() = this != UNSPECIFIED

  fun isNotSpecified() = this == UNSPECIFIED
}
