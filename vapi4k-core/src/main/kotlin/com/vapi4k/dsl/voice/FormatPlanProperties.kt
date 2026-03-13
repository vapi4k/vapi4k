/*
 * Copyright © 2024 Matthew Ambrose
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.vapi4k.dsl.voice

interface FormatPlanProperties {
  /**
  <p>This determines whether the chunk is reformatted before being sent to the voice provider. Many things are reformatted
  including phone numbers, emails and addresses to improve their enunciation.
  <br>Default <code>true</code> because voice generation sounds better with reformatting.
  <br>To disable chunk reformatting, set this to <code>false</code>.
  </p>
   */
  var enabled: Boolean?

  /**
  <p>This is the cutoff after which a number is converted to individual digits instead of being spoken as a whole number.
  <br>Example: if set to 2025, the number 2024 will be spoken as "twenty twenty-four" but 2026 will be spoken as
  "two zero two six".
  <br>Set to 0 to always convert to digits. Set to a very large number to never convert to digits.
  <br>Defaults to -1 (unset, uses Vapi default behavior).
  </p>
   */
  var numberToDigitsCutoff: Int
}
