# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [1.7.1] - 2026-05-10

### Added

- detekt static analysis applied to all publishable subprojects with shared config at
  `config/detekt/detekt.yml`; `MaxLineLength`, `FunctionOnlyReturningConstant`, `UnusedPrivateProperty`, and
  `EmptyFunctionBlock` are intentionally disabled (#49)
- Kover code coverage applied to all publishable subprojects with root-level aggregation (#49)
- Codecov upload step in the `Run tests` GitHub Actions workflow via `codecov/codecov-action@v5` (#49)
- Codecov coverage badge in `README.md` (#50)
- `Makefile` `help` target with auto-discovered descriptions, plus `lint`, `detekt`, and `format` targets (#49)

### Changed

- Bump kotest from `6.0.0.M4` (milestone) to stable `6.1.11` (#49)
- Switch `BuildConfig.RELEASE_DATE` and `BuildConfig.BUILD_TIME` to `ValueSource`-backed providers so they
  refresh on every build instead of being frozen in the configuration cache (#49)
- Centralize repositories in `settings.gradle.kts`; add explicit GPG key ID to publishing config (#48)
- Consolidate build configuration and centralize versions in `gradle/libs.versions.toml` (#47)

### Removed

- Unused `extra["versionStr"]` and `extra["releaseDate"]` propagation across modules (#49)
- Stale `gradle.properties` lines (#49)

### Fixed

- Exclude test-prefixed configurations from the ben-manes `dependencyUpdates` task so silently-dropped
  resolution failures stop hiding test deps from the report (#49)

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
