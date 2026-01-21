<!-- OPENSPEC:START -->

# OpenSpec Instructions

These instructions are for AI assistants working in this project.

Always open `@/openspec/AGENTS.md` when the request:

- Mentions planning or proposals (words like proposal, spec, change, plan)
- Introduces new capabilities, breaking changes, architecture shifts, or big performance/security work
- Sounds ambiguous and you need the authoritative spec before coding

Use `@/openspec/AGENTS.md` to learn:

- How to create and apply change proposals
- Spec format and conventions
- Project structure and guidelines

Keep this managed block so 'openspec update' can refresh the instructions.

<!-- OPENSPEC:END -->

# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Vapi4k is a Ktor plugin and Kotlin DSL for building voice AI applications with [Vapi.ai](https://vapi.ai). It provides
type-safe builders for configuring assistants, tools, models, voices, and call workflows.

**Version:** 1.3.3
**Kotlin:** 2.3.0
**Ktor:** 3.3.3
**JVM Target:** 17

## Module Structure

This is a multi-module Gradle project with the following modules:

- **vapi4k-utils** - Foundation module with shared utilities and zero external dependencies (besides Kotlin stdlib,
  logging, and JSON)
- **vapi4k-core** - Main module containing the Ktor plugin, DSL implementations, and Vapi integration
- **vapi4k-dbms** - Optional database persistence module for message history (HikariCP + PostgreSQL + Exposed ORM)
- **vapi4k-snippets** - Example code demonstrating usage patterns (not published as a library)

**Dependencies:**

- `vapi4k-core` depends on `vapi4k-utils`
- `vapi4k-dbms` depends on `vapi4k-utils`
- `vapi4k-snippets` depends on `vapi4k-core`

## Build Commands

```bash
# Build all modules
./gradlew build

# Run tests
./gradlew test

# Run tests for a specific module
./gradlew :vapi4k-core:test

# Run a single test class
./gradlew :vapi4k-core:test --tests "com.vapi4k.ApplicationTest"

# Run a single test method (use * for spaces in test names)
./gradlew :vapi4k-core:test --tests "com.vapi4k.ApplicationTest.test for serverPath*"

# Lint Kotlin code
./gradlew lintKotlin

# Format Kotlin code
./gradlew formatKotlin

# Generate API documentation (outputs to build/kdocs)
./gradlew dokkaGenerate

# Clean build artifacts
./gradlew clean

# Check for dependency updates
./gradlew dependencyUpdates

# Publish to Maven Local
./gradlew publishToMavenLocal
```

## Architecture

### Type-Safe DSL Pattern

The codebase uses the `@Vapi4KDslMarker` annotation extensively (79+ files) to create a type-safe builder DSL. This
provides compile-time safety and IDE auto-completion.

**Three-Layer Architecture:**

1. **API Layer** (`com.vapi4k.api.*`) - Public interfaces defining the DSL contract
2. **DSL Layer** (`com.vapi4k.dsl.*`) - Concrete implementations with state management and caching
3. **DTO Layer** (`com.vapi4k.dtos.*`) - Kotlinx serialization models for JSON serialization to Vapi API format

### Ktor Plugin Architecture

The core architectural component is `Vapi4kServer`, a Ktor `ApplicationPlugin` that:

- Automatically configures Ktor server with WebSocket support
- Sets up routing for inbound/outbound calls and web interactions
- Manages lifecycle events (starting, started, stopping, stopped)
- Provides admin endpoints in non-production environments
- Integrates Prometheus metrics

**Location:** `vapi4k-core/src/main/kotlin/com/vapi4k/plugin/Vapi4kServer.kt`

### Three Application Types

The framework supports three distinct application types (all inheriting from `AbstractApplicationImpl`):

1. **InboundCallApplication** - Handles incoming phone calls from Vapi (POST endpoint)
2. **OutboundCallApplication** - Initiates outgoing calls (GET and POST endpoints)
3. **WebApplication** - Browser-based voice interactions (GET and POST endpoints)

**Location:** `vapi4k-core/src/main/kotlin/com/vapi4k/api/vapi4k/Vapi4kConfig.kt`

### Request/Response Flow

Every request flows through:

1. **RequestContext** - Contains session ID, timestamp, request type, phone number metadata
2. **ServerRequestType** - Enum with 14 message types:
  - `ASSISTANT_REQUEST`, `TOOL_CALL`, `FUNCTION_CALL`, `END_OF_CALL_REPORT`
  - `CONVERSATION_UPDATE`, `STATUS_UPDATE`, `TRANSCRIPT`, `SPEECH_UPDATE`
  - `TRANSFER_DESTINATION_REQUEST`, `HANG`, `USER_INTERRUPTED`, `ASSISTANT_STARTED`, `PHONE_CALL_CONTROL`,
    `UNKNOWN_REQUEST_TYPE`
3. **ResponseContext** - Wraps response with timing and request reference

**Location:** `vapi4k-utils/src/main/kotlin/com/vapi4k/api/vapi4k/ServerRequestType.kt`

### Callback Pipeline

`AbstractApplicationImpl` (`vapi4k-core/src/main/kotlin/com/vapi4k/dsl/vapi4k/AbstractApplicationImpl.kt`) provides
callback hooks:

- `onAllRequests{}` / `onRequest(type){}` - Request callbacks
- `onAllResponses{}` / `onResponse(type){}` - Response callbacks
- `onTransferDestinationRequest{}` - Transfer handling

### Tool System

Three tool implementation strategies:

1. **ServiceTool** - Annotation-based tools using `@ToolCall` and `@Param`, auto-generates JSON schemas:

```kotlin
class WeatherService {
  @ToolCall("Look up weather for a city")
  fun getWeather(@Param("City name") city: String): String { ... }
}
```

2. **ManualTool** - Imperative tools with `onInvoke{}` blocks for full control
3. **ExternalTool** - Delegates to external HTTP endpoints

All tools extend `BaseTool` and are cached in `ServiceCache` with session-based TTL. Caches are cleared on
end-of-call-report.

### Multi-Provider Abstraction

The architecture abstracts multiple AI providers:

- **Models** (10 providers): OpenAI, Anthropic, Groq, DeepInfra, Anyscale, TogetherAI, OpenRouter, PerplexityAI,
  CustomLLM, Vapi
- **Voices** (9 providers): OpenAI, ElevenLabs, Deepgram, Azure, Cartesia, PlayHT, RimeAI, LMNT, Neets
- **Transcribers** (3 providers): Deepgram, Gladia, Talkscriber

## Environment Variables

Production detection:

- `VAPI4K_PRODUCTION` - Set to disable admin endpoints

Core configuration:

- `VAPI4K_BASE_URL` - Server base URL
- `VAPI4K_SERVER_PATH` - Default server path
- `VAPI_API_PRIVATE_KEY` - Vapi API key
- `VAPI_PHONE_NUMBER_ID` - Phone number ID

Database configuration (optional, for vapi4k-dbms):

- `DBMS_DRIVER_CLASSNAME`
- `DBMS_URL`
- `DBMS_USERNAME`
- `DBMS_PASSWORD`
- `DBMS_MAX_POOL_SIZE`
- `DBMS_MAX_LIFETIME_MINS`

## Admin Endpoints (Non-Production)

When `VAPI4K_PRODUCTION` is not set, the following admin endpoints are available:

- `/admin` - Admin dashboard (basic auth)
- `/caches` - Cache inspection
- `/clear-caches` - Cache clearing
- `/invokeTool` - Tool invocation UI
- `/env` - Environment variable viewer
- `/admin-log` - WebSocket log streaming

## Code Organization Conventions

- **API interfaces** should be defined in `com.vapi4k.api.*` packages
- **DSL implementations** should be in `com.vapi4k.dsl.*` packages
- **DTOs** should be in `com.vapi4k.dtos.*` packages with `@Serializable` annotation
- Use `@Vapi4KDslMarker` on DSL builder interfaces
- Prefix internal implementation properties with underscore (e.g., `_field`)
- Use inline value classes for type safety (e.g., `ApplicationId`, `AssistantId`, `FunctionName`, `CacheKey`)

## Key Design Principles

1. **Type Safety First** - Leverage Kotlin's type system with DSL markers, inline value classes, and sealed hierarchies
2. **Clean Separation** - API (interfaces) → DSL (implementation) → DTOs (serialization)
3. **Optional Dependencies** - Keep modules loosely coupled (e.g., vapi4k-dbms is completely optional)
4. **Caching with TTL** - Built-in caching for tool definitions and function metadata
5. **Session Isolation** - Each application instance has a unique ID for multi-tenancy
6. **Observability** - Prometheus metrics, structured logging, admin dashboard

## Testing

Tests use Kluent for assertions. Key test locations:

- `vapi4k-core/src/test/kotlin/com/vapi4k/` - Unit tests for DSL builders, serialization, utilities
- `vapi4k-core/src/test/kotlin/simpledemo/` - Example applications and integration tests

## Documentation

- Main docs: [vapi4k.com](https://vapi4k.com/overview.html)
- KDocs: Generated to `build/kdocs/` via `./gradlew dokkaGenerate`
- Only `com.vapi4k.api.*` packages are included in generated documentation (configured in root `build.gradle.kts`)

## Publishing

Published to JitPack:

- Group: `com.github.vapi4k`
- Artifacts: `vapi4k-core`, `vapi4k-dbms`, `vapi4k-utils`
- All published modules include sources JAR
