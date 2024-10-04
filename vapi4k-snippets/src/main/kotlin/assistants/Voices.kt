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

package assistants

import com.vapi4k.api.vapi4k.RequestContext
import com.vapi4k.api.vapi4k.Vapi4kConfig
import com.vapi4k.api.voice.AzureVoiceIdType
import com.vapi4k.api.voice.CartesiaVoiceLanguageType
import com.vapi4k.api.voice.CartesiaVoiceModelType
import com.vapi4k.api.voice.DeepGramVoiceIdType
import com.vapi4k.api.voice.ElevenLabsVoiceModelType
import com.vapi4k.api.voice.LMNTVoiceIdType
import com.vapi4k.api.voice.NeetsVoiceIdType
import com.vapi4k.api.voice.OpenAIVoiceIdType
import com.vapi4k.api.voice.PlayHTVoiceIdType
import com.vapi4k.api.voice.RimeAIVoiceModelType

object Voices {
  fun Vapi4kConfig.azureExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          azureVoice {
            voiceIdType = AzureVoiceIdType.BRIAN
          }
        }
      }
    }
  }

  fun Vapi4kConfig.cartesiaExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          cartesiaVoice {
            modelType = CartesiaVoiceModelType.SONIC_ENGLISH
            languageType = CartesiaVoiceLanguageType.ENGLISH
          }
        }
      }
    }
  }

  fun Vapi4kConfig.deepgramExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          deepgramVoice {
            voiceIdType = DeepGramVoiceIdType.ASTERIA
          }
        }
      }
    }
  }

  fun Vapi4kConfig.elevenLabsExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          elevenLabsVoice {
            modelType = ElevenLabsVoiceModelType.ELEVEN_TURBO_V2_5
          }
        }
      }
    }
  }

  fun Vapi4kConfig.lmntExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          lmntVoice {
            voiceIdType = LMNTVoiceIdType.DANIEL
          }
        }
      }
    }
  }

  fun Vapi4kConfig.neetsExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          neetsVoice {
            voiceIdType = NeetsVoiceIdType.VITS
          }
        }
      }
    }
  }

  fun Vapi4kConfig.openAIExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          openAIVoice {
            voiceIdType = OpenAIVoiceIdType.ONYX
          }
        }
      }
    }
  }

  fun Vapi4kConfig.playHTExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          playHTVoice {
            voiceIdType = PlayHTVoiceIdType.DONNA
          }
        }
      }
    }
  }

  fun Vapi4kConfig.rimeAIExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          rimeAIVoice {
            modelType = RimeAIVoiceModelType.MIST
          }
        }
      }
    }
  }
}
