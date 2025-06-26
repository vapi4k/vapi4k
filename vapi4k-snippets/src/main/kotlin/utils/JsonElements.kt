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

package utils

import com.github.pambrose.common.json.get
import com.github.pambrose.common.json.keys
import com.github.pambrose.common.json.stringValue
import com.github.pambrose.common.json.toJsonElement
import com.github.pambrose.common.json.toJsonString
import kotlinx.serialization.json.JsonElement

object JsonElements {
  @JvmStatic
  fun main(args: Array<String>) {
    jsonElementExample()
  }

  fun jsonElementExample() {
    val json = """
      {
        "person": {
          "first": "Bill",
          "last": "Lambert",
          "address": {
            "street": "123 Main",
            "city": "Tusla"
          }
        }
      }
    """

    // Convert the json string to a JsonElement
    val je: JsonElement = json.toJsonElement()

    println(je.keys) // [person]

    println(je["person"].keys) // [first, last, address]

    println(je["person.address"].keys) // [street, city]

    // Get the value of the "person.first" key using the stringValue extension property
    println(je["person.first"].stringValue) // Bill

    // Get the value of the "person.last" key using the stringValue extension function
    println(je.stringValue("person.last")) // Lambert

    // Get the value of the "person.address.street" key
    println(je.stringValue("person.address.street")) // 123 Main

    // Get the value of the "person.address.street" key using the get function and the vararg keys parameter
    println(je["person", "address", "city"].stringValue) // Tulsa

    println(je.toJsonString())
    /*
      Outputs:
      {
        "person": {
          "first": "Bill",
          "last": "Lambert",
          "address": {
            "street": "123 Main",
            "city": "Tusla"
          }
        }
      }
     */
  }
}
