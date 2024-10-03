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

package com.vapi4k.dsl.assistant

import com.vapi4k.api.assistant.enums.VoicemailDetectionType

interface VoicemailDetectionProperties {
  /**
  This sets whether the assistant should detect voicemail. Defaults to true.
   */
  var enabled: Boolean?

  /**
  <p>The number of seconds that Twilio should attempt to perform answering machine detection before timing out andreturning AnsweredBy as unknown. Default is 30 seconds.
  <br>Increasing this value will provide the engine more time to make a determination. This can be useful when DetectMessageEnd is provided in the MachineDetection parameter and there is an expectation of long answering machine greetings that can exceed 30 seconds.
  <br>Decreasing this value will reduce the amount of time the engine has to make a determination. This can be particularly useful when the Enable option is provided in the MachineDetection parameter and you want to limit the time for initial detection.
  <br>Check the <a href="https://www.twilio.com/docs/voice/answering-machine-detection#optional-api-tuning-parameters" target="_blank">Twilio docs</a> for more info.
  <br>@default 30
  </p>
   */
  var machineDetectionTimeout: Int

  /**
  <p>The number of milliseconds that is used as the measuring stick for the length of the speech activity. Durations lower than this value will be interpreted as a human, longer as a machine. Default is 2400 milliseconds.
  <br>Increasing this value will reduce the chance of a False Machine (detected machine, actually human) for a long human greeting (e.g., a business greeting) but increase the time it takes to detect a machine.
  <br>Decreasing this value will reduce the chances of a False Human (detected human, actually machine) for short voicemail greetings. The value of this parameter may need to be reduced by more than 1000ms to detect very short voicemail greetings. A reduction of that significance can result in increased False Machine detections. Adjusting the MachineDetectionSpeechEndThreshold is likely the better approach for short voicemails. Decreasing MachineDetectionSpeechThreshold will also reduce the time it takes to detect a machine.
  <br>Check the <a href="https://www.twilio.com/docs/voice/answering-machine-detection#optional-api-tuning-parameters" target="_blank">Twilio docs</a> for more info.
  <br>@default 2400
  </p>
   */
  var machineDetectionSpeechThreshold: Int

  /**
  <p>The number of milliseconds of silence after speech activity at which point the speech activity is considered complete. Default is 1200 milliseconds.
  <br>Increasing this value will typically be used to better address the short voicemail greeting scenarios. For short voicemails, there is typically 1000-2000ms of audio followed by 1200-2400ms of silence and then additional audio before the beep. Increasing the MachineDetectionSpeechEndThreshold to ~2500ms will treat the 1200-2400ms of silence as a gap in the greeting but not the end of the greeting and will result in a machine detection. The downsides of such a change include:
  <li>Increasing the delay for human detection by the amount you increase this parameter, e.g., a change of 1200ms to 2500ms increases human detection delay by 1300ms.
  <li>Cases where a human has two utterances separated by a period of silence (e.g. a "Hello", then 2000ms of silence, and another "Hello") may be interpreted as a machine.
  <br>Decreasing this value will result in faster human detection. The consequence is that it can lead to increased False Human (detected human, actually machine) detections because a silence gap in a voicemail greeting (not necessarily just in short voicemail scenarios) can be incorrectly interpreted as the end of speech.
  <br>Check the <a href="https://www.twilio.com/docs/voice/answering-machine-detection#optional-api-tuning-parameters" target="_blank">Twilio docs</a> for more info.
  <br>@default 1200
  </p>
   */
  var machineDetectionSpeechEndThreshold: Int

  /**
  <p>The number of milliseconds of initial silence after which an unknown AnsweredBy result will be returned. Default is 5000 milliseconds.
  <br>Increasing this value will result in waiting for a longer period of initial silence before returning an 'unknown' AMD result.
  <br>Decreasing this value will result in waiting for a shorter period of initial silence before returning an 'unknown' AMD result.
  <br>Check the <a href="https://www.twilio.com/docs/voice/answering-machine-detection#optional-api-tuning-parameters" target="_blank">Twilio docs</a> for more info.
  <br>@default 5000
  </p>
   */
  var machineDetectionSilenceTimeout: Int
  val voicemailDetectionTypes: MutableSet<VoicemailDetectionType>
}
