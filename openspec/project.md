# Project Context

## Purpose

Vapi4k is a Ktor plugin and Kotlin DSL for building voice AI applications with [Vapi.ai](https://vapi.ai). It provides
type-safe builders for configuring assistants, tools, models, voices, and call workflows. The project enables developers
to create inbound call handlers, outbound call initiators, and browser-based voice interactions with compile-time safety
and IDE auto-completion.

## Tech Stack

- **Language:** Kotlin 2.3.0
- **Framework:** Ktor 3.3.3 (with WebSocket support)
- **Build System:** Gradle (multi-module)
- **JVM Target:** 17
- **Serialization:** Kotlinx Serialization (JSON)
- **Testing:** Kluent assertions
- **Database (optional):** HikariCP + PostgreSQL + Exposed ORM
- **Metrics:** Prometheus
- **Documentation:** Dokka

### Module Structure

- `vapi4k-utils` - Foundation module with shared utilities (zero external dependencies)
- `vapi4k-core` - Main module with Ktor plugin, DSL implementations, and Vapi integration
- `vapi4k-dbms` - Optional database persistence for message history
- `vapi4k-snippets` - Example code (not published)

## Project Conventions

### Code Style

- Use `@Vapi4KDslMarker` annotation on DSL builder interfaces for type-safe builders
- Prefix internal implementation properties with underscore (e.g., `_field`)
- Use inline value classes for type safety (e.g., `ApplicationId`, `AssistantId`, `FunctionName`, `CacheKey`)
- API interfaces in `com.vapi4k.api.*` packages
- DSL implementations in `com.vapi4k.dsl.*` packages
- DTOs in `com.vapi4k.dtos.*` packages with `@Serializable` annotation
- Run `./gradlew lintKotlin` before committing; use `./gradlew formatKotlin` to auto-fix

### Architecture Patterns

**Three-Layer Architecture:**

1. **API Layer** (`com.vapi4k.api.*`) - Public interfaces defining the DSL contract
2. **DSL Layer** (`com.vapi4k.dsl.*`) - Concrete implementations with state management and caching
3. **DTO Layer** (`com.vapi4k.dtos.*`) - Kotlinx serialization models for JSON serialization

**Core Patterns:**

- Ktor `ApplicationPlugin` for server configuration (`Vapi4kServer`)
- Request/Response flow through `RequestContext` and `ResponseContext`
- Callback pipeline with `onAllRequests{}`, `onAllResponses{}`, etc.
- Session-based caching with TTL for tool definitions
- Multi-provider abstraction for models (10 providers), voices (9 providers), transcribers (3 providers)

**Three Application Types:**

- `InboundCallApplication` - Handles incoming phone calls (POST endpoint)
- `OutboundCallApplication` - Initiates outgoing calls (GET/POST endpoints)
- `WebApplication` - Browser-based voice interactions (GET/POST endpoints)

### Testing Strategy

- Unit tests use Kluent for assertions
- Test locations:
  - `vapi4k-core/src/test/kotlin/com/vapi4k/` - Unit tests for DSL builders, serialization, utilities
  - `vapi4k-core/src/test/kotlin/simpledemo/` - Example applications and integration tests
- Run specific tests with: `./gradlew :vapi4k-core:test --tests "com.vapi4k.TestClass.testMethod*"`

### Git Workflow

- Main branch: `master`
- Feature branches for development
- Clean commit messages describing the change

## Domain Context

- **Voice AI:** The framework integrates with Vapi.ai to handle voice interactions
- **Assistants:** AI-powered voice assistants with configurable personalities and capabilities
- **Tools:** Three implementation strategies:
  - `ServiceTool` - Annotation-based with `@ToolCall` and `@Param`, auto-generates JSON schemas
  - `ManualTool` - Imperative with `onInvoke{}` blocks
  - `ExternalTool` - Delegates to external HTTP endpoints
- **Request Types:** 14 server request types including `ASSISTANT_REQUEST`, `TOOL_CALL`, `FUNCTION_CALL`,
  `END_OF_CALL_REPORT`, `TRANSCRIPT`, etc.

## Important Constraints

- JVM 17+ required
- Only `com.vapi4k.api.*` packages are included in public documentation
- Admin endpoints disabled when `VAPI4K_PRODUCTION` environment variable is set
- Modules should remain loosely coupled (vapi4k-dbms is completely optional)

## External Dependencies

- **Vapi.ai API** - Core voice AI platform (requires `VAPI_API_PRIVATE_KEY`)
- **AI Model Providers:** OpenAI, Anthropic, Groq, DeepInfra, Anyscale, TogetherAI, OpenRouter, PerplexityAI, CustomLLM,
  Vapi
- **Voice Providers:** OpenAI, ElevenLabs, Deepgram, Azure, Cartesia, PlayHT, RimeAI, LMNT, Neets
- **Transcription Providers:** Deepgram, Gladia, Talkscriber
- **Database (optional):** PostgreSQL via HikariCP connection pool and Exposed ORM
