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

enum class GroqModelType(
  val desc: String,
) {
  OPENAI_GPT_OSS_20B("openai/gpt-oss-20b"),
  OPENAI_GPT_OSS_120B("openai/gpt-oss-120b"),
  DEEPSEEK_R1_DISTILL_LLAMA_70B("deepseek-r1-distill-llama-70b"),
  LLAMA_3_3_70B_VERSATILE("llama-3.3-70b-versatile"),
  LLAMA_3_1_405B_REASONING("llama-3.1-405b-reasoning"),
  LLAMA_3_1_8B_INSTANT("llama-3.1-8b-instant"),
  LLAMA3_8B_8192("llama3-8b-8192"),
  LLAMA3_70B_8192("llama3-70b-8192"),
  GEMMA2_9B_IT("gemma2-9b-it"),
  MOONSHOTAI_KIMI_K2_INSTRUCT_0905("moonshotai/kimi-k2-instruct-0905"),
  META_LLAMA_LLAMA_4_MAVERICK_17B_128E_INSTRUCT("meta-llama/llama-4-maverick-17b-128e-instruct"),
  META_LLAMA_LLAMA_4_SCOUT_17B_16E_INSTRUCT("meta-llama/llama-4-scout-17b-16e-instruct"),
  MISTRAL_SABA_24B("mistral-saba-24b"),
  COMPOUND_BETA("compound-beta"),
  COMPOUND_BETA_MINI("compound-beta-mini"),
  UNSPECIFIED(UNSPECIFIED_DEFAULT),
  ;

  fun isSpecified() = this != UNSPECIFIED

  fun isNotSpecified() = this == UNSPECIFIED
}
