package com.dallaway.tidetimes.source

/*
  Copyright 2009-2014 Richard Dallaway

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

import scala.language.reflectiveCalls
import util.Try
import scala.io.Source

import org.joda.time.{LocalDate}
import org.joda.time.format.{DateTimeFormat}

object EasyTideScraper extends EasyTideScraper

class EasyTideScraper extends TideSource {

  // Other ports are available
  lazy val brighton_marina = "http://easytide.ukho.gov.uk/EasyTide/EasyTide/ShowPrediction.aspx?PortID=0082&PredictionLength=1"

  def page = Source.fromURL(brighton_marina).mkString

  def lowsFor(day:LocalDate) = {

    // We want the records that start with the date in this format: Mon 3 Jan
    val date = DateTimeFormat.forPattern("E d MMM").print(day)

    // The page is structured as three rows. We don't know how many columns, but it'll be the same for all rows.
    // The rows are: headings (e.g., HW, LW); times (e.g., 12:30), heights (e.g., 1.6 m)
    val PagePattern =
      """|(?sm).*<th class="HWLWTableHeaderCell".*>DATE</th>\s*</tr>\s*
        |<tr>(.+?)</tr>\s*<tr>(.+?)</tr>\s*<tr>(.+?)</tr>.*""".stripMargin.replaceAll("\n","").replaceFirst("DATE", date).r

    Try {
      val PagePattern(headings_html, times_html, heights_html) = page

      // The values in a column are surrounded by <th> or <td> tags
      def deTag(html: String) = ">([^<]+)<".r.findAllIn(html).matchData map {_.group(1)}

      // Select just the low water info as a list of (time,height) pairs
      val tides = (deTag(headings_html) zip (deTag(times_html) zip deTag(heights_html))) collect { case ("LW", info) => info }

      val result = for ( (time_string, height_string) <- tides ) yield
        Tide( time_string.toLocalTime, height_string.toMetre )

      result.toList
    }

  }



}
