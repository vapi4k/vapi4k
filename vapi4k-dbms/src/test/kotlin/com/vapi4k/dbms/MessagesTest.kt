package com.vapi4k.dbms

import com.vapi4k.api.Messages
import com.vapi4k.api.vapi4k.ServerRequestType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.Duration.Companion.milliseconds

class MessagesTest : StringSpec() {
  init {
    beforeSpec { TestDatabase.database }

    beforeTest {
      transaction { MessagesTable.deleteAll() }
    }

    "insertRequest stores correct messageType and requestType" {
      val json: JsonElement = Json.parseToJsonElement(
        """{"message": {"type": "assistant-request"}}""",
      )

      Messages.insertRequest(json)

      transaction {
        val row = MessagesTable.selectAll()
          .where { MessagesTable.messageType eq "REQUEST" }
          .first()

        row[MessagesTable.messageType] shouldBe "REQUEST"
        row[MessagesTable.requestType] shouldBe "assistant-request"
        Json.parseToJsonElement(row[MessagesTable.messageJson]) shouldBe json
      }
    }

    "insertResponse stores correct messageType and elapsed time" {
      val json: JsonElement = Json.parseToJsonElement(
        """{"result": "ok"}""",
      )
      val elapsed = 150.milliseconds

      Messages.insertResponse(ServerRequestType.TOOL_CALL, json, elapsed)

      transaction {
        val row = MessagesTable.selectAll()
          .where { MessagesTable.messageType eq "RESPONSE" }
          .first()

        row[MessagesTable.messageType] shouldBe "RESPONSE"
        row[MessagesTable.requestType] shouldBe "tool-calls"
        Json.parseToJsonElement(row[MessagesTable.messageJson]) shouldBe json
      }
    }

    "insertRequest and insertResponse coexist in the same table" {
      val requestJson: JsonElement = Json.parseToJsonElement(
        """{"message": {"type": "end-of-call-report"}}""",
      )
      val responseJson: JsonElement = Json.parseToJsonElement(
        """{"summary": "call ended"}""",
      )

      Messages.insertRequest(requestJson)
      Messages.insertResponse(ServerRequestType.END_OF_CALL_REPORT, responseJson, 200.milliseconds)

      transaction {
        val allRows = MessagesTable.selectAll().toList()
        val messageTypes = allRows.map { it[MessagesTable.messageType] }.toSet()
        messageTypes shouldBe setOf("REQUEST", "RESPONSE")
      }
    }
  }
}
