package com.gatling.tests.api.system2.version_1.mongo

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import java.time.LocalDateTime
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
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    val end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    val randomEpochSecond = start.toEpochSecond(java.time.ZoneOffset.UTC) + Random.nextLong((end.toEpochSecond(java.time.ZoneOffset.UTC) - start.toEpochSecond(java.time.ZoneOffset.UTC)))
    LocalDateTime.ofEpochSecond(randomEpochSecond, 0, java.time.ZoneOffset.UTC).format(formatter)
  }

  def generateCommitBody(): String = {
    val authorId = Random.shuffle(userIds).head
    val commiterId = Random.shuffle(userIds).head
    val repoId = Random.shuffle(repoIds).head
    s"""
        {
          "commitId": ${Random.nextInt(10000)},
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

  val createCommitScenario: ScenarioBuilder = scenario("Create Commits in Mongo")
    .repeat(1) {
      exec(session => {
        val commitListBody = generateCommitListBody(10) // Generate 10 commit objects
        session.set("commitListBody", commitListBody)
      })
        .exec(http("Create Commits")
          .post("/api/v1/git/mongo/write-2")
          .body(StringBody("${commitListBody}")).asJson
          .check(status.is(201)) // Assuming 201 is the status code for successful creation
        )
    }

  setUp(
    createCommitScenario.inject(atOnceUsers(10)) // Execute the scenario for 10 users at once
  ).protocols(httpProtocol)
}



