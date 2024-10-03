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

package com.vapi4k.api.vapi4k

import com.vapi4k.dsl.vapi4k.CommonCallbacks
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

@Vapi4KDslMarker
interface Vapi4kConfig : CommonCallbacks {
  /**
  Creates a context for an InboundCall application.
   */
  fun inboundCallApplication(block: InboundCallApplication.() -> Unit): InboundCallApplication

  /**
  Creates a context for an InboundCall application.
   */
  fun outboundCallApplication(block: OutboundCallApplication.() -> Unit): OutboundCallApplication

  /**
  Creates a context for an InboundCall application.
   */
  fun webApplication(block: WebApplication.() -> Unit): WebApplication
}
