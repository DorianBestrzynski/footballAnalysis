package com.gatling.tests.api.system1.version_1.postgres

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.util.Random

class Query1 extends Simulation {
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://localhost:8080")

  val warmUpScenario: ScenarioBuilder = scenario("Warm-up Phase")
    .group("WARM UP") {
      exec(
        http("Query 1")
          .get("/api/v1/game/pg/query-1")
          .queryParam("awayGoals", Random.nextInt(6))
          .silent
      )
        .exec(
          http("Query 1")
            .get("/api/v1/game/pg/query-1")
            .queryParam("awayGoals", Random.nextInt(6))
            .silent
        )
    }

  val testScenario: ScenarioBuilder = scenario("Test Query 1")
    .repeat(100) {
      exec(
        http("Query 1")
          .get("/api/v1/game/pg/query-1")
          .queryParam("awayGoals", Random.nextInt(6))
          .check(status.is(200))
      )
    }

  setUp(
    warmUpScenario.inject(atOnceUsers(1)),
    testScenario.inject(atOnceUsers(50))
  ).protocols(httpProtocol)
    .assertions(
      global.responseTime.mean.lt(5000),
      global.successfulRequests.percent.gt(95)
    )
}
