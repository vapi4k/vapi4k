# JSONElement Usage

The `JsonElement` class is the base class for all JSON elements in Kotlin. It represents a JSON value, which can
be a JSON object, a JSON array, a JSON string, a JSON number, a JSON boolean, or JSON null.

[JsonElementUtils](%utils_url%.json/)
provides utility functions for working with `JsonElement` objects.

To turn a JSON string into a `JsonElement`, call `parseJson()` on it; `reformatJson()` re-encodes a JSON string,
pretty-printed by default. Since vapi4k 1.8.2, the older `String.toJsonElement()` and `String.toJsonString()` are
deprecated in their favor.

Many of the `JsonElement` functions have a `vararg String` path argument.
These arguments are used to navigate the JSON object hierarchy. A path can be either comma-separated
strings or a single dot-separated string.

<chapter title="Simple JSONElement Example" id="squadId" collapsible="false">
<code-block lang="kotlin" src="src/main/kotlin/utils/JsonElements.kt" include-symbol="jsonElementExample"/>
</chapter>
