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

package com.vapi4k.utils

import com.vapi4k.common.JsonContentUtils.defaultJson
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.ApplicationCall
import java.time.Duration

object HttpUtils {
  val httpClient by lazy {
    HttpClient(CIO) {
      install(ContentNegotiation) {
        json(defaultJson())
      }

      // TODO: Look into this
//      install(ContentEncoding) {
//        deflate(1.0F)
//        gzip(0.9F)
//      }
    }
  }

  fun jsonHttpClient(block: HttpClientConfig<CIOEngineConfig>.() -> Unit = {}) =
    HttpClient(CIO) {
      install(ContentNegotiation) {
        json(defaultJson())
      }

      block()

      // TODO: Look into this
//      install(ContentEncoding) {
//        deflate(1.0F)
//        gzip(0.9F)
//      }
    }

  fun wsHttpClient(block: HttpClientConfig<CIOEngineConfig>.() -> Unit = {}) =
    HttpClient(CIO) {
      install(WebSockets) {
        pingIntervalMillis = Duration.ofSeconds(15).toMillis()
        maxFrameSize = Long.MAX_VALUE
      }

      block()
    }

  internal fun String.stripQueryParams() = split("?").first()

  internal fun String.queryParams() = split("?").last()

  internal fun missingQueryParam(name: String): Nothing = error("Missing query parameter: $name")

  internal fun ApplicationCall.getQueryParam(name: String) = request.queryParameters[name]

  internal fun ApplicationCall.getHeader(name: String) = request.headers[name].orEmpty()
}
