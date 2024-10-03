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

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking
import java.lang.reflect.Field
import kotlin.reflect.full.declaredMemberProperties

class AdminConsoleAppender : ConsoleAppender<ILoggingEvent>() {
  private val consoleFlow: MutableSharedFlow<String>

  init {
    consoleFlow = SharedDataLoader.accessSharedFlow()
  }

  override fun append(eventObject: ILoggingEvent) {
    // super.append(eventObject)
    runBlocking {
      val msg = encoder.encode(eventObject).decodeToString()
      consoleFlow.emit(msg)
    }
  }
}

class SharedData {
  companion object {
    val consoleFlow = MutableSharedFlow<String>(replay = 1000)
  }
}

class SharedDataLoader {
  companion object {
    fun accessSharedFlow(): MutableSharedFlow<String> =
      runCatching {
        val contextClassLoader = Thread.currentThread().contextClassLoader
        val sharedDataClass = contextClassLoader.loadClass("com.vapi4k.utils.SharedData")
        val companionField: Field = sharedDataClass.getDeclaredField("Companion")
        val companion = companionField[null]
        val property =
          companion::class.declaredMemberProperties
            .find { it.name == "consoleFlow" } ?: error("Missing consoleFlow property")
        property.getter.call(companion).let {
          if (it is MutableSharedFlow<*>) {
            @Suppress("UNCHECKED_CAST")
            it as MutableSharedFlow<String>
          } else {
            error("Invalid type for consoleFlow property")
          }
        }
      }.getOrElse {
        error(it)
      }
  }
}
