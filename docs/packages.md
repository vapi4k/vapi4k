# Module vapi4k

Vapi4k is a Ktor plugin and Kotlin DSL for building voice AI applications with Vapi.ai.
It provides type-safe builders for configuring assistants, tools, models, voices, and call workflows.

# Package com.vapi4k.api.assistant

Assistant configuration interfaces — models, transcribers, voices, functions, overrides, analysis/artifact plans, and
voicemail detection.

# Package com.vapi4k.api.buttons

Button UI configuration, state, color, position, and type definitions.

# Package com.vapi4k.api.call

Outbound call abstractions, phone configurations, and Vapi API object types.

# Package com.vapi4k.api.conditions

Condition operators for conditional logic in tool messages and workflows.

# Package com.vapi4k.api.destination

Transfer destination types (number, SIP, assistant, step) and destination mode configurations.

# Package com.vapi4k.api.functions

Interface for defining callable functions on assistants.

# Package com.vapi4k.api.model

LLM provider abstractions for all 10 providers (OpenAI, Anthropic, Groq, DeepInfra, Anyscale, TogetherAI, OpenRouter,
PerplexityAI, CustomLLM, Vapi) and knowledge base configuration.

# Package com.vapi4k.api.prompt

Prompt interface for system instructions and message templates.

# Package com.vapi4k.api.response

Response interfaces for inbound/outbound calls, web interactions, and built-in tools (end call, DTMF, voicemail).

# Package com.vapi4k.api.squad

Squad configuration and member definitions for multi-assistant setups.

# Package com.vapi4k.api.tools

Tool abstractions (manual, external, service-based), tool messages, transfer tool, @ToolCall/@Param annotations, and
tool conditions.

# Package com.vapi4k.api.toolservice

ToolCallService base class and completion/failure condition interfaces for dynamic post-invocation messages.

# Package com.vapi4k.api.transcriber

Transcriber provider abstractions (Deepgram, Gladia, Talkscriber) with language and model types.

# Package com.vapi4k.api.vapi4k

Core application types (InboundCallApplication, OutboundCallApplication, WebApplication), Vapi4kConfig, RequestContext,
ResponseContext, and ServerRequestType.

# Package com.vapi4k.api.voice

Voice provider abstractions for all 9 providers (OpenAI, ElevenLabs, Deepgram, Azure, Cartesia, PlayHT, RimeAI, LMNT,
Neets) with voice IDs and model types.

# Package com.vapi4k.api.web

Web interaction abstractions including HTTP method types and talk button UI.

# Package com.vapi4k.api.dbms

Database connection pooling and message storage API.

# Package com.vapi4k.envvar

EnvVar class for environment variable management with HOCON config fallback, value masking, and auto-registration.
