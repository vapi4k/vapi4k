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

import com.vapi4k.api.transcriber.enums.DeepgramLanguageType
import com.vapi4k.api.transcriber.enums.DeepgramModelType
import com.vapi4k.api.transcriber.enums.GladiaLanguageType
import com.vapi4k.api.transcriber.enums.GladiaModelType
import com.vapi4k.api.transcriber.enums.TalkscriberLanguageType
import com.vapi4k.api.transcriber.enums.TalkscriberModelType
import com.vapi4k.api.vapi4k.RequestContext
import com.vapi4k.api.vapi4k.Vapi4kConfig

object Transcribers {
  fun Vapi4kConfig.deepgramExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          deepgramTranscriber {
            transcriberModel = DeepgramModelType.NOVA_MEDICAL
            transcriberLanguage = DeepgramLanguageType.INDONESIAN
          }
        }
      }
    }
  }

  fun Vapi4kConfig.gladiaExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          gladiaTranscriber {
            transcriberModel = GladiaModelType.FAST
            transcriberLanguage = GladiaLanguageType.ICELANDIC
          }
        }
      }
    }
  }

  fun Vapi4kConfig.talkscriberExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          talkscriberTranscriber {
            transcriberModel = TalkscriberModelType.WHISPER
            transcriberLanguage = TalkscriberLanguageType.VIETNAMESE
          }
        }
      }
    }
  }
}
