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

package com.vapi4k.console

import com.vapi4k.common.CssNames.LOG_DIV
import com.vapi4k.plugin.Vapi4kServer.logger
import com.vapi4k.utils.HtmlUtils.attribs
import com.vapi4k.utils.HtmlUtils.html
import com.vapi4k.utils.MiscUtils.removeEnds
import com.vapi4k.utils.SharedDataLoader
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.html.div

internal object AdminLog {
  suspend fun DefaultWebSocketServerSession.adminLogWs() {
    coroutineScope {
      launch {
        // Clear div on client after a server restart
        send(Frame.Text(RESET_HTML))

        SharedDataLoader.accessSharedFlow()
          .onStart { logger.info { "Listening for log updates" } }
          .onEach { msg ->
            val s = if (msg.isBlank()) msg else msg.removeEnds("\n")
            s.split("\n").forEach { line ->
              val html =
                html {
                  div {
                    attribs("hx-swap-oob" to "beforeend:#$LOG_DIV")
                    +"$line\n"
                  }
                }
              send(Frame.Text(html))
            }
          }
          .collect()
      }

      launch {
        for (frame in incoming) {
          if (frame is Frame.Text) {
            val text = frame.readText()
            // println("Received: $text")
          }
        }
      }
    }
  }

  private val RESET_HTML =
    html {
      div {
        attribs("hx-swap-oob" to "innerHTML:#$LOG_DIV")
        +""
      }
    }
}
