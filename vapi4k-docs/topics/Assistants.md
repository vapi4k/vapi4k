<show-structure depth="2"/>

# Assistants and Squads

A `onAssistantRequest{}` call is used to define the behavior of an application.
It is required in all application descriptions. A call to `onAssistantRequest{}`
should define a single `assistant{}`, `squad{}`, `assistantId{}`, or `squadId{}`.
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

A `assistantOverrides{}` call is used to override the default assistant settings, and its argument has an
[`AssistantOverrides`](%core_url%.assistant/-assistant-overrides/) context.

<chapter title="Assistant Id Example" id="assistantId" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/assistants/Assistants.kt" include-symbol="assistantIdExample"/>
</chapter>

## SquadIds

A `squadId{}` call references a static squad defined in the [Vapi dashboard](https://dashboard.vapi.ai/assistants).

<chapter title="Squad Id Example" id="squadId" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/assistants/Assistants.kt" include-symbol="squadIdExample"/>
</chapter>

## Models

* [Anthropic](%core_url%.model/-anthropic-model/)
* [Anyscale](%core_url%.model/-anyscale-model/)
* [CustomLLM](%core_url%.model/-custom-l-l-m-model/)
* [DeepInfra](%core_url%.model/-deep-infra-model/)
* [Groq](%core_url%.model/-groq-model/)
* [OpenAI](%core_url%.model/-open-a-i-model/)
* [OpenRouter](%core_url%.model/-open-router-model/)
* [PerplexityAI](%core_url%.model/-perplexity-a-i-model/)
* [TogetherAI](%core_url%.model/-together-a-i-model/)
* [Vapi](%core_url%.model/-vapi-model/)

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

* [Azure](%core_url%.voice/-azure-voice/)
* [Cartesia](%core_url%.voice/-cartesia-voice/)
* [Deepgram](%core_url%.voice/-deepgram-voice/)
* [ElevenLabs](%core_url%.voice/-eleven-labs-voice/)
* [LMNT](%core_url%.voice/-l-m-n-t-voice/)
* [Neets](%core_url%.voice/-neets-voice/)
* [OpenAI](%core_url%.voice/-open-a-i-voice/)
* [PlayHT](%core_url%.voice/-play-h-t-voice/)
* [RimeAI](%core_url%.voice/-rime-a-i-voice/)

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

* [Deepgram](%core_url%.transcriber/-deepgram-transcriber/)
* [Gladia](%core_url%.transcriber/-gladia-transcriber/)
* [Talkscriber](%core_url%.transcriber/-talkscriber-transcriber/)

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
