# Release Notes — Unreleased

Notes collected for the next release. Fold them into its release notes when preparing it.

## Changes

### Dependencies

- Ktor `3.6.0 → 3.6.1`
- kotlinx-serialization `1.11.0 → 1.13.0`. `vapi4k-utils` exports it as an `api` dependency. 1.12.0 began wrapping
  exceptions thrown by serializers, and 1.13.0 reverted that, so exceptions reach your code as they did with 1.11.0.
- common-utils `5.1.1 → 5.2.0`
- kotlin-logging `8.0.4 → 8.0.7`
- logback `1.6.5 → 1.6.6`
- Micrometer `1.17.1 → 1.17.2`
- Exposed `1.5.1 → 1.6.1`
- Flyway `13.10.0 → 13.12.1`
- Kotest `6.2.5 → 6.2.6`
- Testcontainers `1.21.4 → 2.0.5`

### Build tooling

- Gradle `9.8.1 → 9.9.1`
- Dokka `2.2.0 → 2.3.0`
- pambrose gradle-plugins (kotlinter) `1.1.7 → 1.1.9`
- ben-manes versions plugin `0.66.0 → 0.67.0`

### Tests

- The `vapi4k-dbms` tests use Testcontainers 2: the `org.testcontainers:testcontainers-postgresql` artifact replaces
  `org.testcontainers:postgresql`, and `PostgreSQLContainer` moves to `org.testcontainers.postgresql`. The build no
  longer sets `DOCKER_API_VERSION` and `-Dapi.version=1.44` for Docker Desktop, because Testcontainers 2 tries Docker
  API 1.44 before falling back to 1.32.

### CI

- Action upgrades: setup-java v6.2.0, setup-gradle v6.4.1, upload-artifact v7.0.2, download-artifact v8.0.2, and
  zizmor-action v0.8.1. setup-gradle v6.4.1 is a security release for
  [GHSA-chpw-2mqm-95cc](https://github.com/gradle/actions/security/advisories/GHSA-chpw-2mqm-95cc), which affects the
  dependency graph's `download-and-submit` mode. The workflows here don't submit a dependency graph, so they were not
  exposed.

---

# Release Notes — v1.8.2

## Highlights

A maintenance release. It moves vapi4k to **Kotlin 2.4.21**, **Ktor 3.6.0**, and **common-utils 5.1.1**, and secures
the project's GitHub Actions: [zizmor](https://docs.zizmor.sh) now audits the workflows on every push and pull request,
every action is pinned to a commit SHA, and Dependabot proposes updates for both the actions and the Gradle
dependencies.

There are no vapi4k API or wire-format changes. The one change that can reach your code comes through a dependency;
see **Upgrading from 1.8.1**.

## Upgrading from 1.8.1

`vapi4k-utils` exports common-utils' `json-utils` as an `api` dependency, so your code compiles against it, and this
release moves it from `3.2.2` to `5.1.1`:

- **Deprecated:** `String.toJsonElement()` and `String.toJsonString()`. Use `parseJson()` and `reformatJson()`; each
  deprecation carries a `ReplaceWith`, so the IDE can apply the fix. Migrate rather than suppress the warnings: once
  the `String` overloads are removed, `"{...}".toJsonElement()` will still compile but bind to the generic
  `T.toJsonElement()`, which encodes the string as a JSON string literal instead of parsing it.
- **Stricter parsing and number accessors:** `parseJson` rejects unquoted non-JSON tokens, and `intValue` accepts only
  JSON integers. `doubleValue` throws `NumberFormatException` for text that is not a JSON number, where
  `doubleValueOrNull` returns `null` and `isNumber` returns `false`. `getByPath` returns `null` when the path runs into
  a value that is not an object.
- **New:** `longValue` and `longValueOrNull`.

## Changes

### Dependencies

- Kotlin `2.4.10 → 2.4.21`
- Ktor `3.5.2 → 3.6.0`
- common-utils `3.2.2 → 5.1.1`
- Exposed `1.3.1 → 1.5.1`
- logback `1.6.1 → 1.6.5`
- Micrometer `1.17.0 → 1.17.1`
- PostgreSQL JDBC `42.7.13 → 42.7.14`
- Flyway `13.1.0 → 13.10.0`
- Kotest `6.2.3 → 6.2.5`

### Build tooling

- Gradle `9.6.1 → 9.8.1`
- detekt `2.0.0-alpha.5 → 2.0.0-alpha.6`
- Kover `0.9.9 → 0.9.11`
- buildconfig plugin `6.0.10 → 6.1.2`
- pambrose gradle-plugins (kotlinter) `1.1.1 → 1.1.7`
- ben-manes versions plugin `0.57.0 → 0.66.0`

### CI & security

- **zizmor:** a new `zizmor` workflow audits the workflows and the Dependabot config, uploading its findings to
  Security → Code scanning, and `make zizmor` runs the same audit locally.
- **Hardened workflows:** every action is pinned to a commit SHA with a version comment, each job gets only the
  permissions it needs (`pages: write` and `id-token: write` only in the docs deploy job), and checkouts no longer
  keep the GitHub token in the workspace.
- **Dependabot:** weekly version updates with a 7-day cooldown, so a release that is pulled or found compromised in its
  first week is never proposed. GitHub Actions updates arrive in one grouped PR. Gradle dependencies, plugins and the
  wrapper arrive with minor and patch updates grouped, and each major update in a PR of its own.
- **Action upgrades:** checkout v7, setup-java v6, setup-gradle v6, codecov-action v7, upload-artifact v7,
  download-artifact v8, configure-pages v6, upload-pages-artifact v5, and deploy-pages v5. From v6, setup-gradle's
  default caching uses Gradle's proprietary `gradle-actions-caching` component under the
  [Gradle Terms of Use](https://gradle.com/legal/terms-of-use/); `cache-provider: basic` switches to the open-source
  implementation.

### Build & Makefile

- `make zizmor` audits the GitHub Actions workflows and the Dependabot config.
- `make versions` now lists only the dependencies with newer versions and any that could not be checked;
  `make versions-all` prints the plugin's full report, up-to-date dependencies included.
- Makefile recipes use a `$(GRADLE)` variable.
- `.gitattributes` normalizes text files (LF, with CRLF for `*.bat`), marks the vendored Bootstrap and Prism assets,
  and suppresses diffs of source maps.

### Documentation

- The JsonElement page on the website covers `parseJson()` and `reformatJson()`, and its example uses `parseJson()`.
- `llms.txt` describes the JSON helpers and the project's GitHub Actions audits, and the README's module table names
  the JSON helpers `vapi4k-utils` exports.
- CLAUDE.md is trimmed to what the code does not already say, and the recipe for adding a provider is now the
  `adding-a-provider` Claude Code skill.

## Full Changelog

https://github.com/vapi4k/vapi4k/compare/1.8.1...1.8.2
