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

import com.vapi4k.api.json.get
import com.vapi4k.api.json.jsonElementList
import com.vapi4k.api.json.stringValue
import com.vapi4k.api.json.toJsonElement
import com.vapi4k.utils.JsonUtils.toObjectList
import kotlinx.serialization.Serializable
import kotlin.test.Test

class JsonExtensionTest {
  val json = """
    {
      "message": {
        "type": "tool-calls",
        "toolCalls": [
          {
            "id": "call_fzEwCjWnJsuwoeGQGbITcnpw",
            "type": "function",
            "function": {
              "name": "WeatherLookupService0--getWeatherOutside",
              "arguments": {}
            }
          }
        ],
        "toolCallList": [
          {
            "id": "call_fzEwCjWnJsuwoeGQGbITcnpw",
            "type": "function",
            "function": {
              "name": "WeatherLookupService0--getWeatherOutside",
              "arguments": {}
            }
          }
        ],
        "toolWithToolCallList": [
          {
            "type": "function",
            "function": {
              "name": "WeatherLookupService0--getWeatherOutside",
              "description": "Look up the weather outside",
              "parameters": {
                "type": "object",
                "properties": {}
              }
            },
            "async": false,
            "server": {
              "url": "https://myapp.ondigitalocean.app/toolCall",
              "secret": "12345",
              "timeoutSeconds": 20
            },
            "messages": [
              {
                "content": "This is the request start message",
                "type": "request-start"
              },
              {
                "content": "This is the request complete message",
                "type": "request-complete"
              },
              {
                "content": "This is the request failed message",
                "type": "request-failed"
              },
              {
                "content": "This is the request delayed message",
                "type": "request-response-delayed",
                "timingMilliseconds": 2000
              }
            ],
            "toolCall": {
              "id": "call_fzEwCjWnJsuwoeGQGbITcnpw",
              "type": "function",
              "function": {
                "name": "WeatherLookupService0--getWeatherOutside",
                "arguments": {}
              }
            }
          }
        ],
        "call": {
          "id": "6b38c32e-6d5c-4d10-bce5-951581ddcd77",
          "orgId": "379a13ec-f40d-4055-8959-797c4ee1694s",
          "createdAt": "2024-07-04T05:38:46.478Z",
          "updatedAt": "2024-07-04T05:38:46.478Z",
          "type": "inboundPhoneCall",
          "status": "ringing",
          "phoneCallProvider": "twilio",
          "phoneCallProviderId": "CAe519bb3b2aa4c6177f0bb5d8054422a3",
          "phoneCallTransport": "pstn",
          "phoneNumberId": "4a5a04dc-dcbe-45b1-8f64-fd32a253d136",
          "customer": {
            "number": "+14566836490"
          },
          "assistant": {
            "model": {
              "model": "gpt-3.5-turbo",
              "messages": [
                {
                  "content": "You are the friendly and helpful voice of EO Care, here to assist customers with their frequently asked questions about EO Care. Your main task is to provide support through audio interactions and answer questions. Be sure not to give medical advice since you aren't qualified to do that. Remember, customers can't see you, so your words need to paint the picture clearly and warmly.\nWhen interacting, listen carefully for cues about the customer's mood and the context of their questions.\nIf a customer asks if you're listening, reassure them with a prompt and friendly acknowledgment and include the word \"frankly\". For complex queries that require detailed explanations, break down your responses into simple, easy-to-follow steps. Your goal is to make every customer feel heard, supported, and satisfied with the service.\n\nIf the customer says the word \"bananas\", interrupt and immediately respond with \"are delicious\".\n\n",
                  "role": "system"
                }
              ],
              "tools": [
                {
                  "async": false,
                  "function": {
                    "name": "WeatherLookupService0--getWeatherOutside",
                    "description": "Look up the weather outside",
                    "parameters": {
                      "type": "object",
                      "properties": {}
                    }
                  },
                  "messages": [
                    {
                      "content": "This is the request start message",
                      "type": "request-start"
                    },
                    {
                      "content": "This is the request complete message",
                      "type": "request-complete"
                    },
                    {
                      "content": "This is the request failed message",
                      "type": "request-failed"
                    },
                    {
                      "content": "This is the request delayed message",
                      "type": "request-response-delayed",
                      "timingMilliseconds": 2000
                    }
                  ],
                  "server": {
                    "url": "https://myapp.ondigitalocean.app/toolCall",
                    "secret": "12345",
                    "timeoutSeconds": 20
                  },
                  "type": "function"
                }
              ],
              "provider": "openai"
            },
            "firstMessage": "Hi there. My name is Bob. I am here to help you with your queries."
          }
        },
        "phoneNumber": {
          "id": "4a5a04dc-dcbe-45b1-8f64-fd32a253d136",
          "orgId": "379a13ec-f40d-4055-8959-797c4ee1694s",
          "number": "+19254207621",
          "createdAt": "2024-06-29T03:03:00.576Z",
          "updatedAt": "2024-06-29T03:04:32.131Z",
          "stripeSubscriptionId": "sub_1PWrYyCRkod4mKy33dFxM9B8",
          "stripeSubscriptionStatus": "active",
          "stripeSubscriptionCurrentPeriodStart": "2024-06-29T03:02:56.000Z",
          "provider": "twilio"
        },
        "customer": {
          "number": "+14566836490"
        },
        "artifact": {
          "messages": [
            {
              "role": "bot",
              "message": "Hi there. My name is Bob. I am here to help you with your queries.",
              "time": 1720071529727,
              "endTime": 1720071533657,
              "secondsFromStart": 1.04,
              "source": ""
            },
            {
              "role": "user",
              "message": "What is the weather outside?",
              "time": 1720071535127,
              "endTime": 1720071536607,
              "secondsFromStart": 6.44,
              "duration": 2.52
            },
            {
              "toolCalls": [
                {
                  "id": "call_Q9OmH6CEgazy0kvNJk3xhvfh",
                  "type": "function",
                  "function": {
                    "name": "WeatherLookupService0--getWeatherOutside",
                    "arguments": "{}"
                  }
                }
              ],
              "role": "tool_calls",
              "message": "",
              "time": 1720071535790,
              "secondsFromStart": 7.499
            },
            {
              "role": "tool_call_result",
              "time": 1720071536223,
              "secondsFromStart": 7.932,
              "name": "WeatherLookupService0--getWeatherOutside",
              "result": "No result returned.",
              "toolCallId": "call_Q9OmH6CEgazy0kvNJk3xhvfh"
            },
            {
              "role": "user",
              "message": "Outside.",
              "time": 1720071536767,
              "endTime": 1720071537267,
              "secondsFromStart": 8.08,
              "duration": 0.8199997
            },
            {
              "toolCalls": [
                {
                  "id": "call_fzEwCjWnJsuwoeGQGbITcnpw",
                  "type": "function",
                  "function": {
                    "name": "WeatherLookupService0--getWeatherOutside",
                    "arguments": "{}"
                  }
                }
              ],
              "role": "tool_calls",
              "message": "",
              "time": 1720071550216,
              "secondsFromStart": 21.925
            },
            {
              "role": "user",
              "message": "Hello?",
              "time": 1720071550457,
              "endTime": 1720071550867,
              "secondsFromStart": 21.77,
              "duration": 0.8199997
            }
          ],
          "messagesOpenAIFormatted": [
            {
              "role": "assistant",
              "content": "Hi there. My name is Bob. I am here to help you with your queries."
            },
            {
              "role": "user",
              "content": "What is the weather outside?"
            },
            {
              "role": "assistant",
              "content": "",
              "tool_calls": [
                {
                  "type": "function",
                  "id": "call_Q9OmH6CEgazy0kvNJk3xhvfh",
                  "function": {
                    "name": "WeatherLookupService0--getWeatherOutside",
                    "arguments": "{}"
                  }
                }
              ]
            },
            {
              "role": "tool",
              "tool_call_id": "call_Q9OmH6CEgazy0kvNJk3xhvfh",
              "content": "No result returned."
            },
            {
              "role": "user",
              "content": "Outside."
            },
            {
              "role": "assistant",
              "content": "",
              "tool_calls": [
                {
                  "type": "function",
                  "id": "call_fzEwCjWnJsuwoeGQGbITcnpw",
                  "function": {
                    "name": "WeatherLookupService0--getWeatherOutside",
                    "arguments": "{}"
                  }
                }
              ]
            }
          ]
        },
        "timestamp": "2024-07-04T05:39:10.225Z"
      }
    }
  """.trimIndent()

  @Test
  fun testStringValue() {
    val obj = json.toJsonElement()
    assert(obj["message"]["type"].stringValue == "tool-calls")
    assert(obj.stringValue("message.type") == "tool-calls")
  }

  @Test
  fun testPathVarargs() {
    val obj = json.toJsonElement()
    assert(obj["message", "type"].stringValue == "tool-calls")
    assert(obj.stringValue("message.type") == "tool-calls")
  }

  @Test
  fun testPathString() {
    val obj = json.toJsonElement()
    assert(obj.stringValue("message.type") == "tool-calls")
  }

  @Test
  fun testArrayValues() {
    val obj = json.toJsonElement()
    assert(obj.jsonElementList("message.toolWithToolCallList").size == 1)
  }

  @Test
  fun testOnceAgain() {
    val str = """
      {
        "async": false,
        "servers": [
          {
            "url": "https://myapp.ondigitalocean.app/toolCall1",
            "secret": "12345"
          },
          {
            "url": "https://myapp.ondigitalocean.app/toolCall2",
            "secret": "12345"
          },
          {
            "url": "https://myapp.ondigitalocean.app/toolCall3",
            "secret": "12345"
          }
        ]
      }
    """.trimIndent()

    @Serializable
    class Server(
      val url: String,
      val secret: String,
    )

    val jsonElement = str.toJsonElement()
    val servers = jsonElement["servers"].toObjectList<Server>()
//    for (server in servers) {
//      println(server.url)
//    }
  }
}
