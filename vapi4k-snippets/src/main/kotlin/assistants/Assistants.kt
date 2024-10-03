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

import com.vapi4k.api.model.enums.OpenAIModelType
import com.vapi4k.api.vapi4k.RequestContext
import com.vapi4k.api.vapi4k.Vapi4kConfig

object Assistants {
  fun Vapi4kConfig.assistantIdExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistantId {
          id = "41ba80bc-807c-4cf5-a8c3-0a88a5a5882g"

          assistantOverrides {
            // Define the assistant overrides here
          }
        }
      }
    }
  }

  fun Vapi4kConfig.assistantExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_4O
            systemMessage = "You are a helpful agent."
          }
          firstMessage = "Hello, how can I help you today?"
        }
      }
    }
  }

  fun Vapi4kConfig.squadExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        squad {
          members {
            member {
              assistant {
                name = "Assistant 1"
                // Define assistant1 here
              }
              destinations {
                destination {
                  assistantName = "Assistant 2"
                }
              }
            }
            member {
              assistant {
                name = "Assistant 2"
                // Define assistant2 here
              }
              destinations {
                destination {
                  assistantName = "Assistant 1"
                }
              }
            }
          }
        }
      }
    }
  }

  fun Vapi4kConfig.squadIdExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        squadId {
          id = "51ba90bc-807c-4cf5-a8c4-1a88a5a5882h"
        }
      }
    }
  }
}
