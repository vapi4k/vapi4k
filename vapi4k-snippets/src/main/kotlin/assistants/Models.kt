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

import com.vapi4k.api.model.AnthropicModelType
import com.vapi4k.api.model.GroqModelType
import com.vapi4k.api.model.OpenAIModelType
import com.vapi4k.api.vapi4k.RequestContext
import com.vapi4k.api.vapi4k.Vapi4kConfig

object Models {
  fun Vapi4kConfig.anthropicExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          anthropicModel {
            modelType = AnthropicModelType.CLAUDE_3_HAIKU_20240307
            emotionRecognitionEnabled = true
            maxTokens = 250
            temperature = 0.5
            systemMessage = "You're a polite AI assistant named Vapi who is fun to talk with."
          }
        }
      }
    }
  }

  fun Vapi4kConfig.anyscaleExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          anyscaleModel {
            model = "Model_Description"
            emotionRecognitionEnabled = true
            maxTokens = 250
            temperature = 0.5
            systemMessage = "You're a polite AI assistant named Vapi who is fun to talk with."
          }
        }
      }
    }
  }

  fun Vapi4kConfig.customLLMExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          customLLMModel {
            model = "Model_Description"
            url = "Model_URL"
            emotionRecognitionEnabled = true
            maxTokens = 250
            temperature = 0.5
            systemMessage = "You're a polite AI assistant named Vapi who is fun to talk with."
          }
        }
      }
    }
  }

  fun Vapi4kConfig.deepInfraExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          deepInfraModel {
            model = "Model_Description"
            emotionRecognitionEnabled = true
            maxTokens = 250
            temperature = 0.5
            systemMessage = "You're a polite AI assistant named Vapi who is fun to talk with."
          }
        }
      }
    }
  }

  fun Vapi4kConfig.groqExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          groqModel {
            modelType = GroqModelType.LLAMA3_70B_8192
            emotionRecognitionEnabled = true
            maxTokens = 250
            temperature = 0.5
            systemMessage = "You're a polite AI assistant named Vapi who is fun to talk with."
          }
        }
      }
    }
  }

  fun Vapi4kConfig.openAIExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_4_TURBO
            semanticCachingEnabled = true
            emotionRecognitionEnabled = true
            maxTokens = 250
            temperature = 0.5
            fallbackModelTypes += OpenAIModelType.GPT_4O
            systemMessage = "You're a polite AI assistant named Vapi who is fun to talk with."
          }
        }
      }
    }
  }

  fun Vapi4kConfig.openRouterExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          openRouterModel {
            model = "Model_Description"
            emotionRecognitionEnabled = true
            maxTokens = 250
            temperature = 0.5
            systemMessage = "You're a polite AI assistant named Vapi who is fun to talk with."
          }
        }
      }
    }
  }

  fun Vapi4kConfig.perplexityAIExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          perplexityAIModel {
            model = "Model_Description"
            emotionRecognitionEnabled = true
            maxTokens = 250
            temperature = 0.5
            systemMessage = "You're a polite AI assistant named Vapi who is fun to talk with."
          }
        }
      }
    }
  }

  fun Vapi4kConfig.togetherAIExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          togetherAIModel {
            model = "Model_Description"
            emotionRecognitionEnabled = true
            maxTokens = 250
            temperature = 0.5
            systemMessage = "You're a polite AI assistant named Vapi who is fun to talk with."
          }
        }
      }
    }
  }

  fun Vapi4kConfig.vapiExample() {
    inboundCallApplication {
      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          vapiModel {
            model = "Model_Description"
            emotionRecognitionEnabled = true
            maxTokens = 250
            temperature = 0.5
            systemMessage = "You're a polite AI assistant named Vapi who is fun to talk with."
          }
        }
      }
    }
  }
}
