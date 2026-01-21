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

package com.vapi4k

import com.github.pambrose.common.json.get
import com.github.pambrose.common.json.jsonElementList
import com.github.pambrose.common.json.stringValue
import com.github.pambrose.common.json.toJsonElement
import com.vapi4k.AssistantTest.Companion.newRequestContext
import com.vapi4k.api.model.GroqModelType
import com.vapi4k.api.voice.CartesiaVoiceLanguageType
import com.vapi4k.api.voice.CartesiaVoiceModelType
import com.vapi4k.api.voice.PlayHTVoiceEmotionType
import com.vapi4k.api.voice.PlayHTVoiceIdType
import com.vapi4k.utils.assistantResponse
import kotlinx.serialization.json.jsonArray
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test

class VoiceTest {
  @Test
  fun `playHt voice basic test`() {
    val squad =
      assistantResponse(newRequestContext()) {
        squad {
          members {
            member {
              assistant {
                name = "Receptionist"
                firstMessage = "Hi there!"

                groqModel {
                  modelType = GroqModelType.LLAMA3_8B_8192
                }

                playHTVoice {
                  voiceIdType = PlayHTVoiceIdType.MATT
                  emotion = PlayHTVoiceEmotionType.MALE_SAD
                }
              }
            }
          }
        }
      }
    val jsonElement = squad.toJsonElement()
    val members = jsonElement["messageResponse.squad.members"].jsonArray.toList()
    members.size shouldBeEqualTo 1
    members.first().stringValue("assistant.name") shouldBeEqualTo "Receptionist"
    members.first().stringValue("assistant.firstMessage") shouldBeEqualTo "Hi there!"
    members.first().stringValue("assistant.model.model") shouldBeEqualTo "llama3-8b-8192"
    members.first().stringValue("assistant.model.provider") shouldBeEqualTo "groq"
    members.first().stringValue("assistant.voice.voiceId") shouldBeEqualTo "matt"
    members.first().stringValue("assistant.voice.emotion") shouldBeEqualTo "male_sad"
  }

  @Test
  fun `playHt voice two or no voiceId error test`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        squad {
          members {
            member {
              assistant {
                name = "Receptionist"
                firstMessage = "Hi there!"

                groqModel {
                  modelType = GroqModelType.LLAMA3_8B_8192
                }

                playHTVoice {
                  voiceIdType = PlayHTVoiceIdType.MATT
                  emotion = PlayHTVoiceEmotionType.MALE_SAD
                  customVoiceId = "jeff"
                }
              }
            }
          }
        }
      }
    }.also {
      it.message shouldBeEqualTo "playHTVoice{} cannot have both voiceIdType and customVoiceId values"
    }

    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        squad {
          members {
            member {
              assistant {
                name = "Receptionist"
                firstMessage = "Hi there!"

                groqModel {
                  modelType = GroqModelType.LLAMA3_8B_8192
                }

                playHTVoice {
                  emotion = PlayHTVoiceEmotionType.MALE_SAD
                }
              }
            }
          }
        }
      }
    }.also {
      it.message shouldBeEqualTo "playHTVoice{} requires a voiceIdType or customVoiceId value"
    }
  }

  @Test
  fun `cartesia voice two or no models error test`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        squad {
          members {
            member {
              assistant {
                name = "Receptionist"
                firstMessage = "Hi there!"

                groqModel {
                  modelType = GroqModelType.LLAMA3_8B_8192
                }

                cartesiaVoice {
                  voiceId = "matt"
                  modelType = CartesiaVoiceModelType.SONIC_ENGLISH
                  customModel = "specialModel"
                }
              }
            }
          }
        }
      }
    }.also {
      it.message shouldBeEqualTo "cartesiaVoice{} cannot have both modelType and customModel values"
    }

//    assertThrows(IllegalStateException::class.java) {
//      assistantResponse(newRequestContext()) {
//        squad {
//          members {
//            member {
//              assistant {
//                name = "Receptionist"
//                firstMessage = "Hi there!"
//
//                groqModel {
//                  modelType = GroqModelType.MIXTRAL_8X7B
//                }
//
//                cartesiaVoice {
//                  voiceId = "matt"
//                }
//              }
//            }
//          }
//        }
//      }
//    }.also {
//      assertEquals("cartesiaVoice{} requires a modelType or customModel value", it.message)
//    }
  }

  @Test
  fun `cartesia voice two languages error test`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        squad {
          members {
            member {
              assistant {
                name = "Receptionist"
                firstMessage = "Hi there!"

                groqModel {
                  modelType = GroqModelType.LLAMA3_8B_8192
                }

                cartesiaVoice {
                  voiceId = "matt"
                  modelType = CartesiaVoiceModelType.SONIC_ENGLISH
                  languageType = CartesiaVoiceLanguageType.FRENCH
                  customLanguage = "specialLanguage"
                }
              }
            }
          }
        }
      }
    }.also {
      it.message shouldBeEqualTo "cartesiaVoice{} cannot have both languageType and customLanguage values"
    }
  }

  @Test
  fun `cartesia voice double model error test`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        squad {
          members {
            member {
              assistant {
                name = "Receptionist"
                firstMessage = "Hi there!"

                groqModel {
                  modelType = GroqModelType.LLAMA3_8B_8192
                }

                groqModel {
                  modelType = GroqModelType.LLAMA3_8B_8192
                }

                cartesiaVoice {
                  voiceId = "matt"
                  modelType = CartesiaVoiceModelType.SONIC_ENGLISH
                  customModel = "specialModel"
                }
              }
            }
          }
        }
      }
    }.also {
      it.message shouldBeEqualTo "groqModel{} already called"
    }
  }

  @Test
  fun `cartesia voice double voice error test`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        squad {
          members {
            member {
              assistant {
                name = "Receptionist"
                firstMessage = "Hi there!"

                groqModel {
                  modelType = GroqModelType.LLAMA3_8B_8192
                }

                cartesiaVoice {
                  voiceId = "matt"
                  modelType = CartesiaVoiceModelType.SONIC_ENGLISH
                }

                cartesiaVoice {
                  voiceId = "matt"
                  modelType = CartesiaVoiceModelType.SONIC_ENGLISH
                }
              }
            }
          }
        }
      }
    }.also {
      it.message shouldBeEqualTo "cartesiaVoice{} already called"
    }
  }

  @Test
  fun `double values test`() {
    val squad =
      assistantResponse(newRequestContext()) {
        squad {
          members {
            member {
              assistant {
                name = "Receptionist 1"
                name = "Receptionist"
                firstMessage = "Hi there!"
                firstMessage = "Hello!"

                groqModel {
                  modelType = GroqModelType.LLAMA3_70B_8192
                  modelType = GroqModelType.LLAMA3_8B_8192
                }

                playHTVoice {
                  voiceIdType = PlayHTVoiceIdType.MATT
                  voiceIdType = PlayHTVoiceIdType.JACK
                  emotion = PlayHTVoiceEmotionType.MALE_SAD
                  emotion = PlayHTVoiceEmotionType.MALE_ANGRY
                  temperature = 5.0
                  temperature = 10.0
                }
              }
            }
          }
        }
      }
    val jsonElement = squad.toJsonElement()
    val members = jsonElement.jsonElementList("messageResponse.squad.members")
    with(members.first()) {
      stringValue("assistant.firstMessage") shouldBeEqualTo "Hello!"
      stringValue("assistant.model.model") shouldBeEqualTo "llama3-8b-8192"
      stringValue("assistant.voice.voiceId") shouldBeEqualTo "jack"
      stringValue("assistant.voice.emotion") shouldBeEqualTo "male_angry"
      stringValue("assistant.voice.temperature") shouldBeEqualTo "10.0"
    }
  }
}
