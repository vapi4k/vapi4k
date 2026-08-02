# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Vapi4k is a Ktor plugin and Kotlin DSL for building voice AI applications with [Vapi.ai](https://vapi.ai). It provides
type-safe builders for configuring assistants, tools, models, voices, and call workflows.

**Version:** 1.8.1 (defined in `gradle.properties` via the `version` property)
**JVM Target:** 17

Key dependency versions are managed in `gradle/libs.versions.toml`.

## Vapi API Spec

The live Vapi OpenAPI spec is at `https://api.vapi.ai/api-json`. Use it to verify enum values, check for new/removed
providers, and validate wire values. Enum values in this codebase should match the spec exactly (case-sensitive).

## Build Commands

A `Makefile` wraps the common Gradle invocations — run `make help` for the full list. Non-obvious invocations:

```bash
# Run a single test method — use * for the spaces in Kotest test names
./gradlew :vapi4k-core:test --tests "com.vapi4k.ApplicationTest.test for serverPath*"
```

Note that `detekt` fails the build on **any** finding, and `dependencyUpdates` must run with
`--no-configuration-cache --no-parallel`.

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

## Quality Tooling

- **kotlinter** (`com.pambrose.kotlinter` plugin) provides `lintKotlin` / `formatKotlin`.
- **detekt** (2.x, applied via the `dev.detekt` plugin) runs on all publishable subprojects (`vapi4k-core`,
  `vapi4k-dbms`, `vapi4k-utils`) via `Project.configureDetekt()` in the root build. `ignoreFailures = false`, so
  any finding fails the build. The rule config at `config/detekt/detekt.yml` is **overrides-only**: it sets
  `buildUponDefaultConfig = true` and carries just the project's deviations from detekt's defaults — it is *not*
  a full copy of the default config. Current deviations:
  - Rules disabled: `MaxLineLength`, `FunctionOnlyReturningConstant`, `UnusedPrivateProperty`, `EmptyFunctionBlock`,
    `MagicNumber`, `MatchingDeclarationName`, `MemberNameEqualsClassName`, `ForbiddenComment`.
  - Thresholds raised: `LongMethod` (`allowedLines: 140`), `LongParameterList` (10 params), `TooManyFunctions`
    (raised per-scope limits, excludes test sources).

  Where suppression at the call site is a better fit, use `@Suppress("RuleName")` instead of editing the global
  config. Note: detekt 2.x renamed packages from `io.gitlab.arturbosch.detekt.*` to `dev.detekt.gradle.*` and
  reworked the config schema (rule `threshold` → `allowedX`; report `xml` → `checkstyle`, `md` → `markdown`); the
  pinned `2.0.0-alpha.5` is a pre-release.
- **Kover** is applied to the same publishable subprojects, with the root project aggregating reports via
  `kover(project(...))` dependencies. Run `koverHtmlReport` for local browsing or `koverXmlReport` to produce
  `build/reports/kover/report.xml` (the file uploaded to Codecov in CI).
- **Codecov**: the GitHub Actions `Run tests` workflow runs `./gradlew test koverXmlReport` and uploads the
  aggregated report via `codecov/codecov-action@v5` using `secrets.CODECOV_TOKEN`. Coverage is visible at
  https://codecov.io/gh/vapi4k/vapi4k.
- **Dependency updates**: `./gradlew dependencyUpdates` runs the ben-manes versions plugin (replacing the former
  `com.pambrose.stable-versions`). As of plugin `0.57.0` the id is **`io.github.ben-manes.versions`** — it moved from
  `com.github.ben-manes.versions`, so the old id resolves to the stale `0.54.0` line and must not be reintroduced.
  `Project.configureVersions()` in the root build rejects pre-release candidates
  (`rc`/`beta`/`alpha`/milestone/`snapshot`/`eap`/`dev`/`pre`) **unless the current version is already on a
  pre-release line** — so a detekt alpha still surfaces newer alphas, while stable deps ignore pre-releases.
- **BuildConfig**: `vapi4k-core` exposes `BuildConfig.RELEASE_DATE` and `BuildConfig.BUILD_TIME` backed by
  `ValueSource` providers, so they refresh on every build (configuration-cache safe) instead of being frozen
  in the cache.

## Architecture

### Three-Layer Architecture

1. **API Layer** (`com.vapi4k.api.*`) - Public interfaces defining the DSL contract, annotated with `@Vapi4KDslMarker`
2. **DSL Layer** (`com.vapi4k.dsl.*`) - Concrete implementations with state management and caching
3. **DTO Layer** (`com.vapi4k.dtos.*`) - Kotlinx serialization models for JSON serialization to Vapi API format

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

- Enum entry names use `UPPER_SNAKE_CASE` with underscores between words (e.g., `NUMERIC_SCALE`, `PASS_FAIL`)
- Has an `UNSPECIFIED` entry with `desc = UNSPECIFIED_DEFAULT`
- Has a `desc: String` property holding the Vapi API wire value
- Has `isSpecified()` / `isNotSpecified()` methods
- Has a companion `KSerializer` that serializes/deserializes using the `desc` field
- Is annotated with `@Serializable(with = XxxSerializer::class)`

`UNSPECIFIED` values are filtered out during serialization.

**Exception:** Some discriminator enums (e.g., `DestinationType`, `VoiceProviderType`) omit `UNSPECIFIED` and the
custom `KSerializer` — they use the standard pattern but without the sentinel value.

### DTO Serialization Patterns

- **`assignEnumOverrides()` pattern**: DTOs use `@Transient` on enum properties and store the wire value in a plain
  `String` field. `assignEnumOverrides()` copies `enum.desc` to the string field before serialization.
- **Polymorphic serialization**: Common interfaces (`CommonTranscriberDto`, `CommonVoiceDto`, `CommonModelDto`) use
  custom `KSerializer` implementations dispatching to concrete serializers. Deserialization is intentionally
  unsupported.
- **`@EncodeDefault`**: Used on `provider` discriminator fields to ensure they're always emitted.

### External JSON Utilities

The codebase heavily uses `com.pambrose.common-utils:json-utils` (imported as `utils-json` in the version
catalog). Provides `toJsonElement()`, `toJsonString()`, dot-path access (`stringValue("a.b.c")`), and more. Imported
throughout core and tests.

## Environment Variables

Defined in `vapi4k-core/src/main/kotlin/com/vapi4k/common/CoreEnvVars.kt` (plus `DBMS_*` for vapi4k-dbms). Values are
read from `System.getenv()` first, then fall back to HOCON config (`application.conf`) — that fallback is easy to miss
when a var appears unset.

## Admin Endpoints

The admin UI (dashboard, cache inspection, tool validation, live log streaming) is registered **only when
`IS_PRODUCTION` is not `true`**; `/ping` and `/version` are always available. The UI pulls HTMX from a CDN and bundles
Bootstrap under `core_static/`, `bootstrap/`, `prism/`.

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
- Multi-provider abstraction covers 15 model providers, 18 voice providers, and 12 transcriber providers
- **Dokka gotcha**: the root project's module-name constant is named `dokkaModuleName` (not `moduleName`). Inside a
  `dokka { }` block the extension has its own `moduleName` property, so a top-level `val moduleName` would be shadowed
  and `moduleName.set(moduleName)` would wire the property to itself — a "Circular evaluation detected" failure at
  `dokkaGenerate`. Keep DSL-property names and top-level constants distinct.
- **Test logging gotcha**: `configureTesting()` lists `STANDARD_ERROR` in `testLogging.events` but does *not* set
  `showStandardStreams`. Its setter removes both `STANDARD_OUT` and `STANDARD_ERROR` from the event set, so
  `showStandardStreams = false` after configuring `events` would silently erase the `STANDARD_ERROR` entry.

## Adding New Providers

See the `adding-a-provider` skill (`.claude/skills/adding-a-provider/SKILL.md`) for the per-provider file set and the
wiring points that are easy to miss.

## Testing

Kotest 6 (`StringSpec` with `init {}`) + Kotest assertions + Ktor test host. All test classes extend `StringSpec()` and
define tests as string-named lambdas inside an `init {}` block.

- `vapi4k-core/src/test/kotlin/com/vapi4k/` - Unit tests for DSL builders, serialization, utilities
- `vapi4k-core/src/test/kotlin/simpledemo/` - Example applications and integration tests
- `vapi4k-core/src/test/resources/json-tool-tests/` - JSON fixtures (`assistantRequest.json`, `toolRequest1-4.json`,
  `endOfCallReportRequest.json`)

Key test helper: `withTestApplication(appType, fileName, block)` in
`vapi4k-core/src/test/kotlin/com/vapi4k/utils/TestUtils.kt`
starts a Ktor test app, posts a JSON fixture, and returns `(HttpResponse, JsonElement)`.

## Publishing

Published to Maven Central: group `com.vapi4k`, artifacts `vapi4k-core`, `vapi4k-dbms`, `vapi4k-utils`.
All modules include sources JAR and Dokka javadoc JAR. Uses the
[Vanniktech Maven Publish](https://github.com/vanniktech/gradle-maven-publish-plugin) plugin with automatic release.
Template repo: [vapi4k-template](https://github.com/vapi4k/vapi4k-template).
