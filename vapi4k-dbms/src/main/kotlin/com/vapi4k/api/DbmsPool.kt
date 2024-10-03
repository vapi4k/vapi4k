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

package com.vapi4k.api

import com.vapi4k.dbms.DbmsEnvVars
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import kotlin.time.Duration.Companion.minutes

object DbmsPool {
  fun connectToDbms() = database

  private val database: Database by lazy {
    Database.connect(
      HikariDataSource(
        HikariConfig().apply {
          driverClassName = DbmsEnvVars.DBMS_DRIVER_CLASSNAME.value
          jdbcUrl = DbmsEnvVars.DBMS_URL.value
          username = DbmsEnvVars.DBMS_USERNAME.value
          password = DbmsEnvVars.DBMS_PASSWORD.value
          maximumPoolSize = DbmsEnvVars.DBMS_MAX_POOL_SIZE.toInt()
          transactionIsolation = "TRANSACTION_REPEATABLE_READ"
          maxLifetime = DbmsEnvVars.DBMS_MAX_LIFETIME_MINS.toInt().minutes.inWholeMilliseconds
          validate()
        },
      ),
    )
  }
}
