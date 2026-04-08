# Release Notes — v1.7.0

## Highlights

This release migrates Vapi4k from JitPack to **Maven Central** and updates the Maven group ID. Downstream
consumers must update their dependency declarations.

## Breaking Changes

### Maven coordinates changed

| | Before (1.6.2) | After (1.7.0) |
|---|---|---|
| **Repository** | `maven("https://jitpack.io")` | `mavenCentral()` |
| **Group ID** | `com.github.vapi4k.vapi4k` | `com.vapi4k` |
| **Example** | `com.github.vapi4k.vapi4k:vapi4k-core:1.6.2` | `com.vapi4k:vapi4k-core:1.7.0` |

**Migration:** Remove the JitPack repository from your `build.gradle.kts` (Maven Central is included by default)
and update the group ID in all dependency declarations:

```kotlin
// Before
repositories {
  maven("https://jitpack.io")
}
dependencies {
  implementation("com.github.vapi4k.vapi4k:vapi4k-core:1.6.2")
  implementation("com.github.vapi4k.vapi4k:vapi4k-dbms:1.6.2")
}

// After
dependencies {
  implementation("com.vapi4k:vapi4k-core:1.7.0")
  implementation("com.vapi4k:vapi4k-dbms:1.7.0")
}
```

## Changes

### Publishing

- Migrate artifact publishing from JitPack to Maven Central
- Adopt [Vanniktech Maven Publish](https://github.com/vanniktech/gradle-maven-publish-plugin) plugin for
  streamlined Central publishing with automatic release
- Add GPG signing support (conditional — skipped when no signing key is provided)
- Add POM metadata (license, developer info, SCM URLs) required by Maven Central

### Build

- Update `common-utils` dependency coordinates from `com.github.pambrose` to `com.pambrose` (group `2.7.1`)
- Update `gradle-plugins` from 1.0.10 to 1.0.12
- Remove unused Gradle plugins: `pambrose-repos`, `pambrose-snapshot`, `pambrose-testing`
- Remove unused `ktor-client-mock` test dependency
- Simplify `settings.gradle.kts` by removing JitPack plugin resolution strategy
- Centralize publishing configuration in root `build.gradle.kts` (removed per-module publishing blocks)
- Support version override via Gradle property (`-PoverrideVersion=...`) for snapshot builds

### Makefile

- Rename `publish` target to `publish-local`
- Add `publish-local-snapshot`, `publish-snapshot`, and `publish-maven-central` targets
- Remove JitPack-specific `trigger-jitpack` and `view-jitpack` targets
- Remove `dist` target

### Documentation

- Update README badge from JitPack to Maven Central
- Update installation instructions to remove JitPack repository requirement
- Update `llms.txt` with new coordinates and version
- Remove JitPack link from documentation references

### Internal

- Update all import paths from `com.github.pambrose.common` to `com.pambrose.common` across source and test files
- Simplify `.gitignore` by removing redundant exclusion rules
- Clean up opt-in annotations to use a loop instead of repeated calls

## Full Changelog

https://github.com/vapi4k/vapi4k/compare/1.6.2...1.7.0
