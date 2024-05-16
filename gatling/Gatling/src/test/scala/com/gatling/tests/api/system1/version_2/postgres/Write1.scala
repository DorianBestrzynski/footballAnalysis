package com.gatling.tests.api.system1.version_2.postgres

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


class Write1 extends Simulation {
  def loadIdsFromFile(filePath: String): List[String] = {
    Using(Source.fromResource(filePath)) { source =>
      source.getLines().drop(1).toList // Pomijamy pierwszą linię (nagłówek)
    }.getOrElse {
      throw new RuntimeException(s"Failed to load data from $filePath")
    }
  }
  val playerIds: List[String] = loadIdsFromFile("playerIds.csv")

  def randomString(length: Int): String = {
    val chars = ('A' to 'Z') ++ ('a' to 'z') ++ ('0' to '9')
    Seq.fill(length)(chars(Random.nextInt(chars.length))).mkString
  }

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
  // Generate random double with precision
  def randomDouble(precision: Int): Double = BigDecimal(Random.nextDouble()).setScale(precision, BigDecimal.RoundingMode.HALF_UP).toDouble

  def generateLeague(): String = {
    s"""{
    "name": "Premier League",
    "understatNotation": "EPL"
  }"""
  }

  def generatePlayer(): String = {
    s"""{
    "name": "Harry Kane"
  }"""
  }

  def generateTeam(name: String): String = {
    s"""{
    "name": "$name"
  }"""
  }

  def generateGame(): String = {
    s"""{
    "gameId": 101,
    "date": "${randomDate("2015-01-01", "2024-04-02")}",
    "leagueId": 1,
    "leagueName": "Premier League",
    "season": 2024,
    "homeTeamId": 1,
    "awayTeamId": 2,
    "homeGoals": ${Random.nextInt(5)},
    "awayGoals": ${Random.nextInt(5)},
    "homeProbability": ${randomDouble(2)},
    "awayProbability": ${randomDouble(2)},
    "drawProbability": ${randomDouble(2)},
    "homeGoalsHalfTime": ${Random.nextInt(3)},
    "awayGoalsHalfTime": ${Random.nextInt(3)}
  }"""
  }

  def generatePlayerAppearance(): String = {
    s"""{
    "goals": ${Random.nextInt(4)},
    "ownGoals": ${Random.nextInt(2)},
    "shots": ${Random.nextInt(10)},
    "xGoals": ${randomDouble(2)},
    "xGoalsChain": ${randomDouble(2)},
    "xGoalsBuildup": ${randomDouble(2)},
    "assists": ${Random.nextInt(6)},
    "keyPasses": ${Random.nextInt(10)},
    "xAssists": ${randomDouble(2)},
    "position": "Forward",
    "positionOrder": ${Random.nextInt(11)},
    "yellowCard": ${Random.nextInt(3)},
    "redCard": ${Random.nextInt(2)},
    "time": 90,
    "substituteIn": "None",
    "substituteOut": "None"
  }"""
  }

  def generateTeamStat(teamId: Int, location: Char): String = {
    s"""{
    "gameID": 101,
    "teamID": $teamId,
    "season": 2024,
    "date": "${randomDate("2015-01-01", "2024-04-02")}",
    "location": "$location",
    "goals": ${Random.nextInt(5)},
    "expectedGoals": ${randomDouble(2)},
    "shots": ${Random.nextInt(20)},
    "shotsOnTarget": ${Random.nextInt(10)},
    "deep": ${Random.nextInt(10)},
    "ppda": ${randomDouble(1)},
    "fouls": ${Random.nextInt(10)},
    "corners": ${Random.nextInt(5)},
    "yellowCards": ${Random.nextInt(4)},
    "redCards": ${Random.nextInt(2)},
    "result": "${if (location == 'H') "W" else "L"}"
  }"""
  }

  def generateShot(): String = {
    val playerId = Random.shuffle(playerIds).head
    val assisterId = Random.shuffle(playerIds).head

    s"""{
    "gameId": 101,
    "shooterId": $playerId,
    "assisterId": $assisterId,
    "minute": ${Random.nextInt(90)},
    "situation": "OpenPlay",
    "lastAction": "Pass",
    "shotType": "RightFoot",
    "shotResult": "Goal",
    "xGoal": ${randomDouble(3)},
    "positionX": ${randomDouble(3)},
    "positionY": ${randomDouble(3)}
  }"""
  }

  // Main method to assemble the full JSON body
  def generateUpdateBody(): String = {
    s"""{
    "league": ${generateLeague()},
    "player": ${generatePlayer()},
    "team1": ${generateTeam("Manchester United")},
    "team2": ${generateTeam("Chelsea")},
    "game": ${generateGame()},
    "playerAppearance": ${generatePlayerAppearance()},
    "teamStat1": ${generateTeamStat(1, 'H')},
    "teamStat2": ${generateTeamStat(2, 'A')},
    "shot": ${generateShot()}
  }"""
  }

  val updateTeamStatScenario: ScenarioBuilder = scenario("Create Player")
    .repeat(500) {
      exec(session => {
        val updateBody = generateUpdateBody()
        Files.write(Paths.get("teamStatsJson.txt"), updateBody.getBytes(StandardCharsets.UTF_8))
        session.set("updateBody", updateBody)
      })
        .exec(http("Create Player")
          .post("/api/v2/game/pg/write-1") // Assuming this is your update endpoint, with teamStatId as a path parameter
          .body(StringBody("${updateBody}")).asJson
          .check(status.is(201)) // Assuming 200 is the status code for successful update
        )
    }
  setUp(
    updateTeamStatScenario.inject(atOnceUsers(20)) // Execute the scenario for one user
  ).protocols(httpProtocol)
}
