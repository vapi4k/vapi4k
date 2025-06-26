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
import com.github.pambrose.common.json.keys
import com.github.pambrose.common.json.stringValue
import com.github.pambrose.common.json.toJsonElement
import com.vapi4k.AssistantTest.Companion.newRequestContext
import com.vapi4k.api.model.OpenAIModelType
import com.vapi4k.utils.assistantResponse
import com.vapi4k.utils.tools
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test

class ModelTests {
  @Test
  fun `tool server details`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO
            tools {
              dtmfTool {
                async = false
                onInvoke { }
                server {
                  url = "zzz"
                  secret = "123"
                  timeoutSeconds = 10
                }
              }
            }
          }
        }
      }

    val je = response.toJsonElement()
    val server = je.tools().first()["server"]
    server.stringValue("url") shouldBeEqualTo "zzz"
    server.stringValue("secret") shouldBeEqualTo "123"
    server.stringValue("timeoutSeconds") shouldBeEqualTo "10"
    je.tools().first().booleanValue("async").shouldBeFalse()
  }

  @Test
  fun `missing external tool name blocks`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO

            tools {
              externalTool {
                server {
                  url = "yyy"
                  secret = "456"
                  timeoutSeconds = 5
                }
              }
            }
          }
        }
      }
    }.also {
      it.message shouldBeEqualTo "externalTool{} parameter name is required"
    }
  }

  @Test
  fun `blank external tool name blocks`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO

            tools {
              externalTool {
                name = ""

                server {
                  url = "yyy"
                  secret = "456"
                  timeoutSeconds = 5
                }
              }
            }
          }
        }
      }
    }.also {
      it.message shouldBeEqualTo "externalTool{} parameter name is required"
    }
  }

  @Test
  fun `Check description with external tool decl`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO

            tools {
              externalTool {
                name = "wed"
                description = "a description"
                async = true
//                onInvoke { args ->
//
//                }
                server {
                  url = "yyy"
                  secret = "456"
                  timeoutSeconds = 5
                }
              }
            }
          }
        }
      }
    val je = response.toJsonElement()
    val tool = je.tools().first()
    tool["function"].stringValue("name") shouldBeEqualTo "wed"
    tool["function"].stringValue("description") shouldBeEqualTo "a description"
    tool.stringValue("async") shouldBeEqualTo "true"
  }

  @Test
  fun `blank param names with external tool decl`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO

            tools {
              externalTool {
                name = "wed"
                description = "a description"
                async = true

                parameters {
                  parameter {
                    name = ""
                  }
                }

                server {
                  url = "yyy"
                  secret = "456"
                  timeoutSeconds = 5
                }
              }
            }
          }
        }
      }
    }.also {
      it.message shouldBeEqualTo "Parameter name must be assigned in externalTool{}"
    }
  }

  @Test
  fun `Bad type with external tool decl`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO

            tools {
              externalTool {
                name = "wed"
                description = "a description"
                async = true

                parameters {
                  parameter {
                    name = "param1"
                    description = "a description"
                    type = Any::class
                  }
                }

                server {
                  url = "yyy"
                  secret = "456"
                  timeoutSeconds = 5
                }
              }
            }
          }
        }
      }
    }.also {
      assert(it.message.orEmpty().contains("externalTool{} parameter type must be one of"))
    }
  }

  @Test
  fun `Duplicate param names external tool decl`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO

            tools {
              externalTool {
                name = "wed"
                description = "a description"
                async = true

                parameters {
                  parameter {
                    name = "param1"
                    description = "a description"
                  }
                  parameter {
                    name = "param1"
                    description = "a description"
                  }
                }

                server {
                  url = "yyy"
                  secret = "456"
                  timeoutSeconds = 5
                }
              }
            }
          }
        }
      }
    }.also {
      assert(it.message.orEmpty().contains("already exists"))
    }
  }

  @Test
  fun `check parameters with external tool decl`() {
    val response =
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO

            tools {
              externalTool {
                name = "wed"
                description = "a description"
                async = true

                parameters {
                  parameter {
                    name = "param1"
                    description = "a description"
                  }
                  parameter {
                    name = "param2"
                    description = "a description"
                  }
                  parameter {
                    name = "param3"
                    description = "a description"
                    type = Int::class
                  }
                }

                server {
                  url = "yyy"
                  secret = "456"
                  timeoutSeconds = 5
                }
              }
            }
          }
        }
      }
    val je = response.toJsonElement()
    val tool = je.tools().first()
    tool["function.parameters.properties"].keys.size shouldBeEqualTo 3
  }

  @Test
  fun `multiple server with external tool decl`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO

            tools {
              externalTool {
                name = "wed"

                server {
                  url = "yyy"
                  secret = "456"
                  timeoutSeconds = 5
                }
                server {
                  url = "yyy"
                  secret = "456"
                  timeoutSeconds = 5
                }
              }
            }
          }
        }
      }
    }.also {
      it.message shouldBeEqualTo "externalTool{} contains multiple calls to server{}"
    }
  }

  @Test
  fun `missing server with external tool decl`() {
    assertThrows(IllegalStateException::class.java) {
      assistantResponse(newRequestContext()) {
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_3_5_TURBO

            tools {
              externalTool {
                name = "wed"
              }
            }
          }
        }
      }
    }.also {
      it.message shouldBeEqualTo "externalTool{} must contain a call to server{}"
    }
  }
}
