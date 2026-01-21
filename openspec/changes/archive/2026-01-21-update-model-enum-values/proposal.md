# Change: Update Model Enum Values to Match Vapi TypeScript SDK

## Why

The vapi4k model enum values are outdated compared to Vapi's official TypeScript SDK. New models have been released (
GPT-4.1, Claude Opus 4.5, Llama 4.x, etc.) and deprecated models should be removed to ensure users can access the latest
AI capabilities and prevent usage of unsupported models.

## What Changes

- Update `OpenAIModelType` enum with latest GPT models from Vapi TypeScript SDK (40+ models including GPT-5.x series,
  O3/O4 series, realtime models)
- Update `AnthropicModelType` enum with latest Claude models (12 models including Claude 3.7, Claude 4.x, Claude 4.5.x
  series)
- Update `GroqModelType` enum with latest Groq models (15 models including DeepSeek, Llama 3.3, Llama 4.x, Mistral, and
  Compound series)
- **BREAKING**: Remove deprecated/unsupported model values that no longer exist in Vapi's TypeScript SDK
- Maintain existing `UNSPECIFIED` enum value for each provider
- Preserve existing `desc`, `isSpecified()`, and `isNotSpecified()` patterns

## Impact

- Affected specs: `model-types`
- Affected code:
  - `vapi4k-core/src/main/kotlin/com/vapi4k/api/model/OpenAIModelType.kt`
  - `vapi4k-core/src/main/kotlin/com/vapi4k/api/model/AnthropicModelType.kt`
  - `vapi4k-core/src/main/kotlin/com/vapi4k/api/model/GroqModelType.kt`
- Breaking change: Code using removed model enum values will need to update to supported models or use `customModel`
  property
- Tests using specific model types may need updates
