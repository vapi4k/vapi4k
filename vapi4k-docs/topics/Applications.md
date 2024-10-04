# Applications

The Vapi4k Ktor plugin allows you to define three types of applications:

* InboundCall
* OutboundCall
* Web

## InboundCall Applications

InboundCall applications handle requests from the Vapi platform resulting from calls to Vapi.
Responses are specified using the [`inboundCallApplication{}`](%core_url%.vapi4k/-inbound-call-application/)
function.

<chapter title="InboundCall Application Ktor Config" id="inboundApp" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/applications/IncomingCall.kt" include-symbol="module"/>
</chapter>

## OutboundCall Applications

OutboundCall applications are used to make outgoing calls from the Vapi platform.
Responses are specified using the [`outboundCallApplication{}`](%core_url%.vapi4k/-outbound-call-application/)
function.

<chapter title="OutboundCall Application Ktor Config" id="outboundApp" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/applications/OutgoingCall.kt" include-symbol="module"/>
</chapter>

<chapter title="OutboundCall Client" id="outboundClient" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/applications/CallCustomer.kt" include-symbol="outboundCallExample"/>
</chapter>

## Web Applications

Web applications are used to create web page-based conversations.
Responses are specified using the [`webCallApplication{}`](%core_url%.vapi4k/-web-application/) function.

<chapter title="Web Application Ktor Config" id="webAppKtor" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/applications/WebCall.kt" include-symbol="module"/>
</chapter>

<chapter title="Web Application HTML Page" id="webPage" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/applications/TalkPage.kt" include-symbol="talkPage"/>
</chapter>

## Application Properties

A Vapi4k configuration can include multiple application declarations. The default `serverPath` value is `/vapi4k`. If
there is more than one application of a given type, you will need to specify a unique `serverPath` property value for
each application.

The `serverSecret` property is optional.

## Application Functions

All applications require a call to `onAssistantRequest{}`. Its lambda will define
the desired assistant, assistantId, squad, or squadId for the request.

All applications allow you to define callbacks for requests and responses using the `onAllRequest{}`,
`onRequest{}`, `onAllResponse{}`, and `onResponse{}` functions.
The arguments for `onRequest{}` and `onResponse{}` are of type
[ServerRequestType](%utils_url%.vapi4k/-server-request-type/).

These functions are also available globally
in the [`Vapi4kConfig` context](%core_url%.vapi4k/-vapi4k-config/).

All applications can optionally call `onTransferDestinationRequest{}` to define a transfer destination.

[AssistantRequestUtils](%core_url%.vapi4k/-assistant-request-utils/) provides useful utility functions for
working with assistant requests.

When writing applications, you are likely to reference the
[RequestContext](%core_url%.vapi4k/-request-context/) object. It contains all the request data. The
raw JSON request is available in the `request` property and can be accessed with the `JSONElement` utilities
described [here](JsonElement.md).
