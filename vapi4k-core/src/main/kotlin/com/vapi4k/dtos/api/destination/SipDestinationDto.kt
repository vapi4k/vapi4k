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

package com.vapi4k.dtos.api.destination

import com.vapi4k.api.destination.DestinationType
import com.vapi4k.api.destination.SipDestination
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class SipDestinationDto(
  override var message: String = "",
  override var description: String = "",
  override var sipUri: String = "",
) : CommonDestinationDto,
  SipDestination {
  @EncodeDefault
  val type: DestinationType = DestinationType.SIP
}
