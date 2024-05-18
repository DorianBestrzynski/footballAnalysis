package com.gatling.tests.api.system2.version_1.mongo

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import java.time.{Instant, LocalDate, LocalDateTime, ZoneOffset}
import java.time.format.DateTimeFormatter
import scala.io.Source
import scala.util.{Random, Using}

class Write3 extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://localhost:8080") // Your API endpoint base URL
    .header("Content-Type", "application/json")

  def randomDateTime(startDate: String, endDate: String): String = {
    val formatter = DateTimeFormatter.ISO_INSTANT
    val start = Instant.parse(startDate)
    val end = Instant.parse(endDate)
    val randomEpochSecond = start.getEpochSecond + Random.nextLong(end.getEpochSecond - start.getEpochSecond)
    Instant.ofEpochSecond(randomEpochSecond).atOffset(ZoneOffset.UTC).format(formatter)
  }

  def generateUserBody(): String = {
    s"""
        {
          "userId": ${Random.nextInt(10000)},
          "name": "User ${Random.alphanumeric.take(10).mkString}",
          "type": "User",
          "bio": "Bio for user ${Random.alphanumeric.take(20).mkString}",
          "email": "user${Random.nextInt(10000)}@example.com",
          "login": "login${Random.alphanumeric.take(10).mkString}",
          "company": "Company ${Random.alphanumeric.take(10).mkString}",
          "blog": "https://blog${Random.alphanumeric.take(10).mkString}.com",
          "location": "Location ${Random.alphanumeric.take(10).mkString}",
          "createdAt": "${randomDateTime("2015-01-01T00:00:00.000Z", "2024-04-02T23:59:59.999Z")}",
          "updatedAt": "${randomDateTime("2015-01-01T00:00:00.000Z", "2024-04-02T23:59:59.999Z")}",
          "hirable": ${Random.nextBoolean()},
          "isSuspicious": ${Random.nextBoolean()}
        }
    """
  }

  val createUserScenario: ScenarioBuilder = scenario("Create Users in MongoDB")
    .repeat(1000) {
      exec(session => {
        val userBody = generateUserBody()
        session.set("userBody", userBody)
      })
        .exec(http("Create User")
          .post("/api/v1/git/mongo/write-3")
          .body(StringBody("${userBody}")).asJson
          .check(status.is(201)) // Assuming 201 is the status code for successful creation
        )
    }

  setUp(
    createUserScenario.inject(atOnceUsers(1)) // Execute the scenario for 10 users at once
  ).protocols(httpProtocol)
}



