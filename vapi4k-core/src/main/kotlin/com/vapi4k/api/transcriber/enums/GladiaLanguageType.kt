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

package com.vapi4k.api.transcriber.enums

import com.vapi4k.common.Constants.UNSPECIFIED_DEFAULT
import kotlinx.serialization.Serializable

@Serializable
enum class GladiaLanguageType(
  val desc: String,
) {
  AFRIKAANS("af"),
  ALBANIAN("sq"),
  AMHARIC("am"),
  ARABIC("ar"),
  ARMENIAN("hy"),
  ASSAMESE("as"),
  AZERBAIJANI("az"),
  BASHKIR("ba"),
  BASQUE("eu"),
  BELARUSIAN("be"),
  BENGALI("bn"),
  BOSNIAN("bs"),
  BRETON("br"),
  BULGARIAN("bg"),
  CATALAN("ca"),
  CHINESE("zh"),
  CROATIAN("hr"),
  CZECH("cs"),
  DANISH("da"),
  DUTCH("nl"),
  ENGLISH("en"),
  ESTONIAN("et"),
  FAROESE("fo"),
  FINNISH("fi"),
  FRENCH("fr"),
  GALICIAN("gl"),
  GEORGIAN("ka"),
  GERMAN("de"),
  GREEK("el"),
  GUJARATI("gu"),
  HAITIAN("ht"),
  HAUSA("ha"),
  HAWAIIAN("haw"),
  HEBREW("he"),
  HINDI("hi"),
  HUNGARIAN("hu"),
  ICELANDIC("is"),
  INDONESIAN("id"),
  ITALIAN("it"),
  JAPANESE("ja"),

  // TODO: Unknown
  JP("jp"),
  JAVANESE("jv"),
  KANNADA("kn"),
  KAZAKH("kk"),
  CENTRAL_KHMER("km"),
  KOREAN("ko"),
  LAO("lo"),
  LATIN("la"),
  LATVIAN("lv"),
  LINGALA("ln"),
  LITHUANIAN("lt"),
  LUXEMBOURGISH("lb"),
  MACEDONIAN("mk"),
  MALAGASY("mg"),
  MALAY("ms"),
  MALAYALAM("ml"),
  MALTESE("mt"),
  MAORI("mi"),
  MARATHI("mr"),
  MONGOLIAN("mn"),

  // TODO: Unknown
  MYMR("mymr"),
  NEPALI("ne"),
  NORWEGIAN("no"),
  NORWEGIAN_NYNORSK("nn"),
  OCCITAN("oc"),
  PASHTO("ps"),
  PERSIAN("fa"),
  POLISH("pl"),
  PORTUGUESE("pt"),
  PUNJABI("pa"),
  ROMANIAN("ro"),
  RUSSIAN("ru"),
  SANSKRIT("sa"),
  SERBIAN("sr"),
  SHONA("sn"),
  SINDHI("sd"),
  SINHALA("si"),
  SLOVAK("sk"),
  SLOVENIAN("sl"),
  SOMALI("so"),
  SPANISH("es"),
  SUNDANESE("su"),
  SWAHILI("sw"),
  SWEDISH("sv"),
  TAGALOG("tl"),
  TAJIK("tg"),
  TAMIL("ta"),
  TATAR("tt"),
  TELUGU("te"),
  THAI("th"),
  TIBETAN("bo"),
  TURKISH("tr"),
  TURKMEN("tk"),
  UKRAINIAN("uk"),
  URDU("ur"),
  UZBEK("uz"),
  VIETNAMESE("vi"),
  WELSH("cy"),
  YIDDISH("yi"),
  YORUBA("yo"),
  UNSPECIFIED(UNSPECIFIED_DEFAULT),
  ;

  fun isSpecified() = this != UNSPECIFIED

  fun isNotSpecified() = this == UNSPECIFIED
}
