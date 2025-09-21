package com.valven.ecommerce

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class EcommerceLoadTest extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .userAgentHeader("Gatling Performance Test")

  val userCount = System.getProperty("userCount", "50").toInt
  val rampUpTime = System.getProperty("rampUpTime", "60").toInt
  val testDuration = System.getProperty("testDuration", "300").toInt

  // Scenario 1: User Registration and Login
  val registrationScenario = scenario("User Registration and Login")
    .exec(
      http("User Registration")
        .post("/api/auth/signup")
        .body(StringBody("""{
          "name": "Test User ${__Random(1,10000)}",
          "email": "testuser${__Random(1,10000)}@example.com",
          "password": "password123"
        }"""))
        .check(status.is(201))
        .check(jsonPath("$.data.token").saveAs("authToken"))
    )
    .pause(1, 3)
    .exec(
      http("User Login")
        .post("/api/auth/signin")
        .body(StringBody("""{
          "email": "testuser${__Random(1,10000)}@example.com",
          "password": "password123"
        }"""))
        .check(status.is(200))
        .check(jsonPath("$.data.token").saveAs("authToken"))
    )

  // Scenario 2: Browse Products
  val browseProductsScenario = scenario("Browse Products")
    .exec(
      http("Get All Products")
        .get("/api/products")
        .check(status.is(200))
        .check(jsonPath("$.data").exists)
    )
    .pause(2, 5)
    .exec(
      http("Search Products")
        .get("/api/products?q=test")
        .check(status.is(200))
    )
    .pause(1, 3)
    .exec(
      http("Get Product by Category")
        .get("/api/products?category=Electronics")
        .check(status.is(200))
    )

  // Scenario 3: Shopping Cart Operations
  val shoppingCartScenario = scenario("Shopping Cart Operations")
    .exec(
      http("Get Cart")
        .get("/api/carts")
        .header("Authorization", "Bearer ${authToken}")
        .check(status.is(200))
    )
    .pause(1, 2)
    .exec(
      http("Add Item to Cart")
        .post("/api/carts/items")
        .header("Authorization", "Bearer ${authToken}")
        .body(StringBody("""{
          "productId": ${__Random(1,10)},
          "productName": "Test Product",
          "price": 99.99,
          "quantity": ${__Random(1,5)}
        }"""))
        .check(status.is(200))
    )
    .pause(2, 4)
    .exec(
      http("Remove Item from Cart")
        .delete("/api/carts/items/${__Random(1,10)}")
        .header("Authorization", "Bearer ${authToken}")
        .check(status.is(200))
    )

  // Scenario 4: Order Processing
  val orderProcessingScenario = scenario("Order Processing")
    .exec(
      http("Create Order")
        .post("/api/orders")
        .header("Authorization", "Bearer ${authToken}")
        .body(StringBody("""{
          "totalAmount": 199.98,
          "items": [
            {
              "productId": ${__Random(1,10)},
              "productName": "Test Product",
              "price": 99.99,
              "quantity": 2
            }
          ]
        }"""))
        .check(status.is(201))
    )
    .pause(1, 3)
    .exec(
      http("Get User Orders")
        .get("/api/orders")
        .header("Authorization", "Bearer ${authToken}")
        .check(status.is(200))
    )

  // Scenario 5: High Load Product Search
  val highLoadSearchScenario = scenario("High Load Product Search")
    .exec(
      http("Search Products - High Load")
        .get("/api/products?q=${__Random(1,1000)}")
        .check(status.is(200))
    )
    .pause(100.milliseconds, 500.milliseconds)

  // Load Test Configuration
  setUp(
    registrationScenario.inject(
      rampUsers(userCount * 0.2).during(rampUpTime.seconds)
    ),
    browseProductsScenario.inject(
      rampUsers(userCount * 0.3).during(rampUpTime.seconds)
    ),
    shoppingCartScenario.inject(
      rampUsers(userCount * 0.2).during(rampUpTime.seconds)
    ),
    orderProcessingScenario.inject(
      rampUsers(userCount * 0.2).during(rampUpTime.seconds)
    ),
    highLoadSearchScenario.inject(
      rampUsers(userCount * 0.1).during(rampUpTime.seconds)
    )
  )
    .protocols(httpProtocol)
    .maxDuration(testDuration.seconds)
    .assertions(
      global.responseTime.mean.lt(1000), // Mean response time should be less than 1 second
      global.responseTime.percentile3.lt(2000), // 95% of requests should be less than 2 seconds
      global.successfulRequests.percent.gt(95) // 95% of requests should be successful
    )

  // Additional assertions for specific scenarios
  val productSearchAssertions = List(
    global.responseTime.mean.lt(500), // Product search should be fast
    global.successfulRequests.percent.gt(98) // High success rate for product search
  )

  val authAssertions = List(
    global.responseTime.mean.lt(1500), // Auth operations can be slower
    global.successfulRequests.percent.gt(90) // Lower success rate acceptable for auth
  )
}