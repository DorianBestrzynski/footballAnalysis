package com.gatling.tests.api.system2.version_1.postgres

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.io.Source
import scala.util.{Random, Using}


class Write1 extends Simulation {
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://localhost:8080") // Your API endpoint base URL
    .header("Content-Type", "application/json")

  def randomDateTime(startDate: String, endDate: String): String = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    val end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    val randomEpochSecond = start.toEpochSecond(java.time.ZoneOffset.UTC) + Random.nextLong((end.toEpochSecond(java.time.ZoneOffset.UTC) - start.toEpochSecond(java.time.ZoneOffset.UTC)))
    LocalDateTime.ofEpochSecond(randomEpochSecond, 0, java.time.ZoneOffset.UTC).format(formatter)
  }

  def generateFollowerList(numFollowers: Int): String = {
    (1 to numFollowers).map(_ => Random.nextInt(10000)).mkString("[", ", ", "]")
  }

  def generateFollowingList(numFollowing: Int): String = {
    (1 to numFollowing).map(_ => Random.nextInt(10000)).mkString("[", ", ", "]")
  }

  def generateRepoList(numRepos: Int): String = {
    (1 to numRepos).map { _ =>
      s"""
      {
        "repoId": ${Random.nextInt(10000)},
        "name": "Repo ${Random.alphanumeric.take(10).mkString}",
        "description": "Description ${Random.alphanumeric.take(20).mkString}",
        "language": "Language ${Random.alphanumeric.take(5).mkString}",
        "hasWiki": ${Random.nextBoolean()},
        "createdAt": "${randomDateTime("2015-01-01T00:00:00.000Z", "2024-04-02T23:59:59.999Z")}",
        "updatedAt": "${randomDateTime("2015-01-01T00:00:00.000Z", "2024-04-02T23:59:59.999Z")}",
        "pushedAt": "${randomDateTime("2015-01-01T00:00:00.000Z", "2024-04-02T23:59:59.999Z")}",
        "defaultBranch": "main",
        "stargazersCount": ${Random.nextInt(1000)},
        "openIssues": ${Random.nextInt(100)},
        "ownerId": ${Random.nextInt(10000)},
        "license": "License ${Random.alphanumeric.take(5).mkString}",
        "size": ${Random.nextInt(100000)},
        "fork": ${Random.nextBoolean()}
      }
      """
    }.mkString("[", ", ", "]")
  }

  def generateCommitList(numCommits: Int): String = {
    (1 to numCommits).map { _ =>
      s"""
      {
        "commitId": ${Random.nextInt(10000)},
        "message": "Commit message ${Random.alphanumeric.take(10).mkString}",
        "commitAt": "${randomDateTime("2015-01-01T00:00:00.000Z", "2024-04-02T23:59:59.999Z")}",
        "generateAt": "${randomDateTime("2015-01-01T00:00:00.000Z", "2024-04-02T23:59:59.999Z")}",
        "repoId": ${Random.nextInt(10000)},
        "authorId": ${Random.nextInt(10000)},
        "committerId": ${Random.nextInt(10000)},
        "repoName": "Repo ${Random.alphanumeric.take(10).mkString}",
        "repoDescription": "Description ${Random.alphanumeric.take(20).mkString}"
      }
      """
    }.mkString("[", ", ", "]")
  }

  def generateFullUserBody(numFollowers: Int, numFollowing: Int, numRepos: Int, numCommits: Int): String = {
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
      "isSuspicious": ${Random.nextBoolean()},
      "followerList": ${generateFollowerList(numFollowers)},
      "followingList": ${generateFollowingList(numFollowing)},
      "repoList": ${generateRepoList(numRepos)},
      "commitList": ${generateCommitList(numCommits)}
    }
    """
  }

  val createFullUserScenario: ScenarioBuilder = scenario("Create Full User in Postgres")
    .repeat(1) {
      exec(session => {
        val fullUserBody = generateFullUserBody(5, 5, 3, 10) // Generate 5 followers, 5 followings, 3 repositories, and 10 commits
        session.set("fullUserBody", fullUserBody)
      })
        .exec(http("Create Full User")
          .post("/api/v1/git/pg/write-1")
          .body(StringBody("${fullUserBody}")).asJson
          .check(status.is(201)) // Assuming 201 is the status code for successful creation
        )
    }

  setUp(
    createFullUserScenario.inject(atOnceUsers(10)) // Execute the scenario for 10 users at once
  ).protocols(httpProtocol)
}
