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

package com.vapi4k.api.transcriber

import com.vapi4k.common.Constants.UNSPECIFIED_DEFAULT
import kotlinx.serialization.Serializable

// See: https://developers.deepgram.com/docs/models-languages-overview
@Serializable
enum class DeepgramLanguageType(
  val desc: String,
) {
  ARABIC("ar"),
  AZERBAIJANI("az"),
  BASHKIR("ba"),
  BRETON("br"),
  BULGARIAN("bg"),
  CATALAN("ca"),
  CHINESE_MANDARIN_SIMPLIFIED("zh"),
  CHINESE_MANDARIN_SIMPLIFIED_CN("zh-CN"),
  CHINESE_MANDARIN_SIMPLIFIED_HAN("zh-Hans"),
  CHINESE_MANDARIN_TRADITIONAL_TW("zh-TW"),
  CHINESE_MANDARIN_TRADITIONAL_HAN("zh-Hant"),
  CHINESE_HK("zh-HK"),
  CZECH("cs"),
  DANISH("da"),
  DANISH_DK("da-DK"),
  DUTCH("nl"),
  ENGLISH("en"),
  ENGLISH_AU("en-AU"),
  ENGLISH_CA("en-CA"),
  ENGLISH_GB("en-GB"),
  ENGLISH_IE("en-IE"),
  ENGLISH_IN("en-IN"),
  ENGLISH_NZ("en-NZ"),
  ENGLISH_US("en-US"),
  ESTONIAN("et"),
  BASQUE("eu"),
  PERSIAN("fa"),
  FINNISH("fi"),
  FLEMISH("nl-BE"),
  FRENCH("fr"),
  FRENCH_CA("fr-CA"),
  GERMAN("de"),
  GERMAN_SWISS("de-CH"),
  GREEK("el"),
  HAUSA("ha"),
  HAWAIIAN("haw"),
  HEBREW("he"),
  HINDI("hi"),
  HUNGARIAN("hu"),
  ICELANDIC("is"),
  INDONESIAN("id"),
  ITALIAN("it"),
  JAPANESE("ja"),
  JAVANESE("jw"),
  KANNADA("kn"),
  KOREAN("ko"),
  KOREAN_KR("ko-KR"),
  LATVIAN("lv"),
  LINGALA("ln"),
  LITHUANIAN("lt"),
  MACEDONIAN("mk"),
  MALAY("ms"),
  MULTILINGUAL_SPANISH_ENGLISH("multi"),
  NORWEGIAN("no"),
  POLISH("pl"),
  PORTUGUESE("pt"),
  PORTUGUESE_BR("pt-BR"),
  PORTUGUESE_PT("pt-PT"),
  ROMANIAN("ro"),
  RUSSIAN("ru"),
  SERBIAN("sr"),
  SHONA("sn"),
  SLOVAK("sk"),
  SLOVENIAN("sl"),
  SOMALI("so"),
  SPANISH("es"),
  SPANISH_419("es-419"),
  SUNDANESE("su"),
  SWEDISH("sv"),
  SWEDISH_SE("sv-SE"),
  THAI("th"),
  THAI_TH("th-TH"),
  TATAR("tt"),
  TURKISH("tr"),
  UKRAINIAN("uk"),
  URDU("ur"),
  VIETNAMESE("vi"),
  YORUBA("yo"),

  // Work with enhanced models
  TAMASHEQ("taq"),
  TAMIL("ta"),

  // Work with nova models
  HINDI_LATN("hi-Latn"),

  // Work with enhanced and base models
  SPANISH_LATAM("es-LATAM"),

  UNSPECIFIED(UNSPECIFIED_DEFAULT),
  ;

  fun isSpecified() = this != UNSPECIFIED

  fun isNotSpecified() = this == UNSPECIFIED
}
