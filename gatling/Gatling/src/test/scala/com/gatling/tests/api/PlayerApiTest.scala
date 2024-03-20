package com.gatling.tests.api

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder


class PlayerApiTest extends Simulation {

  //protocol
  val httpProtocol: HttpProtocolBuilder = http.baseUrl("http://localhost:8080")

  //scenario
  val scn: ScenarioBuilder = scenario("GET Player Request")
    .exec(
      http("Get all players")
        .get("/api/v1/player")
        .queryParam("playerId", 560)
        .check(status.is(200))
    )

  //setup

  setUp(
    scn.inject(atOnceUsers(1))
      .protocols(httpProtocol)
  )
    .assertions(
      global.responseTime.max.lt(50),
      global.successfulRequests.percent.gt(95)
    )
}
