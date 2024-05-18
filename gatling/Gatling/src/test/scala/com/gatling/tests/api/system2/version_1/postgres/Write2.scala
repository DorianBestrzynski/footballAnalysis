package com.gatling.tests.api.system2.version_1.postgres

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import java.time.{Instant, LocalDateTime, ZoneOffset}
import java.time.format.DateTimeFormatter
import scala.io.Source
import scala.util.{Random, Using}

class Write2 extends Simulation {
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://localhost:8080") // Your API endpoint base URL
    .header("Content-Type", "application/json")

  def loadIdsFromFile(filePath: String): List[String] = {
    Using(Source.fromResource(filePath)) { source =>
      source.getLines().drop(1).toList // Pomijamy pierwszą linię (nagłówek)
    }.getOrElse {
      throw new RuntimeException(s"Failed to load data from $filePath")
    }
  }

  val userIds: List[String] = loadIdsFromFile("userIds.csv")
  val repoIds: List[String] = loadIdsFromFile("repoIds.csv")

  def randomDateTime(startDate: String, endDate: String): String = {
    val formatter = DateTimeFormatter.ISO_INSTANT
    val start = Instant.parse(startDate)
    val end = Instant.parse(endDate)
    val randomEpochSecond = start.getEpochSecond + Random.nextLong(end.getEpochSecond - start.getEpochSecond)
    Instant.ofEpochSecond(randomEpochSecond).atOffset(ZoneOffset.UTC).format(formatter)
  }

  def generateCommitBody(): String = {
    val authorId = Random.shuffle(userIds).head
    val commiterId = Random.shuffle(userIds).head
    val repoId = Random.shuffle(repoIds).head

    s"""
        {
          "message": "Commit message ${Random.alphanumeric.take(10).mkString}",
          "commitAt": "${randomDateTime("2015-01-01T00:00:00.000Z", "2024-04-02T23:59:59.999Z")}",
          "generateAt": "${randomDateTime("2015-01-01T00:00:00.000Z", "2024-04-02T23:59:59.999Z")}",
          "repoId": $repoId,
          "authorId": $authorId,
          "committerId": $commiterId,
          "repoName": "Repo ${Random.alphanumeric.take(10).mkString}",
          "repoDescription": "Repository description ${Random.alphanumeric.take(20).mkString}"
        }
    """
  }

  def generateCommitListBody(numCommits: Int): String = {
    val commits = (1 to numCommits).map(_ => generateCommitBody()).mkString(", ")
    s"""
      [
        $commits
      ]
    """
  }

  val createCommitScenario: ScenarioBuilder = scenario("Create Commits in Postgres")
    .repeat(10) {
      exec(session => {
        val commitListBody = generateCommitListBody(10000) // Generate 10 commit objects
        session.set("commitListBody", commitListBody)
      })
        .exec(http("Create Commits")
          .post("/api/v1/git/pg/write-2")
          .body(StringBody("${commitListBody}")).asJson
          .check(status.is(201)) // Assuming 201 is the status code for successful creation
        )
    }

  setUp(
    createCommitScenario.inject(atOnceUsers(1)) // Execute the scenario for 10 users at once
  ).protocols(httpProtocol)
}


