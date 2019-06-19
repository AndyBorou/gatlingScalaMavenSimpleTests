package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

//Simple showcase of a maven project using the gatling-maven-plugin
//To test it out, simply execute the following command:
//
//$mvn gatling:test -Dgatling.simulationClass=computerdatabase.BasicSimulation
//or simply:
//
//$mvn gatling:test

class  GoogleTest extends Simulation {

  val httpProtocol = http
    .baseUrl("https://www.google.com") // Here is the root for all relative URLs
    .inferHtmlResources() // i do not know what is this
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0")

  val scn = scenario("test goole search page") // A scenario is a chain of requests and pauses
    .exec(http("request_1")
      .get("/"))
    .pause(1) // Note that Gatling has recorder real time pauses
    .exec(http("search by 'macbook' word")
      .get("/search?source=hp&ei=cj0JXbKOO8ygjgbvtJuIBQ&q=macbook")
      .check(status.is(200))
      .check(substring("macbook")))
    .pause(1)
    .exec(http("return to main page")
      .get("/"))
    .pause(1)
    .exec(http("click on <I'm Feeling Lucky>")
      .get("/doodles/")
      .check(status.is(200)))

  setUp(
    scn.inject(
    nothingFor(4 seconds), // 1
      atOnceUsers(10), // 2
      constantUsersPerSec(20) during (15 seconds), // 4
      constantUsersPerSec(20) during (15 seconds) randomized, // 5
      rampUsersPerSec(10) to 20 during (55 seconds), // 6
      rampUsersPerSec(10) to 20 during (55 seconds) randomized, // 7
  ).protocols(httpProtocol))
}
