<show-structure depth="2"/>

# Assistants and Squads

A `onAssistantRequest{}` call is used to define the behavior of an application.
It is required in all application descriptions. A call to `onAssistantRequest{}`
should define a single `assistant{}`, `squad{}`, `assistantId{}`, or a `squadId{}`.
Calls to `assistant{}` and `assistantId{}` can also include a call to `assistantOverrides{}`.

## Assistants

An `assistant{}` call creates a temporary assistant that lasts for the duration of the call.

<chapter title="Assistant Example" id="assistant" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/assistants/Assistants.kt" include-symbol="assistantExample"/>
</chapter>

## Squads

A `squad{}` call creates a temporary squad that lasts for the duration of the call.

<chapter title="Squad Example" id="squad" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/assistants/Assistants.kt" include-symbol="squadExample"/>
</chapter>

## AssistantIds

An `assistantId{}` call references a static assistant defined in
the [Vapi dashboard](https://dashboard.vapi.ai/assistants).

A `assistantOverrides{}` call is used to override the default assistant settings and its argument has a
[`AssistantOverrides`](%core_url%.assistant/-assistant-overrides/index.html) context.

<chapter title="Assistant Id Example" id="assistantId" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/assistants/Assistants.kt" include-symbol="assistantIdExample"/>
</chapter>

## SquadIds

A `squadId{}` call references a static squad defined in the [Vapi dashboard](https://dashboard.vapi.ai/assistants).

<chapter title="Squad Id Example" id="squadId" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/assistants/Assistants.kt" include-symbol="squadIdExample"/>
</chapter>

## Models

* [Anthropic](%core_url%.model/-anthropic-model/index.html)
* [Anyscale](%core_url%.model/-anyscale-model/index.html)
* [CustomLLM](%core_url%.model/-custom-l-l-m-model/index.html)
* [DeepInfra](%core_url%.model/-deep-infra-model/index.html)
* [Groq](%core_url%.model/-groq-model/index.html)
* [OpenAI](%core_url%.model/-open-a-i-model/index.html)
* [OpenRouter](%core_url%.model/-open-router-model/index.html)
* [PerplexityAI](%core_url%.model/-perplexity-a-i-model/index.html)
* [TogetherAI](%core_url%.model/-together-a-i-model/index.html)
* [Vapi](%core_url%.model/-vapi-model/index.html)

### Model Examples

<tabs>
  <tab title="Anthropic">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Models.kt" include-symbol="anthropicExample"/>
  </tab>
  <tab title="Anyscale">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Models.kt" include-symbol="anyscaleExample"/>
  </tab>
  <tab title="CustomLLM">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Models.kt" include-symbol="customLLMExample"/>
  </tab>
  <tab title="DeepInfra">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Models.kt" include-symbol="deepInfraExample"/>
  </tab>
  <tab title="Groq">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Models.kt" include-symbol="groqExample"/>
  </tab>
  <tab title="OpenAI">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Models.kt" include-symbol="openAIExample"/>
  </tab>
  <tab title="OpenRouter">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Models.kt" include-symbol="openRouterExample"/>
  </tab>
  <tab title="PerplexityAI">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Models.kt" include-symbol="perplexityAIExample"/>
  </tab>
  <tab title="TogetherAI">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Models.kt" include-symbol="togetherAIExample"/>
  </tab>
  <tab title="Vapi">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Models.kt" include-symbol="vapiExample"/>
  </tab>
</tabs>

## Voices

* [Azure](%core_url%.voice/-azure-voice/index.html)
* [Cartesia](%core_url%.voice/-cartesia-voice/index.html)
* [Deepgram](%core_url%.voice/-deepgram-voice/index.html)
* [ElevenLabs](%core_url%.voice/-eleven-labs-voice/index.html)
* [LMNT](%core_url%.voice/-l-m-n-t-voice/index.html)
* [Neets](%core_url%.voice/-neets-voice/index.html)
* [OpenAI](%core_url%.voice/-open-a-i-voice/index.html)
* [PlayHT](%core_url%.voice/-play-h-t-voice/index.html)
* [RimeAI](%core_url%.voice/-rime-a-i-voice/index.html)

### Voice Examples

<tabs>
  <tab title="Azure">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Voices.kt" include-symbol="azureExample"/>
  </tab>
  <tab title="Cartesia">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Voices.kt" include-symbol="cartesiaExample"/>
  </tab>
  <tab title="Deepgram">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Voices.kt" include-symbol="deepgramExample"/>
  </tab>
  <tab title="ElevenLabs">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Voices.kt" include-symbol="elevenLabsExample"/>
  </tab>
  <tab title="LMNT">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Voices.kt" include-symbol="lmntExample"/>
  </tab>
  <tab title="Neets">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Voices.kt" include-symbol="neetsExample"/>
  </tab>
  <tab title="OpenAI">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Voices.kt" include-symbol="openAIExample"/>
  </tab>
  <tab title="PlayHT">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Voices.kt" include-symbol="playHTExample"/>
  </tab>
  <tab title="RimeAI">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Voices.kt" include-symbol="rimeAIExample"/>
  </tab>
</tabs>

## Transcribers

* [Deepgram](%core_url%.transcriber/-deepgram-transcriber/index.html)
* [Gladia](%core_url%.transcriber/-gladia-transcriber/index.html)
* [Talkscriber](%core_url%.transcriber/-talkscriber-transcriber/index.html)

### Transcriber Examples

<tabs>
  <tab title="Deepgram">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Transcribers.kt" include-symbol="deepgramExample"/>
  </tab>
  <tab title="Gladia">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Transcribers.kt" include-symbol="gladiaExample"/>
  </tab>
  <tab title="Talkscriber">
    <code-block lang="kotlin" src="src/main/kotlin/assistants/Transcribers.kt" include-symbol="talkscriberExample"/>
  </tab>
</tabs>
