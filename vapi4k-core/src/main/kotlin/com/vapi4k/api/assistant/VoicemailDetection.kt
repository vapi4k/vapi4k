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

package com.vapi4k.api.assistant

import com.vapi4k.dsl.assistant.VoicemailDetectionProperties
import com.vapi4k.dsl.vapi4k.Vapi4KDslMarker

/**
These are the settings to configure or disable voicemail detection. Alternatively, voicemail detection can be configured
using the model.tools=[VoicemailTool]. This uses Twilio's built-in detection while the VoicemailTool relies on the model
to detect if a voicemail was reached. You can use neither of them, one of them, or both of them. By default, Twilio
built-in detection is enabled while VoicemailTool is not.
 */
@Vapi4KDslMarker
interface VoicemailDetection : VoicemailDetectionProperties
