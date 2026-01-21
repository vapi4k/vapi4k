# Implementation Tasks

## 1. Update OpenAIModelType Enum

- [x] 1.1 Update `OpenAIModelType.kt` with all model values from Vapi TypeScript SDK
- [x] 1.2 Remove deprecated OpenAI models no longer in Vapi SDK
- [x] 1.3 Verify enum naming follows PascalCase convention with underscores
- [x] 1.4 Ensure `UNSPECIFIED` value remains at the end

## 2. Update AnthropicModelType Enum

- [x] 2.1 Update `AnthropicModelType.kt` with all Claude model values from Vapi TypeScript SDK
- [x] 2.2 Add new Claude 3.7, Claude 4.x, and Claude 4.5.x models
- [x] 2.3 Verify enum naming follows PascalCase convention
- [x] 2.4 Ensure `UNSPECIFIED` value remains at the end

## 3. Update GroqModelType Enum

- [x] 3.1 Update `GroqModelType.kt` with all model values from Vapi TypeScript SDK
- [x] 3.2 Remove deprecated Groq models (llama3-groq tool-use preview models)
- [x] 3.3 Add new Llama 3.3, Llama 4.x, DeepSeek, Mistral, and Compound models
- [x] 3.4 Verify enum naming follows PascalCase convention
- [x] 3.5 Ensure `UNSPECIFIED` value remains at the end

## 4. Update Tests

- [x] 4.1 Review test files using model enum values
- [x] 4.2 Update tests using deprecated models to use supported models
- [x] 4.3 Run full test suite to verify no breakages

## 5. Validation

- [x] 5.1 Build project successfully: `./gradlew build -x test`
- [x] 5.2 Run all tests: `./gradlew test`
- [x] 5.3 Run linting: `./gradlew lintKotlin`
- [x] 5.4 Verify JSON serialization produces correct model strings
