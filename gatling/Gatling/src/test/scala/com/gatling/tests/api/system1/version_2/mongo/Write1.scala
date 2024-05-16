package com.gatling.tests.api.system1.version_2.mongo

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

  def randomString(length: Int): String = {
    val chars = ('A' to 'Z') ++ ('a' to 'z') ++ ('0' to '9')
    Seq.fill(length)(chars(Random.nextInt(chars.length))).mkString
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
  // Generate random double with precision
  def randomDouble(precision: Int): Double = BigDecimal(Random.nextDouble()).setScale(precision, BigDecimal.RoundingMode.HALF_UP).toDouble

  // Generate random team stats
  def generateTeamStats(teamId: Int, location: Char): String = {
    s"""{
      "teamstatId": $teamId,
      "location": "$location",
      "goals": ${Random.nextInt(5)},
      "xGoals": ${randomDouble(6)},
      "shots": ${Random.nextInt(20)},
      "shotsOnTarget": ${Random.nextInt(10)},
      "deep": ${Random.nextInt(15)},
      "ppda": ${randomDouble(4)},
      "fouls": ${Random.nextInt(10)},
      "corners": ${Random.nextInt(5)},
      "yellowCards": ${Random.nextInt(3)},
      "redCards": ${Random.nextInt(2)},
      "result": "${if (location == 'h') "W" else "L"}"
    }"""
  }

  // Generate random player appearance
  def generateAppearance(gameID: Int): String = {
    s"""{
      "appearanceId": ${Random.nextInt(1000)},
      "gameID": $gameID,
      "playerID": ${Random.nextInt(100)},
      "goals": ${Random.nextInt(3)},
      "ownGoals": ${Random.nextInt(1)},
      "shots": ${Random.nextInt(10)},
      "xGoals": ${randomDouble(2)},
      "xGoalsChain": ${randomDouble(2)},
      "xGoalsBuildup": ${randomDouble(2)},
      "assists": ${Random.nextInt(5)},
      "keyPasses": ${Random.nextInt(10)},
      "xAssists": ${randomDouble(2)},
      "position": "${Seq("GK", "DF", "MF", "FW")(Random.nextInt(4))}",
      "positionOrder": ${Random.nextInt(4) + 1},
      "yellowCard": ${Random.nextInt(2)},
      "redCard": ${Random.nextInt(1)},
      "time": ${Random.nextInt(90) + 30},
      "substituteIn": "none",
      "substituteOut": "none",
      "leagueId": 1,
      "playerName": "${randomString(10)}",
      "leagueName": "Premier League",
      "understatNotation": "EPL"
    }"""
  }

  // Generate random shots
  def generateShots(gameID: Int): String = {
    val situations = Seq("FromCorner", "Penalty", "OpenPlay", "SetPiece", "DirectFreekick")
    val lastActions = Seq("CrossNotClaimed", "BallRecovery", "PenaltyFaced", "Save", "None", "Interception", "Pass", "Smother", "CornerAwarded", "ChanceMissed")
    val shotTypes = Seq("Head", "LeftFoot", "OtherBodyPart", "RightFoot")
    val shotResults = Seq("OwnGoal", "MissedShots", "ShotOnPost", "Goal", "SavedShot")

    s"""{
      "shotId": ${Random.nextInt(1000)},
      "gameId": $gameID,
      "shooterId": ${Random.nextInt(50)},
      "assisterId": ${if (Random.nextBoolean()) Random.nextInt(50) else "null"},
      "minute": ${Random.nextInt(90)},
      "situation": "${situations(Random.nextInt(situations.length))}",
      "lastAction": "${lastActions(Random.nextInt(lastActions.length))}",
      "shotType": "${shotTypes(Random.nextInt(shotTypes.length))}",
      "shotResult": "${shotResults(Random.nextInt(shotResults.length))}",
      "xGoal": ${randomDouble(3)},
      "positionX": ${randomDouble(3)},
      "positionY": ${randomDouble(3)},
      "shooterName": "${randomString(10)}",
      "assisterName": ${if (Random.nextBoolean()) "\"" + randomString(10) + "\"" else "null"}
    }"""
  }

  // Main method to generate the full JSON body
  def generateUpdateBody(): String = {
    val gameId = Random.nextInt(100) + 1
    val appearances = (1 to 2).map(_ => generateAppearance(gameId)).mkString("[", ",", "]")
    val shots = (1 to 2).map(_ => generateShots(gameId)).mkString("[", ",", "]")

    s"""{
    "gameId": $gameId,
    "date": "${randomDate("2015-01-01", "2024-04-02")}",
    "leagueName": "Premier League",
    "season": ${Random.nextInt(5) + 2015},
    "homeGoals": ${Random.nextInt(5)},
    "awayGoals": ${Random.nextInt(5)},
    "homeProbability": ${randomDouble(4)},
    "awayProbability": ${randomDouble(4)},
    "drawProbability": ${randomDouble(4)},
    "homeGoalsHalfTime": ${Random.nextInt(3)},
    "awayGoalsHalfTime": ${Random.nextInt(3)},
    "homeTeam": {
      "teamID": 89,
      "name": "Manchester United",
      "teamStats": ${generateTeamStats(1, 'h')}
    },
    "awayTeam": {
      "teamID": 82,
      "name": "Tottenham",
      "teamStats": ${generateTeamStats(2, 'a')}
    },
    "appearances": $appearances,
    "shots": $shots
  }"""
  }

  val updateTeamStatScenario: ScenarioBuilder = scenario("Create Table")
    .repeat(500) {
      exec(session => {
        val updateBody = generateUpdateBody()
        Files.write(Paths.get("teamStatsJson.txt"), updateBody.getBytes(StandardCharsets.UTF_8))
        session.set("updateBody", updateBody)
      })
        .exec(http("Create Player")
          .post("/api/v2/game/mongo/write-1") // Assuming this is your update endpoint, with teamStatId as a path parameter
          .body(StringBody("${updateBody}")).asJson
          .check(status.is(201)) // Assuming 200 is the status code for successful update
        )
    }
  setUp(
    updateTeamStatScenario.inject(atOnceUsers(20)) // Execute the scenario for one user
  ).protocols(httpProtocol)
}
