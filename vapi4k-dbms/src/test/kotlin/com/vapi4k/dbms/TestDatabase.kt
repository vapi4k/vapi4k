package com.vapi4k.dbms

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.jdbc.Database
import org.testcontainers.containers.PostgreSQLContainer

object TestDatabase {
  private val container: PostgreSQLContainer<*> by lazy {
    PostgreSQLContainer("postgres:17-alpine")
      .also { it.start() }
  }

  val database: Database by lazy {
    Flyway.configure()
      .dataSource(container.jdbcUrl, container.username, container.password)
      .locations("classpath:db/migrations")
      .load()
      .migrate()

    val ds = HikariDataSource(
      HikariConfig().apply {
        jdbcUrl = container.jdbcUrl.replace("jdbc:postgresql://", "jdbc:pgsql://")
        username = container.username
        password = container.password
        driverClassName = "com.impossibl.postgres.jdbc.PGDriver"
        maximumPoolSize = 2
      },
    )

    Database.connect(ds)
  }
}
