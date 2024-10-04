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

package tools

import com.vapi4k.api.destination.AssistantTransferMode
import com.vapi4k.dsl.model.CommonModelProperties

object TransferTools {
  fun CommonModelProperties.assistantDestinationExample() {
    tools {
      transferTool {
        assistantDestination {
          assistantName = "assistant1"
          transferMode = AssistantTransferMode.ROLLING_HISTORY
          message = "This is the assistant destination message"
          description = "Transfer the call to an assistant"
        }
      }
    }
  }

  fun CommonModelProperties.numberDestinationExample() {
    tools {
      transferTool {
        numberDestination {
          number = "+14155551212"
          message = "This is the number destination message"
          description = "Transfer the call to a phone number"
        }
      }
    }
  }
}
