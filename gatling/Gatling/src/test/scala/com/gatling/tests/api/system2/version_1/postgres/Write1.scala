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
  val playerIds: List[String] = loadIdsFromFile("playerIds.csv")

  def randomDate(startDate: String, endDate: String): String = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val start = LocalDate.parse(startDate, formatter)
    val end = LocalDate.parse(endDate, formatter)
    val randomDaysToAdd = Random.nextInt(end.toEpochDay.toInt - start.toEpochDay.toInt)
    start.plusDays(randomDaysToAdd).atTime(15, 0).toString
  }

  def generateUpdateBody(): String = {
    val name = randomString(10)

    val appearances = (1 to 2).map(_ => generateAppearance()).mkString("[", ",", "]")
    val shots = (1 to 2).map(_ => generateShots()).mkString("[", ",", "]")

    s"""
    {
      "name": "$name",
      "appearances": $appearances,
      "shots": $shots
      }
  """
  }

  def generateAppearance(): String = {
    val gameID = Random.shuffle(gameIds).head
    val leagueID = Random.nextInt(5) + 1 // Example randomization, adapt range as needed
    val time = Random.nextInt(120) + 1 // Assuming a range of 1-120 minutes for a football match
    val position = Seq("GK", "DF", "MF", "FW")(Random.nextInt(4)) // Random position
    val positionOrder = Random.nextInt(4) + 1 // Assuming position order ranges from 1 to 4

    // Generate a random body for the update.
    s"""
    {
      "gameID": $gameID,
      "leagueID": $leagueID,
      "time": $time,
      "goals": ${Random.nextInt(5)},
      "ownGoals": ${Random.nextInt(3)},
      "xGoals": ${Random.nextFloat()},
      "assists": ${Random.nextInt(5)},
      "position": "$position",
      "positionOrder": $positionOrder,
      "xAssists": ${Random.nextFloat()},
      "shots": ${Random.nextInt(10)},
      "keyPasses": ${Random.nextInt(10)},
      "yellowCards": ${Random.nextInt(3)},
      "redCards": ${Random.nextInt(2)},
      "xGoalsChain": ${Random.nextFloat()},
      "xGoalsBuildup": ${Random.nextFloat()}
    }
  """.trim
  }

  def generateShots(): String = {
    val gameID = Random.shuffle(gameIds).head
    val shooterId = Random.shuffle(playerIds).head
    val assisterId = Random.shuffle(playerIds).head
    val situation = Seq("FromCorner", "Penulllty", "OpenPlay", "SetPiece", "DirectFreekick")(Random.nextInt(5))
    val lastAction = Seq("CrossNotClaimed", "BallRecovery", "PenullltyFaced", "Save", "None", "Interception", "Pass", "Smother", "CornerAwarded", "ChanceMissed")(Random.nextInt(10))
    val shotType = Seq("Head", "LeftFoot", "OtherBodyPart", "RightFoot")(Random.nextInt(4))
    val shotResult = Seq("OwnGoal", "MissedShots", "ShotOnPost", "Goal", "SavedShot")(Random.nextInt(5))


    // Generate a random body for the update.
    s"""
    {
      "gameID": $gameID,
      "shooterID": $shooterId,
      "assisterID": $assisterId,
      "minute": ${Random.nextInt(90)},
      "situation": "$situation",
      "lastAction": "$lastAction",
      "shotType": "$shotType",
      "shotResult": "$shotResult",
      "xGoals": ${Random.nextFloat()},
      "positionX": ${Random.nextFloat()},
      "positionY": ${Random.nextFloat()}
    }
  """.trim
  }

  val updateTeamStatScenario: ScenarioBuilder = scenario("Create Player")
    .repeat(10) {
      exec(session => {
        val updateBody = generateUpdateBody()
        session.set("updateBody", updateBody)
      })
        .exec(http("Create Player")
          .post("/api/v1/game/pg/write-1") // Assuming this is your update endpoint, with teamStatId as a path parameter
          .body(StringBody("${updateBody}")).asJson
          .check(status.is(201)) // Assuming 200 is the status code for successful update
        )
    }
  setUp(
    updateTeamStatScenario.inject(atOnceUsers(1)) // Execute the scenario for one user
  ).protocols(httpProtocol)
}
