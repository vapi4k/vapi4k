# Release Notes — v1.8.0

## Highlights

A build-quality and toolchain modernization release. This version introduces **detekt**, **Kover**, and **Codecov** to
the build, moves to **Kotlin 2.4.0**, the **Gradle 9.6.1** wrapper, and **detekt 2.x**, and carries a broad batch of
dependency bumps (Ktor, Exposed, Kotest, Flyway, HikariCP, and more). Several hot-path methods were refactored to shed
their complexity suppressions, each backed by new tests.

There are no API or wire-format changes in this release. Downstream consumers can bump from `1.7.0` to `1.8.0` with no
source changes.

## Changes

### Quality tooling

- **detekt** is applied to all publishable subprojects (`vapi4k-core`, `vapi4k-dbms`, `vapi4k-utils`) via a minimal
  overrides-only `config/detekt/detekt.yml` (`buildUponDefaultConfig = true`). The build fails on any finding
  (`ignoreFailures = false`); prefer `@Suppress("RuleName")` at the call site over disabling rules globally.
- **Kover** is applied to the same subprojects with root-level aggregation, so a single `./gradlew koverHtmlReport` or
  `koverXmlReport` produces a unified report.
- **Codecov**: the `Run tests` GitHub Actions workflow runs `./gradlew test koverXmlReport` and uploads the aggregated
  XML via `codecov/codecov-action@v5`. `README.md` gains a coverage badge.

### Toolchain

- Upgrade to **Kotlin 2.4.0** (from 2.3.20) and the **Gradle wrapper 9.6.1** (from 9.5.0).
- Adopt **detekt 2.x** (`2.0.0-alpha.5`): the plugin id is `dev.detekt`, `configureDetekt()` uses the 2.x API (lazy
  `.set(...)`, reports `checkstyle` / `markdown`), and the config is a minimal overrides file rather than a full copy of
  the defaults.
- Enable the Kotlin `-Xreturn-value-checker` on production code to catch discarded return values.

### Dependencies

- Ktor `3.4.2 → 3.5.1`, Kotlinx Serialization `1.10.0 → 1.11.0`, Kotest `6.0.0.M4 → 6.2.2` (milestone → stable),
  Exposed `1.2.0 → 1.3.1`, Flyway `11.8.0 → 12.11.0`, HikariCP `7.0.2 → 7.1.0`, PostgreSQL `42.7.10 → 42.7.13`,
  Micrometer `1.16.4 → 1.17.0`, logback `1.5.32 → 1.5.38`, kotlin-logging `8.0.01 → 8.0.4`,
  common-utils `2.7.1 → 3.1.0`.
- Swap the dependency-updates plugin from `com.pambrose.stable-versions` to the ben-manes
  `com.github.ben-manes.versions`. `configureVersions()` now rejects pre-release candidates
  (`rc`/`beta`/`alpha`/milestone/`snapshot`/`eap`/`dev`/`pre`) in the `dependencyUpdates` report — unless the current
  version is already on a pre-release line, so a detekt alpha still surfaces newer alphas.

### Refactoring

- Reduce the cyclomatic complexity of `AdminJobs.startCallbackThread`, `FunctionDetails.invokeMethod()`, and
  `ModelSerializer.serialize()` by extracting focused private helpers, dropping their
  `@Suppress("CyclomaticComplexMethod")` annotations. Behavior is preserved.
- Replace `ModelSerializer`'s 15-branch `when` with a `KClass`-keyed serializer registry, and promote
  `assignEnumOverrides()` to a default no-op on the `CommonModelDto` interface so it is applied uniformly.

### Tests

- New `CallbackDispatchTest` covers global/per-application request/response fan-out, per-type filtering, and
  applicationId routing.
- New `ModelSerializerTest` covers all 15 model providers plus the registry-miss branch.
- Added coverage for the tool-call failure path, the admin `validate` not-found rendering, and the restructured
  `FunctionDetails.invokeMethod()` paths (suspend invocation, parameter-not-found, arg-log rendering).

### Fixes

- `dokkaGenerate` no longer fails with a circular-evaluation error. The top-level `moduleName` constant was shadowed
  by the Dokka extension's own `moduleName` property, so `moduleName.set(moduleName)` wired the property to itself;
  the constant is renamed to `dokkaModuleName`.
- Test stderr is no longer silently suppressed. `showStandardStreams = false` removes `STANDARD_ERROR` from the
  test-logging event set, which was negating the `STANDARD_ERROR` event configured just above it; the line is dropped.
- `ValidateApplication`: restore the kotlinx.html `unaryPlus` on two `p { ... }` blocks so error text renders instead
  of empty `<p>` elements.
- `SerializationIssue`: make `DataChild` implement the sealed `Child` interface so the `value as DataChild` cast is
  valid.
- Quiet testcontainers/docker-java wire logging during test runs.

### Build & Makefile

- Switch `BuildConfig.RELEASE_DATE` and `BuildConfig.BUILD_TIME` to `ValueSource`-backed providers so they reflect the
  actual build time on every invocation rather than the value frozen by the configuration cache.
- Centralize repositories in `settings.gradle.kts` and versions in `gradle/libs.versions.toml`; scope Dokka generation
  to publishable subprojects (skip `vapi4k-snippets`); enable the Gradle build cache.
- Consolidate the `DependencyUpdatesTask` configuration into a single block in `configureVersions()`, so the
  configuration-cache opt-out sits next to the `doLast` that reads the version catalog.
- Add a `make help` target with auto-discovered descriptions and a usage header, plus `lint`, `detekt`, and `format`
  targets.

## Full Changelog

https://github.com/vapi4k/vapi4k/compare/1.7.0...1.8.0
