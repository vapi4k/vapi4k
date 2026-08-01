# Release Notes — v1.8.1

## Highlights

A small maintenance release. It carries a routine batch of dependency bumps — most notably **Kotlin 2.4.10**,
**Ktor 3.5.2**, **logback 1.6.1**, and **Flyway 13.1.0** — and repairs the `dependencyUpdates` task, which had been
silently pinned to a stale version of the ben-manes plugin after that plugin changed its published id.

There are no API or wire-format changes in this release. Downstream consumers can bump from `1.8.0` to `1.8.1` with no
source changes.

## Changes

### Dependencies

- Kotlin `2.4.0 → 2.4.10`
- Ktor `3.5.1 → 3.5.2`
- logback `1.5.38 → 1.6.1`
- Flyway `12.11.0 → 13.1.0`
- Kotest `6.2.2 → 6.2.3`
- Kover `0.9.8 → 0.9.9`
- common-utils `3.1.0 → 3.2.2`
- pambrose gradle-plugins (kotlinter) `1.1.0 → 1.1.1`
- ben-manes versions plugin `0.54.0 → 0.57.0`

### Fixes

- The dependency-updates plugin id moved from `com.github.ben-manes.versions` to `io.github.ben-manes.versions`. The
  old coordinate is frozen at `0.54.0`, so the build was resolving an outdated plugin every time `dependencyUpdates`
  ran. The version catalog now points at the relocated id.

### Build & Makefile

- Add a `make depends` target that runs `./gradlew dependencies` to print the project dependency tree.

## Full Changelog

https://github.com/vapi4k/vapi4k/compare/1.8.0...1.8.1
