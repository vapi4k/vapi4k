# Release Notes — v1.7.1

## Highlights

A build-tooling refresh: detekt and Kover are now wired into all publishable subprojects, code coverage is
uploaded to Codecov from CI, and `BUILD_TIME` / `RELEASE_DATE` are no longer frozen by the configuration cache.

There are no API or wire-format changes in this release. Downstream consumers can bump from `1.7.0` to `1.7.1`
with no source changes.

## Changes

### Quality tooling

- **detekt** is applied to all publishable subprojects (`vapi4k-core`, `vapi4k-dbms`, `vapi4k-utils`) via a
  shared rule config at `config/detekt/detekt.yml`. The build fails on any finding (`ignoreFailures = false`).
  A small set of rules is intentionally disabled (`MaxLineLength`, `FunctionOnlyReturningConstant`,
  `UnusedPrivateProperty`, `EmptyFunctionBlock`) — at the call site, prefer `@Suppress("RuleName")` over
  disabling globally.
- **Kover** is applied to the same subprojects with root-level aggregation, so a single
  `./gradlew koverHtmlReport` or `koverXmlReport` produces a unified report.
- **Codecov**: the `Run tests` GitHub Actions workflow runs `./gradlew test koverXmlReport` and uploads the
  aggregated XML via `codecov/codecov-action@v5`. The repo's `CODECOV_TOKEN` secret keeps uploads stable.
- **README** now displays a Codecov coverage badge alongside the existing quality badges.

### Build

- `BuildConfig.RELEASE_DATE` and `BuildConfig.BUILD_TIME` are now backed by `ValueSource` providers, so they
  reflect the actual build time on every invocation rather than the value frozen by the configuration cache.
- Bump kotest from the `6.0.0.M4` milestone to stable `6.1.11`.
- Centralize repository declarations in `settings.gradle.kts` and pin the publishing GPG key ID.
- Centralize Gradle plugin version refs in `gradle/libs.versions.toml`.
- Exclude test-prefixed configurations from the ben-manes `dependencyUpdates` task so silently-dropped
  resolution failures stop hiding test deps from the report.

### Makefile

- New `help` target with auto-discovered descriptions for every target.
- New `lint`, `detekt`, and `format` targets.

### Cleanup

- Drop the unused `extra["versionStr"]` and `extra["releaseDate"]` propagation across modules.
- Remove stale `gradle.properties` lines.

## Full Changelog

https://github.com/vapi4k/vapi4k/compare/1.7.0...1.7.1
