# Bug Review Findings

## Task 1.1: Utility Functions - Edge Cases and Error Handling

### vapi4k-utils/src/main/kotlin/com/vapi4k/common/Utils.kt

#### BUG-001: Division by zero in `obfuscate()` (Severity: Medium)

**Location:** `Utils.kt:50`

```kotlin
fun String.obfuscate(freq: Int = 2) = mapIndexed { i, v -> if (i % freq == 0) '*' else v }.joinToString("")
```

**Issue:** If `freq` is 0, the modulo operation `i % freq` throws `ArithmeticException: / by zero`.
**Recommendation:** Add a guard clause: `require(freq > 0) { "freq must be positive" }` or handle gracefully.

---

#### BUG-002: `URLDecoder.decode()` can throw on malformed input (Severity: Low)

**Location:** `Utils.kt:79`

```kotlin
fun String.decode() = URLDecoder.decode(this, UTF_8.toString()) ?: this
```

**Issue:** `URLDecoder.decode()` throws `IllegalArgumentException` if the string contains invalid percent-encoded
sequences (e.g., `"%GG"`). The `?: this` fallback never executes because the exception is thrown before returning null.
**Recommendation:** Wrap in try-catch and return original string on failure, or document that callers must handle the
exception.

---

### vapi4k-utils/src/main/kotlin/com/vapi4k/envvar/EnvVar.kt

#### BUG-003: `toInt()` throws unchecked `NumberFormatException` (Severity: Medium)

**Location:** `EnvVar.kt:53`

```kotlin
fun toInt(): Int = value.toInt()
```

**Issue:** If `value` is not a valid integer (e.g., empty string, non-numeric), this throws `NumberFormatException` with
no handling.
**Recommendation:** Either document the exception, provide a `toIntOrNull()` alternative, or catch and throw a more
descriptive error.

---

#### BUG-004: `getEnv(default: Int)` can throw on invalid env var value (Severity: Medium)

**Location:** `EnvVar.kt:61`

```kotlin
fun getEnv(default: Int) = getEnvOrNull()?.toInt() ?: default
```

**Issue:** If the environment variable exists but contains a non-numeric value (e.g., `"abc"`), `toInt()` throws
`NumberFormatException` instead of using the default.
**Recommendation:** Use `toIntOrNull() ?: default` for safer parsing.

---

#### BUG-005: Thread-unsafe mutable map for `envVars` (Severity: Low)

**Location:** `EnvVar.kt:81`

```kotlin
private val envVars = mutableMapOf<String, EnvVar>()
```

**Issue:** Multiple threads creating `EnvVar` instances concurrently could cause race conditions when writing to this
shared map (in `init` block, line 36).
**Recommendation:** Use `ConcurrentHashMap` or synchronize access. In practice, `EnvVar` instances are typically created
at startup, so risk is low.

---

### vapi4k-utils/src/main/kotlin/com/vapi4k/api/vapi4k/ServerRequestType.kt

#### BUG-006: Overly broad exception catch (Severity: Low)

**Location:** `ServerRequestType.kt:72-77`

```kotlin
return try {
  entries.first { it.desc == messageType }
} catch (e: Exception) {
  logger.error { "Invalid ServerRequestType: $messageType" }
  UNKNOWN_REQUEST_TYPE
}
```

**Issue:** Catches all exceptions, not just `NoSuchElementException`. This could mask unexpected errors like
`OutOfMemoryError` propagation issues or other runtime problems.
**Recommendation:** Catch `NoSuchElementException` specifically, or use
`entries.firstOrNull { ... } ?: run { log; UNKNOWN_REQUEST_TYPE }`.

---

#### INFO-001: External dependency for JSON parsing

**Location:** `ServerRequestType.kt:19`

```kotlin
import com.github.pambrose.common.json.stringValue
```

**Note:** The `stringValue` function comes from an external library. Its error handling behavior is not directly
controllable. If `message.type` path doesn't exist in the JSON, behavior depends on that library's implementation.

---

## Task 1.2: JSON Serialization Utilities - Malformed Input Handling

### vapi4k-utils/src/main/kotlin/com/vapi4k/envvar/EnvVar.kt

#### INFO-002: Minimal JSON code in vapi4k-utils

**Location:** `EnvVar.kt:92-98`

```kotlin
fun jsonEnvVarValues() =
  buildJsonObject {
    envVars.values
      .filter { it.reportOnBoot && it.value.isNotBlank() }
      .sortedBy { it.name }
      .forEach { put(it.name, JsonPrimitive(it.logValue)) }
  }
```

**Note:** This is the only JSON serialization in vapi4k-utils. It builds a simple JSON object for env var reporting.
No malformed input handling issues - it only outputs pre-validated string values. The main JSON
serialization/deserialization
logic resides in vapi4k-core (DTOs) and will be reviewed in task 2.8.

**No bugs found in this task for vapi4k-utils.**

---

## Task 1.3: Logging Utilities - Potential Issues

### vapi4k-utils module

#### INFO-003: No custom logging utilities

**Note:** The vapi4k-utils module uses `KotlinLogging` from the `io.github.oshai` library directly (seen in
`ServerRequestType.kt:20`). There are no custom logging wrapper utilities in this module that could introduce bugs.

The logging configuration in `EnvVar.kt:70`:

```kotlin
System.setProperty("kotlin-logging.throwOnMessageError", "true")
```

This is intentional - it ensures logging errors are surfaced during development rather than silently ignored.

**No bugs found in this task for vapi4k-utils.**

---

## Task 1.4: String/Collection Utilities - Null Safety

### vapi4k-utils/src/main/kotlin/com/vapi4k/common/Utils.kt

#### INFO-004: String utilities reviewed

The string utilities in `Utils.kt` were reviewed in Task 1.1. Key null safety observations:

- `ensureStartsWith()` / `ensureEndsWith()`: Safe - called on non-null String receivers
- `trimLeadingSpaces()`: Safe - handles empty strings correctly
- `capitalizeFirstChar()`: Safe - handles empty strings (returns empty)
- `encode()` / `decode()`: See BUG-002 for decode() issue
- `isNull()` / `isNotNull()`: Properly use Kotlin contracts for smart casting

#### INFO-005: No collection utilities

The vapi4k-utils module does not contain custom collection utilities. Standard Kotlin collection
functions are used throughout.

**No additional bugs found in this task.**

---

## Summary

| ID      | File                       | Severity | Category       |
|---------|----------------------------|----------|----------------|
| BUG-001 | Utils.kt:50                | Medium   | Edge Case      |
| BUG-002 | Utils.kt:79                | Low      | Error Handling |
| BUG-003 | EnvVar.kt:53               | Medium   | Error Handling |
| BUG-004 | EnvVar.kt:61               | Medium   | Error Handling |
| BUG-005 | EnvVar.kt:81               | Low      | Thread Safety  |
| BUG-006 | ServerRequestType.kt:72-77 | Low      | Error Handling |

**Total Issues Found:** 6 bugs, 1 informational note
