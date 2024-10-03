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

package simpleDemo

import com.vapi4k.api.json.stringValue
import com.vapi4k.api.json.toJsonElement
import com.vapi4k.api.json.toJsonElementList
import com.vapi4k.api.tools.ToolCall
import com.vapi4k.utils.HttpUtils.jsonHttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import kotlinx.coroutines.runBlocking

class WeatherLookupService {
  @ToolCall("Look up the real weather for a city")
  fun getRealWeatherByCity(
    city: String,
    state: String,
  ): String {
    val currentTemperature =
      runBlocking {
        val response =
          jsonHttpClient().use { client ->
            client.get {
              url {
                protocol = URLProtocol.HTTP
                host = "api.openweathermap.org"
                encodedPath = "/geo/1.0/direct"
                parameters.apply {
                  append("q", "$city,$state,US")
                  append("limit", "5")
                  append("appid", getLocationAPIKey())
                }
              }
            }
          }
        val je =
          response
            .bodyAsText()
            .toJsonElement()
            .toJsonElementList()
            .first()

        jsonHttpClient().use { client ->
          client.get {
            url {
              protocol = URLProtocol.HTTPS
              host = "api.open-meteo.com"
              encodedPath = "/v1/forecast"
              parameters.apply {
                append("latitude", je.stringValue("lat"))
                append("longitude", je.stringValue("lon"))
                append("timezone", "PST")
                append("temperature_unit", "fahrenheit")
                append("current", "temperature_1000hPa")
              }
            }
          }
        }.bodyAsText()
          .toJsonElement()
          .stringValue("current.temperature_1000hPa")
      }
    return "The real weather in $city, $state is $currentTemperature degrees Fahrenheit"
  }

  private fun getLocationAPIKey() = System.getenv("LOCATION_LOOKUP_API_KEY") ?: "LOCATION_LOOKUP_API_KEY not found"
}
