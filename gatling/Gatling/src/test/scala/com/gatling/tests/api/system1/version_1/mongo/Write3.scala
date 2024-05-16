package com.gatling.tests.api.system1.version_1.mongo

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.io.Source
import scala.util.{Random, Using}

class Write3 extends Simulation {
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://localhost:8080") // Your API endpoint base URL
    .header("Content-Type", "application/json")

  def randomDate(startDate: String, endDate: String): String = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val start = LocalDate.parse(startDate, formatter)
    val end = LocalDate.parse(endDate, formatter)
    val randomDaysToAdd = Random.nextInt(end.toEpochDay.toInt - start.toEpochDay.toInt)
    start.plusDays(randomDaysToAdd).atTime(15, 0).toString
  }

  def generateUpdateBody(): String = {
    val leagueName = Seq("Premier League", "Serie A", "Bundesliga", "La Liga", "Ligue 1")(Random.nextInt(5))

    s"""
        {
          "gameId": ${Random.nextInt(55555)},
          "date": "${randomDate("2015-01-01", "2024-04-02")}",
          "leagueName": "$leagueName",
          "season": 2015,
          "homeGoals": ${Random.nextInt(16)},
          "awayGoals": ${Random.nextInt(16)},
          "homeProbability": ${Random.nextFloat()},
          "awayProbability": ${Random.nextFloat()},
          "drawProbability": ${Random.nextFloat()},
          "homeGoalsHalfTime": ${Random.nextInt(8)},
          "awayGoalsHalfTime": ${Random.nextInt(8)}
        }
        """
  }

  val updateTeamStatScenario: ScenarioBuilder = scenario("Create Game")
    .repeat(100) {
      exec(session => {
        val updateBody = generateUpdateBody()
        session.set("updateBody", updateBody)
      })
        .exec(http("Create Game")
          .post("/api/v1/game/mongo/write-3") // Assuming this is your update endpoint, with teamStatId as a path parameter
          .body(StringBody("${updateBody}")).asJson
          .check(status.is(201)) // Assuming 200 is the status code for successful update
        )
    }
  setUp(
    updateTeamStatScenario.inject(atOnceUsers(100)) // Execute the scenario for one user
  ).protocols(httpProtocol)
}


