<show-structure depth="2"/>

# Tools and Functions

Tools and functions
The Vapi4k DSL allows you to assign tools and functions to assistants, which allows
an LLM to perform tasks and calculations.

## Tools

The `tools{}` lambda can contain `serviceTool()`, `manualTool()`, `externalTool()`, and `transferTool()` calls.

### ServiceTool

The `serviceTool()` argument can be either an object instance or a reference to a Kotlin singleton object.
The return value and the method parameters are limited to: `String`, `Int`, `Double`, `Boolean`, and `Unit`.

If you want access to requestContext value of the tool call in the serviceTool implementation, add a
parameter of type `RequestContext` to the method signature. It will be automatically injected along with the
LLM arguments.

<chapter title="" id="serviceTool" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/tools/ServiceTools.kt" include-symbol="serviceToolExample"/>
</chapter>


Here are some examples of serviceTool implementations.

In the case of a non-annotated function, only a single method is allowed in the object.

<chapter title="Non-annotated Function" id="addTwoNumbers" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/tools/ToolCalls.kt" include-symbol="AddTwoNumbers"/>
</chapter>

The `@ToolCall` and `@Param` annotations are used to express intent to the LLM.
The `@ToolCall` annotation describes when a function or serviceTool should be called, and the `@Param` annotation
describes the parameters of the function or serviceTool. If a function or serviceTool is not annotated, the LLM will
infer when to invoke the function based on the function name, and the arguments from the parameter names.

Multiple methods can have `@ToolCall` annotations. In addition, other non-annotated methods in the object are also
allowed.

<chapter title="Annotated Function" id="multiplyTwoNumbers" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/tools/ToolCalls.kt" include-symbol="MultiplyTwoNumbers"/>
</chapter>

This is an example of using a Kotlin singleton object.

<chapter title="Singleton Object Function" id="absoluteValue" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/tools/ToolCalls.kt" include-symbol="AbsoluteValue"/>
</chapter>

A `tool{}` implmentation object can also inherit from `ToolCallService()`

<chapter title="Singleton Object Function" id="toolCallService" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/tools/ToolCalls.kt" include-symbol="WeatherLookupService"/>
</chapter>

### ManualTool

A `manualTool()` describes a function where the implementation is within the Vapi4k DSL. The parameters are manually
extracted from the `args` JsonElement, which is the JSON object included in the LLM request.
A `manualTool()` is not associated with a specific Kotlin object.

<chapter title="" id="manualTool" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/tools/ManualTools.kt" include-symbol="manualToolExample"/>
</chapter>

### ExternalTool

An `externalTool()` describes a function executed by a REST endpoint at a specified server.

<chapter title="" id="externalTool" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/tools/ExternalTools.kt" include-symbol="externalToolExample"/>
</chapter>

### TransferTool

A `transferTool{}` describes a tool that transfers a user to an assistant or a phone/sip number.

<chapter title="" id="assistantDestination" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/tools/TransferTools.kt" include-symbol="assistantDestinationExample"/>
</chapter>

<chapter title="" id="numberDestination" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/tools/TransferTools.kt" include-symbol="numberDestinationExample"/>
</chapter>

## Functions

The `functions{}` lambda contains `function()` calls.
The `function()` argument is the same as `serviceTool()` calls.

<chapter title="Function Declaration" id="function" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/tools/Functions.kt" include-symbol="functionExample"/>
</chapter>
