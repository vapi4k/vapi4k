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
  GPT_5_2("gpt-5.2"),
  GPT_5_2_CHAT_LATEST("gpt-5.2-chat-latest"),
  GPT_5_1("gpt-5.1"),
  GPT_5_1_CHAT_LATEST("gpt-5.1-chat-latest"),
  GPT_5("gpt-5"),
  GPT_5_CHAT_LATEST("gpt-5-chat-latest"),
  GPT_5_MINI("gpt-5-mini"),
  GPT_5_NANO("gpt-5-nano"),
  GPT_4_1_2025_04_14("gpt-4.1-2025-04-14"),
  GPT_4_1_MINI_2025_04_14("gpt-4.1-mini-2025-04-14"),
  GPT_4_1_NANO_2025_04_14("gpt-4.1-nano-2025-04-14"),
  GPT_4_1("gpt-4.1"),
  GPT_4_1_MINI("gpt-4.1-mini"),
  GPT_4_1_NANO("gpt-4.1-nano"),
  CHATGPT_4O_LATEST("chatgpt-4o-latest"),
  O3("o3"),
  O3_MINI("o3-mini"),
  O4_MINI("o4-mini"),
  O1_MINI("o1-mini"),
  O1_MINI_2024_09_12("o1-mini-2024-09-12"),
  GPT_4O_REALTIME_PREVIEW_2024_10_01("gpt-4o-realtime-preview-2024-10-01"),
  GPT_4O_REALTIME_PREVIEW_2024_12_17("gpt-4o-realtime-preview-2024-12-17"),
  GPT_4O_MINI_REALTIME_PREVIEW_2024_12_17("gpt-4o-mini-realtime-preview-2024-12-17"),
  GPT_REALTIME_2025_08_28("gpt-realtime-2025-08-28"),
  GPT_REALTIME_MINI_2025_12_15("gpt-realtime-mini-2025-12-15"),
  GPT_4O_MINI_2024_07_18("gpt-4o-mini-2024-07-18"),
  GPT_4O_MINI("gpt-4o-mini"),
  GPT_4O("gpt-4o"),
  GPT_4O_2024_05_13("gpt-4o-2024-05-13"),
  GPT_4O_2024_08_06("gpt-4o-2024-08-06"),
  GPT_4O_2024_11_20("gpt-4o-2024-11-20"),
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

  // Azure regional variants
  GPT_3_5_TURBO_0125_CANADAEAST("gpt-3.5-turbo-0125:canadaeast"),
  GPT_3_5_TURBO_0125_NORTHCENTRALUS("gpt-3.5-turbo-0125:northcentralus"),
  GPT_3_5_TURBO_0125_SOUTHCENTRALUS("gpt-3.5-turbo-0125:southcentralus"),
  GPT_3_5_TURBO_1106_CANADAEAST("gpt-3.5-turbo-1106:canadaeast"),
  GPT_3_5_TURBO_1106_WESTUS("gpt-3.5-turbo-1106:westus"),
  GPT_4_0125_PREVIEW_EASTUS("gpt-4-0125-preview:eastus"),
  GPT_4_0125_PREVIEW_NORTHCENTRALUS("gpt-4-0125-preview:northcentralus"),
  GPT_4_0125_PREVIEW_SOUTHCENTRALUS("gpt-4-0125-preview:southcentralus"),
  GPT_4_0613_CANADAEAST("gpt-4-0613:canadaeast"),
  GPT_4_1106_PREVIEW_AUSTRALIAEAST("gpt-4-1106-preview:australiaeast"),
  GPT_4_1106_PREVIEW_CANADAEAST("gpt-4-1106-preview:canadaeast"),
  GPT_4_1106_PREVIEW_FRANCE("gpt-4-1106-preview:france"),
  GPT_4_1106_PREVIEW_INDIA("gpt-4-1106-preview:india"),
  GPT_4_1106_PREVIEW_NORWAY("gpt-4-1106-preview:norway"),
  GPT_4_1106_PREVIEW_SWEDENCENTRAL("gpt-4-1106-preview:swedencentral"),
  GPT_4_1106_PREVIEW_UK("gpt-4-1106-preview:uk"),
  GPT_4_1106_PREVIEW_WESTUS("gpt-4-1106-preview:westus"),
  GPT_4_1106_PREVIEW_WESTUS3("gpt-4-1106-preview:westus3"),
  GPT_4_TURBO_2024_04_09_EASTUS2("gpt-4-turbo-2024-04-09:eastus2"),
  GPT_4_1_2025_04_14_EASTUS("gpt-4.1-2025-04-14:eastus"),
  GPT_4_1_2025_04_14_EASTUS2("gpt-4.1-2025-04-14:eastus2"),
  GPT_4_1_2025_04_14_GERMANYWESTCENTRAL("gpt-4.1-2025-04-14:germanywestcentral"),
  GPT_4_1_2025_04_14_NORTHCENTRALUS("gpt-4.1-2025-04-14:northcentralus"),
  GPT_4_1_2025_04_14_POLANDCENTRAL("gpt-4.1-2025-04-14:polandcentral"),
  GPT_4_1_2025_04_14_SOUTHCENTRALUS("gpt-4.1-2025-04-14:southcentralus"),
  GPT_4_1_2025_04_14_SPAINCENTRAL("gpt-4.1-2025-04-14:spaincentral"),
  GPT_4_1_2025_04_14_WESTEUROPE("gpt-4.1-2025-04-14:westeurope"),
  GPT_4_1_2025_04_14_WESTUS("gpt-4.1-2025-04-14:westus"),
  GPT_4_1_2025_04_14_WESTUS3("gpt-4.1-2025-04-14:westus3"),
  GPT_4_1_MINI_2025_04_14_EASTUS("gpt-4.1-mini-2025-04-14:eastus"),
  GPT_4_1_MINI_2025_04_14_EASTUS2("gpt-4.1-mini-2025-04-14:eastus2"),
  GPT_4_1_MINI_2025_04_14_GERMANYWESTCENTRAL("gpt-4.1-mini-2025-04-14:germanywestcentral"),
  GPT_4_1_MINI_2025_04_14_NORTHCENTRALUS("gpt-4.1-mini-2025-04-14:northcentralus"),
  GPT_4_1_MINI_2025_04_14_POLANDCENTRAL("gpt-4.1-mini-2025-04-14:polandcentral"),
  GPT_4_1_MINI_2025_04_14_SOUTHCENTRALUS("gpt-4.1-mini-2025-04-14:southcentralus"),
  GPT_4_1_MINI_2025_04_14_SPAINCENTRAL("gpt-4.1-mini-2025-04-14:spaincentral"),
  GPT_4_1_MINI_2025_04_14_WESTEUROPE("gpt-4.1-mini-2025-04-14:westeurope"),
  GPT_4_1_MINI_2025_04_14_WESTUS("gpt-4.1-mini-2025-04-14:westus"),
  GPT_4_1_MINI_2025_04_14_WESTUS3("gpt-4.1-mini-2025-04-14:westus3"),
  GPT_4_1_NANO_2025_04_14_EASTUS2("gpt-4.1-nano-2025-04-14:eastus2"),
  GPT_4_1_NANO_2025_04_14_NORTHCENTRALUS("gpt-4.1-nano-2025-04-14:northcentralus"),
  GPT_4_1_NANO_2025_04_14_SOUTHCENTRALUS("gpt-4.1-nano-2025-04-14:southcentralus"),
  GPT_4_1_NANO_2025_04_14_WESTUS("gpt-4.1-nano-2025-04-14:westus"),
  GPT_4_1_NANO_2025_04_14_WESTUS3("gpt-4.1-nano-2025-04-14:westus3"),
  GPT_4O_2024_05_13_EASTUS("gpt-4o-2024-05-13:eastus"),
  GPT_4O_2024_05_13_EASTUS2("gpt-4o-2024-05-13:eastus2"),
  GPT_4O_2024_05_13_NORTHCENTRALUS("gpt-4o-2024-05-13:northcentralus"),
  GPT_4O_2024_05_13_SOUTHCENTRALUS("gpt-4o-2024-05-13:southcentralus"),
  GPT_4O_2024_05_13_WESTUS("gpt-4o-2024-05-13:westus"),
  GPT_4O_2024_05_13_WESTUS3("gpt-4o-2024-05-13:westus3"),
  GPT_4O_2024_08_06_EASTUS("gpt-4o-2024-08-06:eastus"),
  GPT_4O_2024_08_06_EASTUS2("gpt-4o-2024-08-06:eastus2"),
  GPT_4O_2024_08_06_NORTHCENTRALUS("gpt-4o-2024-08-06:northcentralus"),
  GPT_4O_2024_08_06_SOUTHCENTRALUS("gpt-4o-2024-08-06:southcentralus"),
  GPT_4O_2024_08_06_WESTUS("gpt-4o-2024-08-06:westus"),
  GPT_4O_2024_08_06_WESTUS3("gpt-4o-2024-08-06:westus3"),
  GPT_4O_2024_11_20_EASTUS("gpt-4o-2024-11-20:eastus"),
  GPT_4O_2024_11_20_EASTUS2("gpt-4o-2024-11-20:eastus2"),
  GPT_4O_2024_11_20_GERMANYWESTCENTRAL("gpt-4o-2024-11-20:germanywestcentral"),
  GPT_4O_2024_11_20_POLANDCENTRAL("gpt-4o-2024-11-20:polandcentral"),
  GPT_4O_2024_11_20_SOUTHCENTRALUS("gpt-4o-2024-11-20:southcentralus"),
  GPT_4O_2024_11_20_SPAINCENTRAL("gpt-4o-2024-11-20:spaincentral"),
  GPT_4O_2024_11_20_SWEDENCENTRAL("gpt-4o-2024-11-20:swedencentral"),
  GPT_4O_2024_11_20_WESTEUROPE("gpt-4o-2024-11-20:westeurope"),
  GPT_4O_2024_11_20_WESTUS("gpt-4o-2024-11-20:westus"),
  GPT_4O_2024_11_20_WESTUS3("gpt-4o-2024-11-20:westus3"),
  GPT_4O_MINI_2024_07_18_EASTUS("gpt-4o-mini-2024-07-18:eastus"),
  GPT_4O_MINI_2024_07_18_EASTUS2("gpt-4o-mini-2024-07-18:eastus2"),
  GPT_4O_MINI_2024_07_18_NORTHCENTRALUS("gpt-4o-mini-2024-07-18:northcentralus"),
  GPT_4O_MINI_2024_07_18_SOUTHCENTRALUS("gpt-4o-mini-2024-07-18:southcentralus"),
  GPT_4O_MINI_2024_07_18_WESTUS("gpt-4o-mini-2024-07-18:westus"),
  GPT_4O_MINI_2024_07_18_WESTUS3("gpt-4o-mini-2024-07-18:westus3"),

  UNSPECIFIED(UNSPECIFIED_DEFAULT),
  ;

  fun isSpecified() = this != UNSPECIFIED

  fun isNotSpecified() = this == UNSPECIFIED
}
