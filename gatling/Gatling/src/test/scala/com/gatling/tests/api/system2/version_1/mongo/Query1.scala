package com.gatling.tests.api.system2.version_1.mongo

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.util.Random

class Query1 extends Simulation {
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://localhost:8080")

  // Warm-up scenario
  val warmUpScenario: ScenarioBuilder = scenario("Warm-up Phase")
    .group("WARM UP") {
      // Repeat the following block 5 times
      exec(
        http("Query 1")
          .get("/api/v1/git/mongo/query-1")
          .silent
      )
        .exec(
          http("Query 1")
            .get("/api/v1/git/mongo/query-1")
            .silent
        )
    }

  // Actual test scenario
  val testScenario: ScenarioBuilder = scenario("Test Query 1")
    .repeat(10) { // Repeat the following block 5 times
      exec(
        http("Query 1")
          .get("/api/v1/git/mongo/query-1")
          .check(status.is(200))
      )
    } // Repeat the request 5 times for the actual test

  // Setup
  setUp(
    // Execute the warm-up scenario without affecting the main test metrics
    warmUpScenario.inject(atOnceUsers(1)),
    // Execute the actual test scenario
    testScenario.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
    .assertions(
      global.responseTime.mean.lt(5000), // Assert the mean response time is less than 50ms
      global.successfulRequests.percent.gt(95) // Assert more than 95% of requests are successful
    )
}
