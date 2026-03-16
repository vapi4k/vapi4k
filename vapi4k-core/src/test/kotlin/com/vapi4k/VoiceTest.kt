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

import com.github.pambrose.common.json.booleanValue
import com.github.pambrose.common.json.get
import com.github.pambrose.common.json.intValue
import com.github.pambrose.common.json.jsonElementList
import com.github.pambrose.common.json.stringValue
import com.github.pambrose.common.json.toJsonElement
import com.vapi4k.AssistantTest.Companion.newRequestContext
import com.vapi4k.api.model.GroqModelType
import com.vapi4k.api.voice.CartesiaVoiceLanguageType
import com.vapi4k.api.voice.CartesiaVoiceModelType
import com.vapi4k.api.voice.PlayHTVoiceEmotionType
import com.vapi4k.api.voice.PlayHTVoiceIdType
import com.vapi4k.api.voice.PunctuationType
import com.vapi4k.utils.assistantResponse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.jsonArray

class VoiceTest : StringSpec() {
  init {
    "playHt voice basic test" {
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
      members.size shouldBe 1
      members.first().stringValue("assistant.name") shouldBe "Receptionist"
      members.first().stringValue("assistant.firstMessage") shouldBe "Hi there!"
      members.first().stringValue("assistant.model.model") shouldBe "llama3-8b-8192"
      members.first().stringValue("assistant.model.provider") shouldBe "groq"
      members.first().stringValue("assistant.voice.voiceId") shouldBe "matt"
      members.first().stringValue("assistant.voice.emotion") shouldBe "male_sad"
    }

    "playHt voice two or no voiceId error test" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "playHTVoice{} cannot have both voiceIdType and customVoiceId values"

      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "playHTVoice{} requires a voiceIdType or customVoiceId value"
    }

    "cartesia voice two or no models error test" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "cartesiaVoice{} cannot have both modelType and customModel values"

//    shouldThrow<IllegalStateException> {
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
//    }.message shouldBe "cartesiaVoice{} requires a modelType or customModel value"
    }

    "cartesia voice two languages error test" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "cartesiaVoice{} cannot have both languageType and customLanguage values"
    }

    "cartesia voice double model error test" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "groqModel{} already called"
    }

    "cartesia voice double voice error test" {
      shouldThrow<IllegalStateException> {
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
      }.message shouldBe "cartesiaVoice{} already called"
    }

    "chunkPlan serializes as nested JSON" {
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

                  cartesiaVoice {
                    voiceId = "test-voice"
                    chunkPlan {
                      enabled = true
                      minCharacters = 40
                      punctuationBoundaries += PunctuationType.PERIOD
                      punctuationBoundaries += PunctuationType.COMMA
                    }
                  }
                }
              }
            }
          }
        }
      val jsonElement = squad.toJsonElement()
      val voice = jsonElement["messageResponse.squad.members"].jsonArray.first()["assistant.voice"]
      voice.stringValue("provider") shouldBe "cartesia"
      voice.stringValue("voiceId") shouldBe "test-voice"
      voice.booleanValue("chunkPlan.enabled") shouldBe true
      voice.intValue("chunkPlan.minCharacters") shouldBe 40
      voice["chunkPlan.punctuationBoundaries"].jsonArray.size shouldBe 2
    }

    "chunkPlan with nested formatPlan serializes correctly" {
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

                  cartesiaVoice {
                    voiceId = "test-voice"
                    fillerInjectionEnabled = true
                    chunkPlan {
                      enabled = true
                      minCharacters = 50
                      formatPlan {
                        enabled = true
                        numberToDigitsCutoff = 2025
                      }
                    }
                  }
                }
              }
            }
          }
        }
      val jsonElement = squad.toJsonElement()
      val voice = jsonElement["messageResponse.squad.members"].jsonArray.first()["assistant.voice"]
      voice.stringValue("provider") shouldBe "cartesia"
      voice.booleanValue("fillerInjectionEnabled") shouldBe true
      voice.booleanValue("chunkPlan.enabled") shouldBe true
      voice.intValue("chunkPlan.minCharacters") shouldBe 50
      voice.booleanValue("chunkPlan.formatPlan.enabled") shouldBe true
      voice.intValue("chunkPlan.formatPlan.numberToDigitsCutoff") shouldBe 2025
    }

    "double values test" {
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
        stringValue("assistant.firstMessage") shouldBe "Hello!"
        stringValue("assistant.model.model") shouldBe "llama3-8b-8192"
        stringValue("assistant.voice.voiceId") shouldBe "jack"
        stringValue("assistant.voice.emotion") shouldBe "male_angry"
        stringValue("assistant.voice.temperature") shouldBe "10.0"
      }
    }
  }
}
