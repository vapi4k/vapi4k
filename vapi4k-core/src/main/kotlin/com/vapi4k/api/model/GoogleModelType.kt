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

enum class GoogleModelType(
  val desc: String,
) {
  GEMINI_3_FLASH_PREVIEW("gemini-3-flash-preview"),
  GEMINI_2_5_PRO("gemini-2.5-pro"),
  GEMINI_2_5_FLASH("gemini-2.5-flash"),
  GEMINI_2_5_FLASH_LITE("gemini-2.5-flash-lite"),
  GEMINI_2_0_FLASH_THINKING_EXP("gemini-2.0-flash-thinking-exp"),
  GEMINI_2_0_PRO_EXP_02_05("gemini-2.0-pro-exp-02-05"),
  GEMINI_2_0_FLASH("gemini-2.0-flash"),
  GEMINI_2_0_FLASH_LITE("gemini-2.0-flash-lite"),
  GEMINI_2_0_FLASH_EXP("gemini-2.0-flash-exp"),
  GEMINI_2_0_FLASH_REALTIME_EXP("gemini-2.0-flash-realtime-exp"),
  GEMINI_1_5_FLASH("gemini-1.5-flash"),
  GEMINI_1_5_FLASH_002("gemini-1.5-flash-002"),
  GEMINI_1_5_PRO("gemini-1.5-pro"),
  GEMINI_1_5_PRO_002("gemini-1.5-pro-002"),
  GEMINI_1_0_PRO("gemini-1.0-pro"),
  UNSPECIFIED(UNSPECIFIED_DEFAULT),
  ;

  fun isSpecified() = this != UNSPECIFIED

  fun isNotSpecified() = this == UNSPECIFIED
}
