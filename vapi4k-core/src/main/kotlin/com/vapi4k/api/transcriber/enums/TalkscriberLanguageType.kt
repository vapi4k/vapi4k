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

package com.vapi4k.api.transcriber.enums

import com.vapi4k.common.Constants.UNSPECIFIED_DEFAULT
import kotlinx.serialization.Serializable

@Serializable
enum class TalkscriberLanguageType(
  val desc: String,
) {
  ENGLISH("en"),
  CHINESE("zh"),
  GERMAN("de"),
  SPANISH("es"),
  RUSSIAN("ru"),
  KOREAN("ko"),
  FRENCH("fr"),
  JAPANESE("ja"),
  PORTUGUESE("pt"),
  TURKISH("tr"),
  POLISH("pl"),
  CATALAN("ca"),
  DUTCH("nl"),
  ARABIC("ar"),
  SWEDISH("sv"),
  ITALIAN("it"),
  INDONESIAN("id"),
  HINDI("hi"),
  FINNISH("fi"),
  VIETNAMESE("vi"),
  HEBREW("he"),
  UKRAINIAN("uk"),
  GREEK("el"),
  MALAY("ms"),
  CZECH("cs"),
  ROMANIAN("ro"),
  DANISH("da"),
  HUNGARIAN("hu"),
  TAMIL("ta"),
  NORWEGIAN("no"),
  THAI("th"),
  URDU("ur"),
  CROATIAN("hr"),
  BULGARIAN("bg"),
  LITHUANIAN("lt"),
  LATIN("la"),
  MAORI("mi"),
  MALAYALAM("ml"),
  WELSH("cy"),
  SLOVAK("sk"),
  TELUGU("te"),
  PERSIAN("fa"),
  LATVIAN("lv"),
  BENGALI("bn"),
  SERBIAN("sr"),
  AZERBAIJANI("az"),
  SLOVENIAN("sl"),
  KANNADA("kn"),
  ESTONIAN("et"),
  MACEDONIAN("mk"),
  BRETON("br"),
  BASQUE("eu"),
  ICELANDIC("is"),
  ARMENIAN("hy"),
  NEPALI("ne"),
  MONGOLIAN("mn"),
  BOSNIAN("bs"),
  KAZAKH("kk"),
  ALBANIAN("sq"),
  SWAHILI("sw"),
  GALICIAN("gl"),
  MARATHI("mr"),
  PUNJABI("pa"),
  SINHALA("si"),
  CENTRAL_KHMER("km"),
  SHONA("sn"),
  YORUBA("yo"),
  SOMALI("so"),
  AFRIKAANS("af"),
  OCCITAN("oc"),
  GEORGIAN("ka"),
  BELARUSIAN("be"),
  TAJIK("tg"),
  SINDHI("sd"),
  GUJARATI("gu"),
  AMHARIC("am"),
  YIDDISH("yi"),
  LAO("lo"),
  UZBEK("uz"),
  FAROESE("fo"),
  HAITIAN("ht"),
  PASHTO("ps"),
  TURKMEN("tk"),
  NORWEGIAN_NYNORSK("nn"),
  MALTESE("mt"),
  SANSKRIT("sa"),
  LUXEMBOURGISH("lb"),
  BURMESE("my"),
  TIBETAN("bo"),
  TAGALOG("tl"),
  MALAGASY("mg"),
  ASSAMESE("as"),
  TATAR("tt"),
  HAWAIIAN("haw"),
  LINGALA("ln"),
  HAUSA("ha"),
  BASHKIR("ba"),

  // TODO: Unknown
  JW("jw"),
  SUNDANESE("su"),
  YUE_CHINESE("yue"),
  UNSPECIFIED(UNSPECIFIED_DEFAULT),
  ;

  fun isSpecified() = this != UNSPECIFIED

  fun isNotSpecified() = this == UNSPECIFIED
}
