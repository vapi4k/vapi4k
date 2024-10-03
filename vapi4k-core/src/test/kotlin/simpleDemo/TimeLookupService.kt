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

package simpleDemo

import com.vapi4k.api.tools.ToolCall
import simpleDemo.Coasts.EAST
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TimeLookupService(
  val coast: Coasts,
) {
  @ToolCall(
    "Look up the current time for the given coast",
  )
  fun lookupTime(): String {
    val zoneId = ZoneId.of(
      if (coast == EAST) {
        "America/New_York"
      } else {
        "America/Los_Angeles"
      },
    )
    val currentTimeInZone = ZonedDateTime.now(zoneId)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formattedTime = currentTimeInZone.format(formatter)
    return "The current time on the $coast is $formattedTime."
  }
}
