---
name: adding-a-provider
description: Use when adding a new model, voice, or transcriber provider to vapi4k — lists the 5 files each provider needs and the 5 wiring points that are easy to miss.
---

# Adding a New Provider

Vapi4k's multi-provider abstraction covers 15 model providers, 18 voice providers, and 12 transcriber
providers. Each new provider is a fixed set of files plus wiring; the wiring is the part that gets
forgotten, because omitting it compiles fine and fails at serialization time.

## Model providers

Each model provider requires 5 files, following the `GroqModel` template:

1. `api/model/XxxModel.kt` — Interface extending `XxxModelProperties` + `CommonModelProperties`, annotated `@Vapi4KDslMarker`
2. `api/model/XxxModelType.kt` — Enum with `desc: String` constructor, `UNSPECIFIED(UNSPECIFIED_DEFAULT)`, `isSpecified()`/`isNotSpecified()`
3. `dsl/model/XxxModelProperties.kt` — Interface with `modelType`, `customModel`, `emotionRecognitionEnabled`, `maxTokens`, `numFastTurns`, `temperature`, `toolIds`
4. `dsl/model/XxxModelImpl.kt` — Internal class extending `AbstractModelImpl`, delegating properties `by modelDto`
5. `dtos/model/XxxModelDto.kt` — `@Serializable` data class with `@EncodeDefault` provider, `assignEnumOverrides()`, `verifyValues()`

Plus wiring in four places:

- the `ModelType` enum
- the `AssistantModels` interface
- `AbstractAssistantImpl` builders
- the `CommonModelDto` serializer registry

## Voice providers

Same shape — 5 files, replacing "model" with "voice" throughout — wired through `CommonVoiceDto` and
`AssistantVoices` instead.

## Before you start

Verify the provider's enum values against the live Vapi OpenAPI spec at `https://api.vapi.ai/api-json`.
Wire values are case-sensitive and must match the spec exactly. The `/vapi-enum-diff` command in this
repo compares the codebase's enums against that spec.

Follow the enum-serialization and DTO-serialization conventions documented in `CLAUDE.md` — they are
non-standard and the surrounding code will not teach you the right pattern by example alone.
