/*
 * Copyright © 2024 Matthew Ambrose (mattbobambrose@gmail.com)
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

package com.vapi4k.api.destination

import com.vapi4k.dsl.destination.CommonDestination
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

@Vapi4KDslMarker
interface NumberDestination : CommonDestination {
  /**
  This is the phone number to transfer the call to.
   */
  var number: String

  /**
  This is the extension to dial after transferring the call to the `number`.
   */
  var extension: String

  /**
  <p>This is the flag to toggle the E164 check for the <code>number</code> field. This is an advanced property which should be used if you know your use case requires it.
  Use cases:
  <ul><li><code>false</code>: To allow non-E164 numbers like <code>+001234567890</code>, <code>1234', or abc`</code>. This is useful for dialing out to non-E164 numbers on your SIP trunks.
  <li><code>true</code> (default): To allow only E164 numbers like <code>+14155551234</code>. This is for most standard PSTN calls.
  </ul>
  <br>If <code>false</code>, the <code>number</code> is still required to only contain alphanumeric characters (regex: <code>/^\+?[a-zA-Z0-9]+$/</code>).
  <br>@default true (E164 check is enabled)
  </p>
   */
  var numberE164CheckEnabled: Boolean?
}
