---
name: vapi-enum-diff
description: Compare vapi4k enums against the Vapi OpenAPI spec and report differences
allowed-tools:
  - Bash
  - Read
  - Glob
  - Grep
---

Compare all vapi4k enum types against the live Vapi OpenAPI spec. Report only — do not make any changes.

## Steps

1. Fetch the Vapi OpenAPI spec by running: `curl -s https://api.vapi.ai/api-json`
   and extract all enum fields from `components.schemas` using python3.
   Output format: one line per schema field with its enum values.

2. Read all enum files in `vapi4k-core/src/main/kotlin/com/vapi4k/api/` that have
   a `desc: String` constructor parameter. Extract the `desc` values from each entry
   (excluding UNSPECIFIED).

3. Match each enum file to the corresponding OpenAPI schema by finding the schema
   whose enum values best overlap with the enum's `desc` values.

4. For each matched enum, report:
   - **New in spec**: values in the spec but not in the enum (need to be added)
   - **Removed from spec**: values in the enum but not in the spec (may need removal)
   - Skip enums that are fully in sync (just count them at the end)

5. Output a summary table. Do NOT modify any files.
