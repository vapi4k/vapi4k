# Project Context

## Purpose

Vapi4k is a Ktor plugin and Kotlin DSL for defining, deploying, and maintaining Vapi (vapi.ai) voice AI applications. It
provides type-safe builders for creating voice assistants, handling tool calls, and managing real-time voice
interactions through a clean Kotlin API.

**Key Goals:**

- Provide a type-safe, idiomatic Kotlin DSL for Vapi platform integration
- Support multiple application types: inbound calls, outbound calls, and web-based interactions
- Enable seamless tool integration with automatic reflection-based service tools
- Offer comprehensive callback and lifecycle management for voice interactions
- Maintain clean separation between API, implementation, and serialization layers

## Tech Stack

### Core Technologies

- **Kotlin** (JVM language, latest stable version)
- **Ktor** (server & client frameworks for HTTP handling and routing)
- **kotlinx.serialization** (JSON serialization/deserialization)
- **Gradle** (build system with Kotlin DSL)

### Libraries & Frameworks

- **Exposed ORM** (optional database integration in vapi4k-dbms module)
- **HikariCP** (connection pooling for database module)
- **PostgreSQL Driver** (database backend support)
- **Micrometer** (metrics and observability)
- **ktlint** (code formatting and linting)
- **Dokka** (KDoc documentation generation)

### Development Tools

- ktlint for code quality
- Gradle continuous build (`./gradlew -t build`)
- JUnit for testing
- Ktor test framework for embedded server testing

## Project Conventions

### Code Style

- **Formatting**: Use ktlint for all Kotlin code formatting
  - Run: `./gradlew formatKotlin`
  - Check: `./gradlew lintKotlin`
- **Naming Conventions**:
  - **Enums**: PascalCase for types/categories (e.g., `OpenAIModelType.GPT_4_TURBO`)
  - **Classes**: PascalCase with descriptive suffixes (`*Impl`, `*Dto`, `*Service`)
  - **Interfaces**: Plain PascalCase without "I" prefix
  - **Properties**: camelCase
- **API Design**: Use `@SerialName` annotations to map Kotlin names to API field names
- **Documentation**: KDocs required for all public API surfaces
- **Mutability**: DTOs use `var` properties by design for builder pattern compatibility

### Architecture Patterns

#### Three-Layer DSL Pattern (CRITICAL)

All DSL components follow this pattern:

1. **API Layer** (`com.vapi4k.api.*`)
  - Public interfaces defining the DSL shape
  - What developers interact with directly
  - Example: `Assistant`, `Tools`, `OpenAIModel`

2. **Implementation Layer** (`com.vapi4k.dsl.*`)
  - Builder classes using Kotlin delegation (`by dto`)
  - Collect state and delegate properties to DTOs
  - Example: `AssistantImpl`, `ToolsImpl`, `OpenAIModelImpl`

3. **DTO Layer** (`com.vapi4k.dtos.*`)
  - `@Serializable` classes for JSON output
  - Mutable properties (`var`)
  - Example: `AssistantDto`, `ToolDto`, `OpenAIModelDto`

**Data Flow**: Developer API → Implementation builds state → DTOs serialize to JSON

#### Module Architecture

Four Gradle modules with clear separation:

- **vapi4k-core**: Main plugin, DSL, and Vapi API integration
- **vapi4k-utils**: Foundation utilities (JSON, logging) - no internal dependencies
- **vapi4k-dbms**: Optional PostgreSQL/JDBC integration via Exposed ORM
- **vapi4k-snippets**: Code examples and demonstrations

#### Application Types

Three distinct application patterns:

| Type                      | Endpoint Pattern             | Purpose                      |
|---------------------------|------------------------------|------------------------------|
| `InboundCallApplication`  | `/inboundCall/{serverPath}`  | Incoming calls from Vapi     |
| `OutboundCallApplication` | `/outboundCall/{serverPath}` | App-initiated outbound calls |
| `WebApplication`          | `/web/{serverPath}`          | WebRTC/web-based calls       |

#### Request-Response Pipeline

```
HTTP POST from Vapi → Route matched → RequestContextImpl created →
Application handler invoked → Response built via DSL →
Implementation delegates to DTOs → DTOs serialize to JSON →
Callbacks executed → JSON response to Vapi
```

#### Tool System

Tools expose functionality to voice assistants:

- **Service Tools**: Kotlin methods auto-exposed via reflection (most common)
- **Manual Tools**: Inline lambda implementations
- **External Tools**: Remote server integration
- **Transfer/DTMF/Control Tools**: Call management functions

Service tools are cached per application and auto-purged based on configured duration.

### Testing Strategy

#### Test Infrastructure

- Use Ktor's `testApplication` for embedded server testing
- JSON test files stored in: `vapi4k-core/src/test/resources/json/`
- Helper function: `withTestApplication()` loads JSON, installs plugin, captures response

#### Standard Test Pattern

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

#### Testing Commands

```bash
# Run all tests
./gradlew test

# Run specific module tests
./gradlew :vapi4k-core:test

# Run single test class
./gradlew :vapi4k-core:test --tests "com.vapi4k.ServerTest"
```

#### Test Assertions

Use JSON helper extensions for response validation:

- `stringValue("path.to.field")`
- `intValue("path.to.field")`
- Response status validation with `shouldBeEqualTo`

### Git Workflow

- **Main Branch**: `master` (use for PRs)
- **Versioning**: Defined in `build.gradle.kts` as `extra["versionStr"]`
- **Current Version**: 1.3.2
- **Commit Conventions**: Standard semantic commit messages recommended
- **Release Process**: Update version in build.gradle.kts before releases

## Domain Context

### Vapi Voice AI Platform

Vapi4k integrates with the Vapi platform (vapi.ai), which provides:

- Voice AI assistant infrastructure
- Real-time voice conversation handling
- Tool calling and action execution
- Multi-model support (OpenAI, Anthropic, Groq, etc.)

### Key Domain Concepts

**Assistants**: Voice AI entities that can:

- Be defined transiently (inline DSL) or persistently (reference existing Vapi assistant by ID)
- Use various LLM models (OpenAI, Anthropic, Groq, DeepGram, etc.)
- Execute tools to perform actions
- Follow conversation flows and handle interruptions

**Tools**: Functions that assistants can invoke:

- Annotated Kotlin methods (`@ToolCall`) automatically exposed
- Type-safe parameter mapping from conversation to code
- Completion callbacks with structured response messages

**Squads**: Multi-assistant configurations for complex interactions

**Request Types**: Various server request types handled:

- `ASSISTANT_REQUEST`: Initial assistant configuration
- `TOOL_CALL`: Tool invocation from assistant
- `STATUS_UPDATE`: Call status changes
- `END_OF_CALL_REPORT`: Final call metrics and transcript
- `HANG`, `SPEECH_UPDATE`, `TRANSCRIPT`, `CONVERSATION_UPDATE`, etc.

**Callbacks**: Lifecycle hooks at global or application level:

- `onAllRequests` / `onRequest(type)`: Pre-processing
- `onAllResponses` / `onResponse(type)`: Post-processing
- Access to `RequestContext` with full request metadata

## Important Constraints

### Design Constraints

- **Always prefer editing existing files**: Never create new files unless absolutely necessary
- **Maintain three-layer pattern**: API → Implementation → DTO must be preserved
- **Backward compatibility**: DTOs must maintain serialization compatibility with Vapi API
- **Type safety**: Leverage Kotlin's type system; avoid stringly-typed APIs

### Technical Constraints

- **Ktor dependency**: Core framework must remain Ktor-based
- **kotlinx.serialization**: Required for JSON handling (not Gson or Jackson)
- **Gradle Kotlin DSL**: Build configuration in Kotlin, not Groovy
- **JVM target**: Kotlin/JVM, not Kotlin/JS or Kotlin/Native

### API Constraints

- **Vapi Platform Compatibility**: All DTOs must match Vapi's expected JSON structure
- **HMAC Validation**: Server secret validation via query parameters
- **Environment Configuration**: Support for `VAPI4K_BASE_URL` and other env vars

### Development Constraints

- **No time estimates**: Never provide timing predictions in responses
- **Minimal scope**: Only implement what's requested; avoid over-engineering
- **No unnecessary improvements**: Don't add features, refactoring, or "enhancements" beyond the task
- **Trust internal code**: Only validate at system boundaries (user input, external APIs)

## External Dependencies

### Primary External Service

- **Vapi Platform (vapi.ai)**:
  - Voice AI infrastructure
  - HTTP callbacks for assistant configuration and tool execution
  - Real-time conversation management
  - Requires account and API credentials

### Development Dependencies

- **Maven Central**: For Kotlin and Ktor dependencies
- **Gradle Plugin Portal**: For build plugins (Dokka, ktlint, etc.)

### Optional External Systems

- **PostgreSQL**: For vapi4k-dbms module (optional database persistence)
- **LLM Providers**: OpenAI, Anthropic, Groq, etc. (configured through Vapi)
- **Third-party Integrations**: GHL (GoHighLevel), Make.com (via dedicated tool types)

### Environment Variables

- `VAPI4K_BASE_URL`: Base URL for production deployments
- `SERVER_SECRET`: HMAC validation secret for each application
- Custom env vars accessed via `env("VAR_NAME")` helper

### Development URLs

- Admin console: `/admin` (available in dev mode)
- Application endpoints: `/{applicationType}/{serverPath}`
- Health/metrics endpoints via Micrometer integration
