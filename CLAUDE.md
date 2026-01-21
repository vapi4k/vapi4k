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

Vapi4k is a Ktor plugin and Kotlin DSL for defining, deploying, and maintaining Vapi (vapi.ai) voice AI applications. It
provides type-safe builders for creating voice assistants, handling tool calls, and managing real-time voice
interactions through a clean Kotlin API.

## Build and Development Commands

### Building

```bash
# Clean build (excludes tests)
./gradlew build -x test

# Full build with tests
./gradlew build

# Continuous build (watches for changes)
./gradlew -t build -x test

# Clean everything
./gradlew clean
# or
make clean
```

### Testing

```bash
# Run all tests
./gradlew test

# Run tests with full output
./gradlew --rerun-tasks check

# Run tests in a specific module
./gradlew :vapi4k-core:test
./gradlew :vapi4k-utils:test

# Run a single test class
./gradlew :vapi4k-core:test --tests "com.vapi4k.ServerTest"

# Run a specific test method
./gradlew :vapi4k-core:test --tests "com.vapi4k.ServerTest.testSimpleAssistantRequest"
```

### Code Quality

```bash
# Run ktlint checks
./gradlew lintKotlin

# Format code with ktlint
./gradlew formatKotlin

# Check for dependency updates
./gradlew dependencyUpdates
# or
make versioncheck
```

### Documentation

```bash
# Generate KDocs (HTML)
./gradlew dokkaGenerate
# or
make kdocs

# Generate markdown docs
./gradlew dokkaGfm
# or
make mddocs
```

### Publishing

```bash
# Publish to Maven Local for testing
./gradlew publishToMavenLocal
# or
make publish
```

## Module Architecture

The project consists of four Gradle modules with distinct responsibilities:

### vapi4k-core

The primary module containing the complete Ktor plugin, DSL framework, and Vapi API integration.

- **Dependencies**: vapi4k-utils, Ktor (client & server), kotlinx.serialization, Micrometer, Exposed ORM
- **Key packages**:
  - `com.vapi4k.api.*` - Public DSL interfaces (the developer-facing API)
  - `com.vapi4k.dsl.*` - Implementation classes with builder pattern
  - `com.vapi4k.dtos.*` - Serializable data transfer objects for JSON
  - `com.vapi4k.plugin.*` - Ktor plugin definition and configuration
  - `com.vapi4k.server.*` - HTTP handling, routing, and callbacks

### vapi4k-utils

Foundation layer providing cross-cutting utilities.

- **Dependencies**: Only external libraries (no internal module dependencies)
- **Purpose**: JSON handling, logging, misc utilities shared across modules

### vapi4k-dbms

Optional PostgreSQL/JDBC integration using Exposed ORM.

- **Dependencies**: vapi4k-utils, Exposed, HikariCP, PostgreSQL driver
- **Purpose**: Database persistence for applications needing storage

### vapi4k-snippets

Code examples and demonstration implementations.

- **Dependencies**: vapi4k-core
- **Purpose**: Example implementations showing DSL usage patterns

## Core Architectural Patterns

### Three-Layer DSL Pattern

The DSL follows a consistent pattern across all components:

1. **API Layer** (`com.vapi4k.api.*`) - Public interfaces defining the DSL shape
  - Example: `Assistant`, `Tools`, `OpenAIModel`
  - These are what developers interact with

2. **Implementation Layer** (`com.vapi4k.dsl.*`) - Builder classes that collect state
  - Example: `AssistantImpl`, `ToolsImpl`, `OpenAIModelImpl`
  - Use Kotlin delegation to delegate properties to DTOs

3. **DTO Layer** (`com.vapi4k.dtos.*`) - Serializable classes for JSON output
  - Example: `AssistantDto`, `ToolDto`, `OpenAIModelDto`
  - Annotated with `@Serializable` for kotlinx.serialization

**Data Flow**: Developer uses API → Implementation builds state → DTOs serialize to JSON

### Application Types and Request Handling

Three application classes handle different interaction patterns:

| Type                      | Purpose                      | Endpoint Pattern             | Main Request Types                                |
|---------------------------|------------------------------|------------------------------|---------------------------------------------------|
| `InboundCallApplication`  | Incoming calls from Vapi     | `/inboundCall/{serverPath}`  | ASSISTANT_REQUEST, TOOL_CALL, STATUS_UPDATE, etc. |
| `OutboundCallApplication` | App-initiated outbound calls | `/outboundCall/{serverPath}` | OutboundRequest                                   |
| `WebApplication`          | WebRTC/web-based calls       | `/web/{serverPath}`          | WebRequest                                        |

Each application defines:

- `serverPath` - The URL suffix for its endpoint
- `serverSecret` - HMAC validation via query parameter
- Request handlers (e.g., `onAssistantRequest`, `onToolCall`)
- Response builders that use the DSL

### Request-Response Processing Pipeline

```
HTTP POST from Vapi Platform
    ↓
Route matched by serverPath
    ↓
RequestContextImpl created (parsed JSON + metadata)
    ↓
Application's request handler invoked (e.g., onAssistantRequest)
    ↓
Response built via DSL (e.g., assistant { ... })
    ↓
Implementation delegates to DTOs
    ↓
DTOs serialize to JSON
    ↓
Callbacks executed (onAllResponses, onResponse)
    ↓
JSON response returned to Vapi
```

### Tool System

Tools are the primary mechanism for assistants to take actions. The `Tools` interface provides:

- `serviceTool()` - Kotlin methods automatically exposed as tools via reflection
- `manualTool()` - Inline lambda implementation
- `externalTool()` - Remote server integration
- `transferTool()` - Call transfer destinations
- `dtmfTool()` - DTMF-based interactions
- `endCallTool()`, `voiceMailTool()` - Call control
- `ghlTool()`, `makeTool()` - Third-party integrations

**Service Tool Pattern** (most common):

```kotlin
class MyService : ToolCallService() {
  @ToolCall("Description of what this does")
  fun myFunction(param: String): ReturnType {
    // Implementation
  }

  override fun onToolCallComplete(...) = requestCompleteMessages {
    requestCompleteMessage { content = "Done!" }
  }
}

// Usage:
assistant {
  tools {
    serviceTool(MyService()) {
      requestStartMessage { content = "Starting..." }
    }
  }
}
```

Service tools are cached per application (by `fullServerPath`) and auto-purged based on configurable duration.

### Callback System

Callbacks execute at different stages of request/response processing:

**Global callbacks** (defined in `Vapi4kConfig`):

- `onAllRequests { }` - Every request, any type
- `onRequest(type1, type2) { }` - Specific ServerRequestType(s)
- `onAllResponses { }` - Every response, any type
- `onResponse(type1, type2) { }` - Specific ServerRequestType(s)

**Application-level callbacks** (defined per application):

- Same structure as global but scoped to the specific application

Callbacks receive `RequestContext` with request details and can perform logging, metrics, validation, etc.

### Assistant Definition Methods

Two approaches for defining assistants in responses:

1. **Transient** - Define inline:

```kotlin
assistant {
  firstMessage = "Hello!"
  openAIModel { modelType = OpenAIModelType.GPT_4_TURBO }
  tools { ... }
}
```

2. **Persistent** - Reference existing Vapi assistant:

```kotlin
assistantId {
  assistantId = "uuid-from-vapi"
  assistantOverrides {
    // Optional overrides
  }
}
```

The same pattern applies to Squads (multi-assistant configurations).

## Test Infrastructure

Tests use Ktor's `testApplication` for embedded server testing.

**Standard Test Pattern**:

```kotlin
@Test
fun `test name`() {
  withTestApplication(INBOUND_CALL, JsonFilenames.JSON_ASSISTANT_REQUEST) {
    assistant {
      groqModel { modelType = GroqModelType.LLAMA3_70B }
    }
  }.let { (response, jsonElement) ->
    response.status shouldBeEqualTo HttpStatusCode.OK
    jsonElement.stringValue("assistant.model.model") shouldBeEqualTo "llama3-70b-8192"
  }
}
```

**Test Infrastructure Components**:

- `withTestApplication()` helper loads JSON from classpath, installs plugin, captures response
- Test request JSONs in: `vapi4k-core/src/test/resources/json/`
- JSON helpers: `stringValue()`, `intValue()`, etc. for extracting values from response

## Important Code Conventions

### DSL Builder Pattern

When implementing new DSL components:

1. Create interface in `api/` package
2. Create `*Impl` class in `dsl/` that delegates to DTO
3. Create `*Dto` class in `dtos/` with `@Serializable`
4. Use `by dto` delegation for properties

Example:

```kotlin
// api/model/MyModel.kt
interface MyModel : MyModelProperties {
  // DSL functions
}

// dsl/model/MyModelImpl.kt
class MyModelImpl(private val dto: MyModelDto) :
  MyModelProperties by dto,
  MyModel {
  // Implementation
}

// dtos/model/MyModelDto.kt
@Serializable
data class MyModelDto(
  override var property: String? = null
) : MyModelProperties
```

### Enum Naming

Enums representing Vapi API values use specific naming:

- Types/Categories: PascalCase (e.g., `OpenAIModelType.GPT_4_TURBO`)
- For serialization, `@SerialName` annotations map to API values

### JSON Utilities

- Use `JsonUtils.toJsonElement()` and `JsonUtils.toJsonString()` for serialization
- Use extension functions like `jsonElement.stringValue("path.to.field")` for extraction

## Plugin Installation and Configuration

Applications install the plugin in their Ktor configuration:

```kotlin
fun Application.module() {
  install(Vapi4k) {
    // Optional global config
    onAllRequests { context ->
      println("Request: ${context.serverRequestType}")
    }

    // Define applications
    inboundCallApplication {
      serverPath = "my-assistant"
      serverSecret = env("SERVER_SECRET") // Use env helper

      onAssistantRequest { context ->
        assistant {
          firstMessage = "Hello!"
          model { /* ... */ }
        }
      }
    }
  }
}
```

**Key Configuration Points**:

- Use `env("VAR_NAME")` helper for environment variables
- Set `VAPI4K_BASE_URL` for production deployments
- Admin console available in dev mode at `/admin`

## Development Workflow Notes

### When Adding New Features

1. Define API interface first in `com.vapi4k.api.*`
2. Create implementation in `com.vapi4k.dsl.*` with delegation
3. Create DTO in `com.vapi4k.dtos.*` with serialization annotations
4. Add tests in corresponding test file using `withTestApplication`
5. Update KDocs for public API

### When Modifying DTOs

- DTOs are mutable by design (properties are `var`)
- Always maintain serialization compatibility
- Use `@SerialName` for API field name mapping
- Consider nullable vs non-null based on Vapi API requirements

### Working with Models

Model implementations (OpenAI, Groq, Anthropic, etc.) follow the same pattern:

- Interface in `api/model/`
- Implementation in `dsl/model/`
- DTO in `dtos/model/`
- Enum for model types with `@SerialName` annotations

### Version Management

- Current version defined in `build.gradle.kts`: `extra["versionStr"] = "1.3.2"`
- Update version before releases
- Version used in KDocs footer and build artifacts
