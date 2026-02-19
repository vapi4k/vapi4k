# Vapi4k

[![Release](https://jitpack.io/v/vapi4k/vapi4k.svg)](https://jitpack.io/#vapi4k/vapi4k)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=vapi4k_vapi4k&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=vapi4k_vapi4k)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=vapi4k_vapi4k&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=vapi4k_vapi4k)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/2ec91457b7814a73a7ac70b9e1290f1e)](https://app.codacy.com/gh/vapi4k/vapi4k/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Kotlin version](https://img.shields.io/badge/kotlin-2.2.0-red?logo=kotlin)](http://kotlinlang.org)
[![ktlint](https://img.shields.io/badge/ktlint%20code--style-%E2%9D%A4-FF4081)](https://pinterest.github.io/ktlint/)
![build workflow](https://github.com/vapi4k/vapi4k/actions/workflows/build-all-docs.yml/badge.svg?branch=docs)

Vapi4k is a [Ktor](https://ktor.io) plugin and a [Kotlin DSL](https://kotlinlang.org/docs/type-safe-builders.html)
that makes it easy to define, deploy, and maintain [Vapi](https://vapi.ai) voice AI applications.

## Features

- **Type-safe Kotlin DSL** for configuring assistants, tools, models, voices, and transcribers
- **Three application types**: inbound calls, outbound calls, and browser-based web interactions
- **Multi-provider support**: 10 model providers (OpenAI, Anthropic, Groq, etc.), 9 voice providers (ElevenLabs,
  Deepgram, etc.), and 3 transcriber providers
- **Flexible tool system**: annotation-based service tools, imperative manual tools, and external HTTP tools
- **Squad support**: multi-assistant configurations with transfer destinations
- **Built-in admin dashboard** with cache inspection, live log streaming, and tool validation (non-production)
- **Optional database persistence** for message history (PostgreSQL via Exposed ORM)

## Installation

Add the JitPack repository and dependency to your `build.gradle.kts`:

```kotlin
repositories {
  maven("https://jitpack.io")
}

dependencies {
  implementation("com.github.vapi4k.vapi4k:vapi4k-core:1.3.8")

  // Optional: database persistence
  implementation("com.github.vapi4k.vapi4k:vapi4k-dbms:1.3.8")
}
```

## Quick Start

Install the `Vapi4k` plugin in your Ktor application and define an inbound call handler:

```kotlin
fun Application.module() {
  install(Vapi4k) {
    inboundCallApplication {
      serverPath = "/inboundApp"
      serverSecret = "12345"

      onAssistantRequest { requestContext: RequestContext ->
        assistant {
          firstMessage = "Hello! How can I help you today?"

          openAIModel {
            modelType = OpenAIModelType.GPT_4_TURBO
            systemMessage = "You're a polite AI assistant."
          }

          deepgramVoice {
            voiceIdType = DeepGramVoiceIdType.LUNA
          }
        }
      }
    }
  }
}
```

### Adding Tools

Define tools using the `@ToolCall` and `@Param` annotations:

```kotlin
class WeatherService {
  @ToolCall("Look up the weather for a city")
  fun getWeather(
    @Param("City name") city: String,
    @Param("State name") state: String,
  ) = "The weather in $city, $state is sunny"
}
```

Then register tools on a model:

```kotlin
openAIModel {
  modelType = OpenAIModelType.GPT_4_TURBO
  systemMessage = "You are a helpful weather assistant."
  tools {
    serviceTool(WeatherService())
  }
}
```

## Modules

| Module           | Description                                                     |
|------------------|-----------------------------------------------------------------|
| **vapi4k-core**  | Ktor plugin, DSL, and Vapi integration                          |
| **vapi4k-dbms**  | Optional database persistence (HikariCP + PostgreSQL + Exposed) |
| **vapi4k-utils** | Shared utilities with zero external dependencies                |

## Documentation

- [Vapi4k Documentation](https://vapi4k.com/overview.html)
- [KDocs](https://vapi4k.com/kdocs/)
- [Getting Started Template](https://github.com/vapi4k/vapi4k-template)
