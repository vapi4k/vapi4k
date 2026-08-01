# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [1.8.1] - 2026-08-01

### Added

- `make depends` target that runs `./gradlew dependencies` to print the project dependency tree (#59)

### Changed

- Bump dependencies: Kotlin `2.4.0 → 2.4.10`, Ktor `3.5.1 → 3.5.2`, logback `1.5.38 → 1.6.1`,
  Flyway `12.11.0 → 13.1.0`, Kotest `6.2.2 → 6.2.3`, Kover `0.9.8 → 0.9.9`, common-utils `3.1.0 → 3.2.2`,
  pambrose gradle-plugins `1.1.0 → 1.1.1`, ben-manes versions plugin `0.54.0 → 0.57.0` (#59)

### Fixed

- Point the dependency-updates plugin at its relocated id: `com.github.ben-manes.versions` →
  `io.github.ben-manes.versions`. The old id is frozen at `0.54.0`, so `dependencyUpdates` was pinned to a stale
  plugin line (#59)

## [1.8.0] - 2026-07-10

### Added

- **detekt** static analysis on all publishable subprojects (`vapi4k-core`, `vapi4k-dbms`, `vapi4k-utils`) via an
  overrides-only `config/detekt/detekt.yml` (`buildUponDefaultConfig = true`); the build fails on any finding
  (`ignoreFailures = false`). `MaxLineLength`, `FunctionOnlyReturningConstant`, `UnusedPrivateProperty`,
  `EmptyFunctionBlock`, and others are intentionally disabled (#49, #53)
- **Kover** code coverage on all publishable subprojects with root-level aggregation
  (`koverHtmlReport` / `koverXmlReport`) (#49)
- **Codecov** upload step in the `Run tests` GitHub Actions workflow via `codecov/codecov-action@v5`, plus a coverage
  badge in `README.md` (#49, #50)
- Callback dispatch tests (`CallbackDispatchTest`) covering global and per-application request/response fan-out,
  per-type filtering, and applicationId routing (#55)
- `ModelSerializerTest` covering all 15 model providers plus the registry-miss error branch (#57)
- Tests for the tool-call failure path, the admin `validate` route's not-found rendering, and the restructured
  `FunctionDetails.invokeMethod()` paths (suspend invocation, parameter-not-found, arg-log rendering) (#54, #56)
- `make help` target with auto-discovered descriptions and a usage header, plus `lint`, `detekt`, and `format`
  targets (#49, #52)

### Changed

- Upgrade to **Kotlin 2.4.0** (from 2.3.20) and the **Gradle wrapper 9.6.1** (from 9.5.0) (#51, #53)
- detekt uses the 2.x `dev.detekt` plugin id; `configureDetekt()` adopts the 2.x API (lazy `.set(...)`, reports
  `checkstyle` / `markdown`), and the config is a minimal overrides file instead of a full copy of detekt's defaults
  (#53)
- Swap the dependency-updates plugin from `com.pambrose.stable-versions` to the ben-manes
  `com.github.ben-manes.versions`; `configureVersions()` rejects pre-release candidates unless the current version is
  already on a pre-release line, and holds the whole `DependencyUpdatesTask` configuration in a single block so the
  configuration-cache opt-out sits next to the `doLast` it protects (#53, #58)
- Enable the Kotlin `-Xreturn-value-checker` on production code (#53)
- Bump dependencies: Ktor `3.4.2 → 3.5.1`, Kotlinx Serialization `1.10.0 → 1.11.0`, Kotest `6.0.0.M4 → 6.2.2`
  (milestone → stable), Exposed `1.2.0 → 1.3.1`, Flyway `11.8.0 → 12.11.0`, HikariCP `7.0.2 → 7.1.0`,
  PostgreSQL `42.7.10 → 42.7.13`, Micrometer `1.16.4 → 1.17.0`, logback `1.5.32 → 1.5.38`,
  kotlin-logging `8.0.01 → 8.0.4`, common-utils `2.7.1 → 3.1.0` (#49, #51, #53, #58)
- Reduce cyclomatic complexity of `AdminJobs.startCallbackThread`, `FunctionDetails.invokeMethod()`, and
  `ModelSerializer.serialize()` by extracting focused helpers and dropping their `@Suppress("CyclomaticComplexMethod")`
  annotations — no behavior change (#55, #56, #57)
- Replace `ModelSerializer`'s 15-branch `when` with a `KClass`-keyed serializer registry and promote
  `assignEnumOverrides()` to a default no-op on `CommonModelDto` so it applies uniformly across model DTOs (#57)
- Switch `BuildConfig.RELEASE_DATE` and `BuildConfig.BUILD_TIME` to `ValueSource`-backed providers so they refresh on
  every build instead of being frozen in the configuration cache (#49)
- Centralize repositories in `settings.gradle.kts` and versions in `gradle/libs.versions.toml`; add an explicit GPG key
  ID to the publishing config; group the version catalog by purpose (#47, #48, #51)
- Scope Dokka generation to publishable subprojects (skip `vapi4k-snippets`); enable the Gradle build cache; capitalize
  the DBMS POM name (#51)

### Fixed

- `ValidateApplication`: two `p { ... }` blocks were missing the kotlinx.html `unaryPlus`, so error text rendered as
  empty `<p>` elements — add the `+` so the message appears (#53)
- `ToolCallResponse`: use `onFailure` instead of `getOrElse` for the tool-invocation result to express side-effect
  intent (#53)
- `SerializationIssue`: make `DataChild` implement the sealed `Child` interface so the `value as DataChild` cast is
  valid (#53)
- Exclude test-prefixed configurations from the `dependencyUpdates` task so silently-dropped resolution failures stop
  hiding test deps from the report (#49)
- Quiet testcontainers/docker-java wire logging by adding a `vapi4k-dbms` test `logback.xml` and quieting
  `com.github.dockerjava` in the `vapi4k-core` test logback (#53)
- `dokkaGenerate` failed with "Circular evaluation detected: extension 'dokka' property 'moduleName'" because the
  top-level `moduleName` constant was shadowed by the Dokka extension's own `moduleName` property, so
  `moduleName.set(moduleName)` wired the property to itself; rename the constant to `dokkaModuleName` (#58)
- Test stderr was silently suppressed: `showStandardStreams = false` removes `STANDARD_ERROR` from the test-logging
  event set, negating the `STANDARD_ERROR` added on the line above it; drop it so test stderr surfaces (#58)

### Removed

- Unused `extra["versionStr"]` and `extra["releaseDate"]` propagation across modules, and stale `gradle.properties`
  lines (#49)

## [1.7.0] - 2026-04-08

### Changed

- Migrate artifact publishing from JitPack to **Maven Central**; group ID changes from
  `com.github.vapi4k.vapi4k` to `com.vapi4k` (**breaking** for downstream consumers) (#44)
- Adopt the [Vanniktech Maven Publish](https://github.com/vanniktech/gradle-maven-publish-plugin) plugin
  for streamlined Central publishing with automatic release; add GPG signing (#44)
- Add POM metadata (license, developer info, SCM URLs) required by Maven Central (#44, #45)
- Update `common-utils` coordinates from `com.github.pambrose` to `com.pambrose` (#44)
- Centralize publishing configuration in the root build; remove per-module publishing blocks (#44)
- Switch documentation install snippet to use `.env` copied from `.env.example` (#46)

### Added

- Support version override via `-PoverrideVersion=...` for snapshot builds (#44)
- `publish-local-snapshot`, `publish-snapshot`, and `publish-maven-central` Makefile targets (#44)

### Removed

- JitPack repository, plugin resolution strategy, and JitPack-specific Makefile targets (#44)
- Unused `pambrose-repos`, `pambrose-snapshot`, `pambrose-testing` Gradle plugins (#44)
- Unused `ktor-client-mock` test dependency (#44)

## [1.6.2] - 2026-04-01

### Changed

- Bump dependencies to latest versions (#43)

## [1.6.1] - 2026-03-19

### Added

- Testcontainers integration tests for vapi4k-dbms (#35)
- 6 new model providers synced with Vapi API spec (#40)
- New transfer mode values from Vapi API spec (#32)
- Punjabi language support for Cartesia and solaria-1 model for Gladia (#31)
- High-priority enum values from Vapi API spec (#30)
- New enum values for message types and server requests (#28)

### Changed

- Rename `PunctuationType` to `PunctuationBoundaryType` to mirror Vapi API (#39)
- Replace `StepDestination` with `DynamicDestination` and `SquadDestination` from Vapi API spec (#37)
- Update provider counts in README and CLAUDE.md (#33)
- Add Vapi API spec reference and provider patterns to CLAUDE.md (#41)

### Fixed

- `SuccessEvaluationRubricType` case mismatch with Vapi API spec (#42)
- Lint line-length violation in `AssistantTransferMode` enum (#36)

### Removed

- Neets voice provider no longer in Vapi API spec (#38)
- `TOOL_CALLS_RESULTS` enum value not in Vapi API spec (#29)

## [1.6.0] - 2026-03-17

### Added

- Test coverage for utilities, enums, tools, and routing (#26)
- Test coverage for foundation utilities, value classes, and providers (#23)
- `/vapi-enum-diff` command to compare enums against Vapi OpenAPI spec (#18)

### Changed

- Migrate test framework from JUnit 5 + Kluent to Kotest 6 StringSpec (#25)
- Clean up test assertion style and reduce reflection boilerplate (#24)
- Restructure voice DTOs to nest chunkPlan/formatPlan matching Vapi API wire format (#21)
- Sync language enums with Vapi OpenAPI spec (#20)
- Sync model enums with Vapi OpenAPI spec (#19)
- Bump dependency versions: config, micrometer, utils, and Gradle wrapper (#22)

### Removed

- OpenSpec framework (#17)

## [1.5.0] - 2026-03-03

### Changed

- Extract version variable in Makefile to avoid hardcoded duplication
- Add build trigger and view commands to Makefile for JitPack integration
- Disable configuration cache for `dependencyUpdates` command in Makefile

## [1.4.0] - 2026-02-18

### Changed

- Update group ID in build configuration and documentation for consistency
- Update gradle-plugins version to 1.0.3 and adjust import path for `BuildConfig`

## [1.3.7] - 2026-02-09

### Changed

- Update Dokka configuration and add packages.md for documentation
- Refactor build configuration to use Kotlin serialization plugin and update dependency versions
- Bump dependencies

### Fixed

- Documentation references for `VoicemailTool` in `AssistantFunctions.kt` and `VoicemailDetection.kt`

## [1.3.6] - 2026-01-29

### Changed

- Version bump and documentation updates

## [1.3.5] - 2026-01-29

### Changed

- Update verbose logging variable names for clarity (#14)

## [1.3.4] - 2026-01-29

### Added

- Build-tests target in Makefile for compiling test Kotlin sources

### Changed

- Enhance JSON handling with verbose logging (#13)
- Upgrade Gradle to version 9.3.0 and update dependencies
- Refactor `ChildSerializer` to remove unnecessary private modifier and simplify response handling in TestUtils
- Update enums (#11)

## [1.3.3] - 2026-01-21

### Changed

- Version bump release (#12)

## [1.3.2] - 2025-08-18

### Changed

- Update JSON utility method usages (#10)

## [1.3.1] - 2025-08-18

### Changed

- Version bump release (#9)

## [1.3.0] - 2025-06-26

### Changed

- Version bump release (#8)

## [1.2.4] - 2025-02-16

### Fixed

- KDocs generation (#7)

## [1.2.3] - 2025-02-08

### Changed

- Version bump release (#6)

## [1.2.2] - 2024-12-19

### Changed

- Version bump release (#5)

## [1.2.1] - 2024-12-11

### Changed

- Version bump release (#4)

## [1.2.0] - 2024-11-27

### Changed

- Update to Kotlin 2.1.0 (#3)

## [1.1.1] - 2024-11-01

### Changed

- Update Kotlin to 2.0.21 (#2)

## [1.1.0] - 2024-10-30

### Changed

- Update to Ktor 3.0.0 (#1)
- Refactor `AssistantIdSource`

## [1.0.1] - 2024-10-05

### Added

- CodeQL, SonarQube, and Detekt CI configurations

### Changed

- Consolidate KDocs into a single build directory
- Remove enums packages and consolidate classes
- Clean up ktlint issues

## [1.0.0] - 2024-10-03

### Added

- Initial release of Vapi4k
- Ktor plugin and Kotlin DSL for building voice AI applications with Vapi.ai
- Type-safe builders for assistants, tools, models, voices, and call workflows
- Support for inbound calls, outbound calls, and web-based voice interactions
- Service tool, manual tool, and external tool strategies
- Admin dashboard with HTMX and Bootstrap
- Prometheus metrics integration
- Maven Central publishing

[1.8.1]: https://github.com/vapi4k/vapi4k/compare/1.8.0...1.8.1
[1.8.0]: https://github.com/vapi4k/vapi4k/compare/1.7.0...1.8.0
[1.7.0]: https://github.com/vapi4k/vapi4k/compare/1.6.2...1.7.0
[1.6.2]: https://github.com/vapi4k/vapi4k/compare/1.6.1...1.6.2
[1.6.1]: https://github.com/vapi4k/vapi4k/compare/1.6.0...1.6.1
[1.6.0]: https://github.com/vapi4k/vapi4k/compare/1.5.0...1.6.0
[1.5.0]: https://github.com/vapi4k/vapi4k/compare/1.4.0...1.5.0
[1.4.0]: https://github.com/vapi4k/vapi4k/compare/1.3.7...1.4.0
[1.3.7]: https://github.com/vapi4k/vapi4k/compare/1.3.6...1.3.7
[1.3.6]: https://github.com/vapi4k/vapi4k/compare/1.3.5...1.3.6
[1.3.5]: https://github.com/vapi4k/vapi4k/compare/1.3.4...1.3.5
[1.3.4]: https://github.com/vapi4k/vapi4k/compare/1.3.2...1.3.4
[1.3.3]: https://github.com/vapi4k/vapi4k/compare/1.3.2...1.3.3
[1.3.2]: https://github.com/vapi4k/vapi4k/compare/1.3.1...1.3.2
[1.3.1]: https://github.com/vapi4k/vapi4k/compare/1.3.0...1.3.1
[1.3.0]: https://github.com/vapi4k/vapi4k/compare/1.2.4...1.3.0
[1.2.4]: https://github.com/vapi4k/vapi4k/compare/1.2.3...1.2.4
[1.2.3]: https://github.com/vapi4k/vapi4k/compare/1.2.2...1.2.3
[1.2.2]: https://github.com/vapi4k/vapi4k/compare/1.2.1...1.2.2
[1.2.1]: https://github.com/vapi4k/vapi4k/compare/1.2.0...1.2.1
[1.2.0]: https://github.com/vapi4k/vapi4k/compare/1.1.1...1.2.0
[1.1.1]: https://github.com/vapi4k/vapi4k/compare/1.1.0...1.1.1
[1.1.0]: https://github.com/vapi4k/vapi4k/compare/1.0.1...1.1.0
[1.0.1]: https://github.com/vapi4k/vapi4k/compare/1.0.0...1.0.1
[1.0.0]: https://github.com/vapi4k/vapi4k/releases/tag/1.0.0
