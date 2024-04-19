package com.gatling.tests.api.version_1.others.postgres

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class ShotsSimulation extends Simulation {
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://localhost:8080")

  // Warm-up scenario
  val warmUpScenario: ScenarioBuilder = scenario("Warm-up Phase")
    .repeat(2) {
      exec(
        http("Get shots")
          .get("/api/v1/player/pg/shots")
          .check(status.is(200))
      )
    }

  val testScenario: ScenarioBuilder = scenario("Test GET shots")
    .repeat(5) {
      exec(
        http("Get shots")
          .get("/api/v1/player/pg/shots")
          .check(status.is(200))
      )
    }

  setUp(
    warmUpScenario.inject(atOnceUsers(1)),
    testScenario.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
    .assertions(
      global.responseTime.mean.lt(5000),
      global.successfulRequests.percent.gt(95)
    )
}
