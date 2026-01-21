# Model Types Specification Delta

## ADDED Requirements

### Requirement: GPT-5 Series Model Support

The system SHALL support OpenAI GPT-5 series models including GPT-5, GPT-5.1, GPT-5.2, GPT-5-mini, and GPT-5-nano
variants.

#### Scenario: Use GPT-5.2 model

- **WHEN** developer sets `modelType = OpenAIModelType.GPT_5_2`
- **THEN** the serialized model value SHALL be `"gpt-5.2"`

#### Scenario: Use GPT-5 chat latest

- **WHEN** developer sets `modelType = OpenAIModelType.GPT_5_CHAT_LATEST`
- **THEN** the serialized model value SHALL be `"gpt-5-chat-latest"`

### Requirement: O-Series Model Support

The system SHALL support OpenAI O-series models including O1-mini, O3, O3-mini, and O4-mini.

#### Scenario: Use O3 model

- **WHEN** developer sets `modelType = OpenAIModelType.O3`
- **THEN** the serialized model value SHALL be `"o3"`

#### Scenario: Use O1-mini with date

- **WHEN** developer sets `modelType = OpenAIModelType.O1_MINI_2024_09_12`
- **THEN** the serialized model value SHALL be `"o1-mini-2024-09-12"`

### Requirement: GPT-4.1 Series Model Support

The system SHALL support OpenAI GPT-4.1 series models including standard, mini, and nano variants with and without date
suffixes.

#### Scenario: Use GPT-4.1 model

- **WHEN** developer sets `modelType = OpenAIModelType.GPT_4_1`
- **THEN** the serialized model value SHALL be `"gpt-4.1"`

#### Scenario: Use GPT-4.1 mini with date

- **WHEN** developer sets `modelType = OpenAIModelType.GPT_4_1_MINI_2024_04_14`
- **THEN** the serialized model value SHALL be `"gpt-4.1-mini-2024-04-14"`

### Requirement: Realtime Preview Model Support

The system SHALL support OpenAI realtime preview models including GPT-4O variants and the production gpt-realtime model.

#### Scenario: Use GPT-4O realtime preview

- **WHEN** developer sets `modelType = OpenAIModelType.GPT_4O_REALTIME_PREVIEW_2024_12_17`
- **THEN** the serialized model value SHALL be `"gpt-4o-realtime-preview-2024-12-17"`

#### Scenario: Use production realtime model

- **WHEN** developer sets `modelType = OpenAIModelType.GPT_REALTIME_2025_08_28`
- **THEN** the serialized model value SHALL be `"gpt-realtime-2025-08-28"`

### Requirement: ChatGPT-4O Latest Model Support

The system SHALL support the ChatGPT-4O latest model variant.

#### Scenario: Use ChatGPT-4O latest

- **WHEN** developer sets `modelType = OpenAIModelType.CHATGPT_4O_LATEST`
- **THEN** the serialized model value SHALL be `"chatgpt-4o-latest"`

### Requirement: Claude 3.5 Haiku Model Support

The system SHALL support Claude 3.5 Haiku model variant.

#### Scenario: Use Claude 3.5 Haiku

- **WHEN** developer sets `modelType = AnthropicModelType.CLAUDE_3_5_HAIKU_20241022`
- **THEN** the serialized model value SHALL be `"claude-3-5-haiku-20241022"`

### Requirement: Claude 3.7 Sonnet Model Support

The system SHALL support Claude 3.7 Sonnet model variant.

#### Scenario: Use Claude 3.7 Sonnet

- **WHEN** developer sets `modelType = AnthropicModelType.CLAUDE_3_7_SONNET_20250219`
- **THEN** the serialized model value SHALL be `"claude-3-7-sonnet-20250219"`

### Requirement: Claude 4.x Series Model Support

The system SHALL support Claude 4.x series models including Opus-4 and Sonnet-4 variants.

#### Scenario: Use Claude Opus 4

- **WHEN** developer sets `modelType = AnthropicModelType.CLAUDE_OPUS_4_20250514`
- **THEN** the serialized model value SHALL be `"claude-opus-4-20250514"`

#### Scenario: Use Claude Sonnet 4

- **WHEN** developer sets `modelType = AnthropicModelType.CLAUDE_SONNET_4_20250514`
- **THEN** the serialized model value SHALL be `"claude-sonnet-4-20250514"`

### Requirement: Claude 4.5 Series Model Support

The system SHALL support Claude 4.5 series models including Opus-4.5, Sonnet-4.5, and Haiku-4.5 variants.

#### Scenario: Use Claude Opus 4.5

- **WHEN** developer sets `modelType = AnthropicModelType.CLAUDE_OPUS_4_5_20251101`
- **THEN** the serialized model value SHALL be `"claude-opus-4-5-20251101"`

#### Scenario: Use Claude Sonnet 4.5

- **WHEN** developer sets `modelType = AnthropicModelType.CLAUDE_SONNET_4_5_20250929`
- **THEN** the serialized model value SHALL be `"claude-sonnet-4-5-20250929"`

#### Scenario: Use Claude Haiku 4.5

- **WHEN** developer sets `modelType = AnthropicModelType.CLAUDE_HAIKU_4_5_20251001`
- **THEN** the serialized model value SHALL be `"claude-haiku-4-5-20251001"`

### Requirement: Llama 3.3 Model Support

The system SHALL support Llama 3.3 70B versatile model via Groq.

#### Scenario: Use Llama 3.3 70B

- **WHEN** developer sets `modelType = GroqModelType.LLAMA_3_3_70B_VERSATILE`
- **THEN** the serialized model value SHALL be `"llama-3.3-70b-versatile"`

### Requirement: Llama 3.1 Model Support

The system SHALL support Llama 3.1 models including 405B reasoning and 8B instant variants via Groq.

#### Scenario: Use Llama 3.1 405B reasoning

- **WHEN** developer sets `modelType = GroqModelType.LLAMA_3_1_405B_REASONING`
- **THEN** the serialized model value SHALL be `"llama-3.1-405b-reasoning"`

#### Scenario: Use Llama 3.1 8B instant

- **WHEN** developer sets `modelType = GroqModelType.LLAMA_3_1_8B_INSTANT`
- **THEN** the serialized model value SHALL be `"llama-3.1-8b-instant"`

### Requirement: Llama 4 Series Model Support

The system SHALL support Meta Llama 4 models including Maverick and Scout variants via Groq.

#### Scenario: Use Llama 4 Maverick

- **WHEN** developer sets `modelType = GroqModelType.META_LLAMA_LLAMA_4_MAVERICK_17B_128E_INSTRUCT`
- **THEN** the serialized model value SHALL be `"meta-llama/llama-4-maverick-17b-128e-instruct"`

#### Scenario: Use Llama 4 Scout

- **WHEN** developer sets `modelType = GroqModelType.META_LLAMA_LLAMA_4_SCOUT_17B_16E_INSTRUCT`
- **THEN** the serialized model value SHALL be `"meta-llama/llama-4-scout-17b-16e-instruct"`

### Requirement: DeepSeek R1 Model Support

The system SHALL support DeepSeek R1 distilled Llama 70B model via Groq.

#### Scenario: Use DeepSeek R1

- **WHEN** developer sets `modelType = GroqModelType.DEEPSEEK_R1_DISTILL_LLAMA_70B`
- **THEN** the serialized model value SHALL be `"deepseek-r1-distill-llama-70b"`

### Requirement: Gemma2 Model Support

The system SHALL support Google Gemma2 9B instruction-tuned model via Groq.

#### Scenario: Use Gemma2 9B

- **WHEN** developer sets `modelType = GroqModelType.GEMMA2_9B_IT`
- **THEN** the serialized model value SHALL be `"gemma2-9b-it"`

### Requirement: Mistral Saba Model Support

The system SHALL support Mistral Saba 24B model via Groq.

#### Scenario: Use Mistral Saba 24B

- **WHEN** developer sets `modelType = GroqModelType.MISTRAL_SABA_24B`
- **THEN** the serialized model value SHALL be `"mistral-saba-24b"`

### Requirement: Compound Series Model Support

The system SHALL support Compound model series including beta and beta-mini variants via Groq.

#### Scenario: Use Compound Beta

- **WHEN** developer sets `modelType = GroqModelType.COMPOUND_BETA`
- **THEN** the serialized model value SHALL be `"compound-beta"`

#### Scenario: Use Compound Beta Mini

- **WHEN** developer sets `modelType = GroqModelType.COMPOUND_BETA_MINI`
- **THEN** the serialized model value SHALL be `"compound-beta-mini"`

### Requirement: Moonshot AI Kimi Model Support

The system SHALL support Moonshot AI Kimi K2 instruct model via Groq.

#### Scenario: Use Kimi K2 Instruct

- **WHEN** developer sets `modelType = GroqModelType.MOONSHOTAI_KIMI_K2_INSTRUCT_0905`
- **THEN** the serialized model value SHALL be `"moonshotai/kimi-k2-instruct-0905"`

### Requirement: OpenAI OSS Model Support via Groq

The system SHALL support OpenAI OSS models (20B and 120B) via Groq.

#### Scenario: Use OpenAI GPT OSS 20B

- **WHEN** developer sets `modelType = GroqModelType.OPENAI_GPT_OSS_20B`
- **THEN** the serialized model value SHALL be `"openai/gpt-oss-20b"`

#### Scenario: Use OpenAI GPT OSS 120B

- **WHEN** developer sets `modelType = GroqModelType.OPENAI_GPT_OSS_120B`
- **THEN** the serialized model value SHALL be `"openai/gpt-oss-120b"`

### Requirement: OpenAI GPT-4O Model Variants

The system SHALL support all GPT-4O model variants with updated date versions.

#### Scenario: Use GPT-4O latest

- **WHEN** developer sets `modelType = OpenAIModelType.GPT_4O`
- **THEN** the serialized model value SHALL be `"gpt-4o"`

#### Scenario: Use GPT-4O with November 2024 date

- **WHEN** developer sets `modelType = OpenAIModelType.GPT_4O_2024_11_20`
- **THEN** the serialized model value SHALL be `"gpt-4o-2024-11-20"`

#### Scenario: Use GPT-4O with August 2024 date

- **WHEN** developer sets `modelType = OpenAIModelType.GPT_4O_2024_08_06`
- **THEN** the serialized model value SHALL be `"gpt-4o-2024-08-06"`

### Requirement: Claude 3.5 Sonnet Model Variants

The system SHALL support both Claude 3.5 Sonnet variants with different release dates.

#### Scenario: Use Claude 3.5 Sonnet June 2024

- **WHEN** developer sets `modelType = AnthropicModelType.CLAUDE_3_5_SONNET_20240620`
- **THEN** the serialized model value SHALL be `"claude-3-5-sonnet-20240620"`

#### Scenario: Use Claude 3.5 Sonnet October 2024

- **WHEN** developer sets `modelType = AnthropicModelType.CLAUDE_3_5_SONNET_20241022`
- **THEN** the serialized model value SHALL be `"claude-3-5-sonnet-20241022"`
