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

class EasyTideScraperSpecTest extends Specification {

  object MockScraper {
    import scala.io.Source
    def use(path: String) = new EasyTideScraper {
      override def page = Source.fromFile(path, "UTF-8").mkString
    }
  }

  "EasyTide screen scraper" should {

    "locate low tides in 2013 HTML format" in {

      val tides = MockScraper
        .use("src/test/resources/easytide20130101.html")
        .lowsFor(LocalDate.of(2013, 1, 1))
      tides.map(_.length) must beSuccess(2)

      val expected = Tide(LocalTime.of(7, 23), Metre(1.2)) :: Tide(
        LocalTime.of(19, 38),
        Metre(1.0)) :: Nil
      tides must beSuccess(expected)
    }

  }

}
