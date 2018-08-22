package com.dallaway.tidetimes.source

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

import org.specs2.mutable._

import java.time.{LocalDate, LocalTime}

class VisitBrightonScraperSpecTest extends Specification {

  object MockScraper {
    import scala.io.Source
    def use(path: String) = new VisitBrightonScraper {
      override def page = Source.fromFile(path, "UTF-8").mkString
    }
  }

  "Visit Brighton screen scraper" should {

    "locate low tide in first day" in {

      val tides = MockScraper
        .use("src/test/resources/visitbrighton07052009.html")
        .lowsFor(LocalDate.of(2009, 5, 7))

      tides.map(_.length) must beSuccess(2)

      val expected = List(
        Tide(LocalTime.of(3, 38), Metre(1.0)),
        Tide(LocalTime.of(15, 59), Metre(1.0)))

      tides must beSuccess(expected)
    }

    "locate low tide in last day" in {

      val tides = MockScraper
        .use("src/test/resources/visitbrighton07052009.html")
        .lowsFor(LocalDate.of(2009, 5, 13))
      tides.map(_.length) must beSuccess(2)

      val expected = List(
        Tide(LocalTime.of(7, 20), Metre(1.2)),
        Tide(LocalTime.of(19, 39), Metre(1.4)))

      tides must beSuccess(expected)
    }

    "Can handle days when there is only one tide" in {

      val tides = MockScraper
        .use("src/test/resources/visitbrighton18052009.html")
        .lowsFor(LocalDate.of(2009, 5, 18))
      tides.map(_.length) must beSuccess(1)

      val expected = List(Tide(LocalTime.of(11, 31), Metre(2.1)))

      tides must beSuccess(expected)

    }

    "Can handle days when no tide data is available" in {
      val tides = MockScraper
        .use("src/test/resources/visitbrighton03012010.html")
        .lowsFor(LocalDate.of(2011, 3, 1))
      tides must beFailure
    }

  }

}
