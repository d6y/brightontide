package com.dallaway.tidetimes

/*
  Copyright 2009-2018 Richard Dallaway

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

import source._
import java.time.{ZoneId, LocalDate, ZonedDateTime}
import java.time.format.DateTimeFormatter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import util.{Failure, Success, Try}
import cats.syntax.show._
import cats.Show

object TzAdjustment {
  implicit class TideOps(tide: Tide) {
    def forZone(destZone: ZoneId, at: LocalDate): Tide = {
      val localDateTime = ZonedDateTime
        .of(at, tide.when, ZoneId.of("UTC"))
        .withZoneSameInstant(destZone)
      Tide(localDateTime.toLocalTime(), tide.height)
    }
  }
}

object TideTweet {

  implicit val tideShow: Show[Tide] = tide =>
    s"${tide.when} (${tide.height.value}m)"

  def main(args: Array[String]) {

    val today = LocalDate.now()
    val date: String = DateTimeFormatter.ofPattern("EE d MMM").format(today)

    val gmt_tides = VisitBrightonScraper.lowsFor(today) orElse EasyTideScraper
      .lowsFor(today)

    // Time tides are in GMT, but we will later convert to whatever timezone we're in:
    import TzAdjustment._
    val brighton = ZoneId.of("Europe/London")

    // Formulate the tweet to send:
    val tweet = gmt_tides match {

      case Success(tide :: Nil) =>
        s"Low tide for $date: " + tide.forZone(brighton, today).show

      case Success(tides) =>
        s"Low tides for $date: " + tides
          .map { _.forZone(brighton, today).show }
          .mkString(", ")

      case Failure(msg) =>
        println(msg)
        "@d6y please fix me! I couldn't lookup the tides today. Sorry."
    }

    args match {

      // post the tweet via http://dev.twitter.com/pages/oauth_single_token
      case Array(
          consumer_key,
          token_value,
          consumer_secret,
          access_token_secret) =>
        val twitter = TwitterFactory.getSingleton
        twitter.setOAuthConsumer(consumer_key, consumer_secret)
        twitter.setOAuthAccessToken(
          new AccessToken(token_value, access_token_secret))
        val status = Try(twitter.updateStatus(tweet))
        println(status.map(_.getText))

      case _ =>
        println(
          "Not posting as consumer and token information not provided on the command line")
        println(
          "Usage: TideTweet consumer-key token-value consumer-secret token-secret")
        println(tweet)

    }

  }

}
