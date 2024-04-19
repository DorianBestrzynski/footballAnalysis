package com.gatling.tests.api.version_1.others.postgres

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.io.Source
import scala.util.{Random, Using}

class TeamStatBatchCreateSimulation extends Simulation {

  def loadIdsFromFile(filePath: String): List[String] = {
    Using(Source.fromFile(filePath)) { source =>
      source.getLines().drop(1).toList // Pomijamy pierwszą linię (nagłówek)
    }.getOrElse {
      throw new RuntimeException(s"Failed to load data from $filePath")
    }
  }

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://localhost:8080") // Your API endpoint base URL
    .header("Content-Type", "application/json")

  val gameIds: List[String] = loadIdsFromFile("gameIds.csv")
  val teamIds: List[String] = loadIdsFromFile("teamIds.csv")

  def randomDate(startDate: String, endDate: String): String = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val start = LocalDate.parse(startDate, formatter)
    val end = LocalDate.parse(endDate, formatter)
    val randomDaysToAdd = Random.nextInt(end.toEpochDay.toInt - start.toEpochDay.toInt)
    start.plusDays(randomDaysToAdd).atTime(15, 0).toString
  }

  val insertTeamStatBatchScenario: ScenarioBuilder = scenario("Insert TeamStat Batch Data")
    .exec(session => {
      val teamStats = (1 to 10000).map { _ =>
        val gameId = Random.shuffle(gameIds).head
        val teamId = Random.shuffle(teamIds).head
        val shots = Random.nextInt(201)
        s"""
        {
          "gameID": $gameId,
          "teamID": $teamId,
          "season": ${2015 + Random.nextInt(10)},
          "date": "${randomDate("2015-01-01", "2024-04-02")}",
          "location": "${if (Random.nextBoolean()) "H" else "A"}",
          "goals": ${Random.nextInt(16)},
          "expectedGoals": ${Random.nextFloat() * 50.0},
          "shots": $shots,
          "shotsOnTarget": ${Random.nextInt(shots + 1)},
          "deep": ${Random.nextInt(201)},
          "ppda": ${Random.nextFloat() * 100},
          "fouls": ${Random.nextInt(101)},
          "corners": ${Random.nextInt(101)},
          "yellowCards": ${Random.nextInt(21)},
          "redCards": ${Random.nextInt(11)},
          "result": "${Seq("W", "D", "L")(Random.nextInt(3))}"
        }
        """
      }.mkString("[", ",", "]")

      session.set("teamStatsJson", teamStats)
    })
    .exec(session => {
      val teamStatsJson = session("teamStatsJson").as[String]
      Files.write(Paths.get("teamStatsJson.txt"), teamStatsJson.getBytes(StandardCharsets.UTF_8))
      session
    })
    .exec(http("Insert TeamStat Batch")
      .post("/api/v1/team/pg/stats") // Assuming this is your batch insert endpoint
      .body(StringBody("${teamStatsJson}")).asJson
      .check(status.is(201)) // Assuming 201 is the status code for successful batch insert
    )

  setUp(
    insertTeamStatBatchScenario.inject(atOnceUsers(1)) // Execute the scenario for one user
  ).protocols(httpProtocol)
}


