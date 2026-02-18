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

**Version:** 1.3.7 (defined in root `build.gradle.kts` via `extra["versionStr"]`)
**JVM Target:** 17

Key dependency versions are managed in `gradle/libs.versions.toml`.

## Module Structure

This is a multi-module Gradle project:

- **vapi4k-utils** - Foundation module with shared utilities (EnvVar, ServerRequestType, common utils). Zero external
  dependencies besides Kotlin stdlib, logging, and JSON.
- **vapi4k-core** - Main module containing the Ktor plugin, DSL implementations, and Vapi integration
- **vapi4k-dbms** - Optional database persistence module for message history (HikariCP + PostgreSQL + Exposed ORM)
- **vapi4k-snippets** - Example code demonstrating usage patterns (not published as a library)

**Dependencies:** `vapi4k-core` → `vapi4k-utils`, `vapi4k-dbms` → `vapi4k-utils`, `vapi4k-snippets` → `vapi4k-core`

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

# Check for dependency updates
./gradlew dependencyUpdates

# Publish to Maven Local
./gradlew publishToMavenLocal
```

## Code Style

Configured in `.editorconfig`:

- **Kotlin files use 2-space indentation** (build.gradle.kts uses 4-space)
- Max line length: 120 characters
- Wildcard imports are allowed (ktlint rule disabled)
- Several ktlint rules are disabled: `multiline-if-else`, `indent`, `multiline-expression-wrapping`,
  `chain-method-continuation`, `function-expression-body`, `string-template-indent`

Global compiler opt-ins (configured in root `build.gradle.kts`, no per-file annotations needed):

- `kotlinx.serialization.ExperimentalSerializationApi`
- `kotlin.concurrent.atomics.ExperimentalAtomicApi`
- `kotlin.contracts.ExperimentalContracts`

## Architecture

### Three-Layer Architecture

1. **API Layer** (`com.vapi4k.api.*`) - Public interfaces defining the DSL contract, annotated with `@Vapi4KDslMarker`
2. **DSL Layer** (`com.vapi4k.dsl.*`) - Concrete implementations with state management and caching
3. **DTO Layer** (`com.vapi4k.dtos.*`) - Kotlinx serialization models for JSON serialization to Vapi API format

### Ktor Plugin

`Vapi4kServer` (`vapi4k-core/src/main/kotlin/com/vapi4k/plugin/Vapi4kServer.kt`) is the core `ApplicationPlugin`. It
auto-configures Ktor with WebSocket support, routing, lifecycle events, admin endpoints, and Prometheus metrics.
`DefaultKtorConfig` (`vapi4k-core/src/main/kotlin/com/vapi4k/server/DefaultKtorConfig.kt`) handles the
auto-configuration.

### Application Types

Three types, all inheriting from `AbstractApplicationImpl`:

1. **InboundCallApplication** - Incoming phone calls (POST endpoint)
2. **OutboundCallApplication** - Outgoing calls (GET and POST endpoints)
3. **WebApplication** - Browser-based voice interactions (GET and POST endpoints)

Defined in `vapi4k-core/src/main/kotlin/com/vapi4k/api/vapi4k/Vapi4kConfig.kt`.

InboundCallApplication also supports **Squads** (multi-assistant configurations) via `squad{}`, `squadId{}`,
`assistantId{}`, plus `numberDestination{}` and `sipDestination{}` for immediate call transfer.

### Request/Response Flow

1. **RequestContext** - Session ID, timestamp, request type, phone number metadata
2. **ServerRequestType** - 14 message types (`ASSISTANT_REQUEST`, `TOOL_CALL`, `FUNCTION_CALL`, `END_OF_CALL_REPORT`,
   `CONVERSATION_UPDATE`, `STATUS_UPDATE`, `TRANSCRIPT`, `SPEECH_UPDATE`, `TRANSFER_DESTINATION_REQUEST`, `HANG`,
   `USER_INTERRUPTED`, `ASSISTANT_STARTED`, `PHONE_CALL_CONTROL`, `UNKNOWN_REQUEST_TYPE`)
3. **ResponseContext** - Wraps response with timing and request reference

### Callback Pipeline

`AbstractApplicationImpl` (`vapi4k-core/src/main/kotlin/com/vapi4k/dsl/vapi4k/AbstractApplicationImpl.kt`) provides:

- `onAllRequests{}` / `onRequest(type){}` - Request callbacks
- `onAllResponses{}` / `onResponse(type){}` - Response callbacks
- `onTransferDestinationRequest{}` - Transfer handling

Callbacks are processed asynchronously through a `Channel(Channel.UNLIMITED)` in a dedicated daemon thread. They exist
at two scopes: global (`Vapi4kConfig.onAllRequests{}`) and per-application.

### Tool System

**Tools vs Functions**: Two separate callable-object systems sharing the same `ServiceCache` and `@ToolCall` annotation
reflection. Tools (`tools { serviceTool(obj) {} }`) support message configuration (start/complete/failed/delayed).
Functions (`functions { function(obj) }`) are simpler with no message configuration.

Three tool strategies:

1. **ServiceTool** - Annotation-based using `@ToolCall` and `@Param`, auto-generates JSON schemas
2. **ManualTool** - Imperative tools with `onInvoke{}` blocks
3. **ExternalTool** - Delegates to external HTTP endpoints

`@ToolCall` constraints (enforced in `FunctionUtils.kt`):

- Each class may have only **one** `@ToolCall`-annotated method
- Allowed parameter types: `String`, `Int`, `Double`, `Boolean`, and optionally `RequestContext` (auto-injected, not in
  schema)
- Allowed return types: `String`, `Int`, `Double`, `Boolean`, `Unit`
- Both sync and suspend functions supported
- Function names are namespaced with `_assistantId` to prevent collisions in Squad mode

Optional base class `ToolCallService` (`vapi4k-core/src/main/kotlin/com/vapi4k/api/toolservice/ToolCallService.kt`)
adds dynamic message generation after tool invocation.

### Cache Lifecycle

`ServiceCache` is a `ConcurrentHashMap<CacheKey, FunctionInfo>` keyed by `"$sessionId_$assistantId"`.

- Cleared on `END_OF_CALL_REPORT` (configurable via `eocrCacheRemovalEnabled` for testing)
- Background sweep daemon runs every `TOOL_CACHE_CLEAN_PAUSE_MINS` (default 30) minutes, purging entries older than
  `TOOL_CACHE_MAX_AGE_MINS` (default 60) minutes

### Enum Serialization Pattern

Every enum used in DTOs follows a strict convention:

- Has an `UNSPECIFIED` entry with `desc = UNSPECIFIED_DEFAULT`
- Has a `desc: String` property holding the Vapi API wire value
- Has `isSpecified()` / `isNotSpecified()` methods
- Has a companion `KSerializer` that serializes/deserializes using the `desc` field
- Is annotated with `@Serializable(with = XxxSerializer::class)`

`UNSPECIFIED` values are filtered out during serialization.

### DTO Serialization Patterns

- **`assignEnumOverrides()` pattern**: DTOs use `@Transient` on enum properties and store the wire value in a plain
  `String` field. `assignEnumOverrides()` copies `enum.desc` to the string field before serialization.
- **Polymorphic serialization**: Common interfaces (`CommonTranscriberDto`, `CommonVoiceDto`, `CommonModelDto`) use
  custom `KSerializer` implementations dispatching to concrete serializers. Deserialization is intentionally
  unsupported.
- **`@EncodeDefault`**: Used on `provider` discriminator fields to ensure they're always emitted.

### External JSON Utilities

The codebase heavily uses `com.github.pambrose.common-utils:json-utils` (imported as `utils-json` in the version
catalog). Provides `toJsonElement()`, `toJsonString()`, dot-path access (`stringValue("a.b.c")`), and more. Imported
throughout core and tests.

## Environment Variables

Defined in `vapi4k-core/src/main/kotlin/com/vapi4k/common/CoreEnvVars.kt`. Values are read from `System.getenv()` first,
then fall back to HOCON config (`application.conf`).

| Variable                      | Default                 | Description                               |
|-------------------------------|-------------------------|-------------------------------------------|
| `IS_PRODUCTION`               | `false`                 | Disables admin endpoints when `true`      |
| `VAPI4K_BASE_URL`             | `http://localhost:8080` | Server base URL                           |
| `DEFAULT_SERVER_PATH`         | `/vapi4k`               | Default server path                       |
| `VAPI_BASE_URL`               | `https://api.vapi.ai`   | Vapi API base URL                         |
| `VAPI_PRIVATE_KEY`            | `""`                    | Vapi API private key (masked in logs)     |
| `VAPI_PUBLIC_KEY`             | `""`                    | Vapi API public key                       |
| `VAPI_PHONE_NUMBER_ID`        | `""`                    | Phone number ID                           |
| `DEEPGRAM_PRIVATE_KEY`        | `""`                    | Deepgram API key (masked in logs)         |
| `ADMIN_PASSWORD`              | `"admin"`               | Admin dashboard password (masked in logs) |
| `PORT`                        | `8080`                  | Server port                               |
| `TOOL_CACHE_CLEAN_PAUSE_MINS` | `30`                    | Interval between cache sweep runs         |
| `TOOL_CACHE_MAX_AGE_MINS`     | `60`                    | Max age before cache entries are purged   |

Database variables (for vapi4k-dbms): `DBMS_DRIVER_CLASSNAME`, `DBMS_URL`, `DBMS_USERNAME`, `DBMS_PASSWORD`,
`DBMS_MAX_POOL_SIZE`, `DBMS_MAX_LIFETIME_MINS`.

## Admin Endpoints (Non-Production)

When `IS_PRODUCTION` is not `true`:

- `/admin` - Dashboard (HTTP Basic Auth, user `admin`, password from `ADMIN_PASSWORD`)
- `/caches`, `/clear-caches` - Cache inspection/clearing
- `/invokeTool`, `/validate` - Tool invocation/validation UI
- `/env`, `/admin-env-vars` - Environment variable viewer
- `/admin-log` - WebSocket live log streaming

Always available: `/ping` (returns `"pong"`), `/version` (JSON version info).

The admin UI uses HTMX (CDN) and Bootstrap (bundled static resources under `core_static/`, `bootstrap/`, `prism/`).

## Code Organization Conventions

- **API interfaces** in `com.vapi4k.api.*`, **DSL implementations** in `com.vapi4k.dsl.*`, **DTOs** in
  `com.vapi4k.dtos.*`
- Use `@Vapi4KDslMarker` on DSL builder interfaces
- Prefix internal implementation properties with underscore (e.g., `_field`)
- Use inline value classes for type safety (defined in `vapi4k-core/src/main/kotlin/com/vapi4k/common/ValueClasses.kt`)
- Constants in `vapi4k-core/src/main/kotlin/com/vapi4k/common/Constants.kt`
- Only `com.vapi4k.api.*` packages are included in generated KDocs

## Build Configuration Notes

- `vapi4k-core` uses the `com.github.gmazzo.buildconfig` plugin to generate `BuildConfig` with `APP_NAME`, `VERSION`,
  `RELEASE_DATE`, and `BUILD_TIME` constants
- Multi-provider abstraction covers 10 model providers, 9 voice providers, and 3 transcriber providers

## Testing

JUnit 5 + Kluent assertions + Ktor test host.

- `vapi4k-core/src/test/kotlin/com/vapi4k/` - Unit tests for DSL builders, serialization, utilities
- `vapi4k-core/src/test/kotlin/simpledemo/` - Example applications and integration tests
- `vapi4k-core/src/test/resources/json-tool-tests/` - JSON fixtures (`assistantRequest.json`, `toolRequest1-4.json`,
  `endOfCallReportRequest.json`)

Key test helper: `withTestApplication(appType, fileName, block)` in
`vapi4k-core/src/test/kotlin/com/vapi4k/utils/TestUtils.kt`
starts a Ktor test app, posts a JSON fixture, and returns `(HttpResponse, JsonElement)`.

## Publishing

Published to JitPack: group `com.github.vapi4k`, artifacts `vapi4k-core`, `vapi4k-dbms`, `vapi4k-utils`.
All modules include sources JAR. Template repo: [vapi4k-template](https://github.com/vapi4k/vapi4k-template).
