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

import scala.language.implicitConversions
import util.Try
import java.time.{LocalTime, LocalDate}

case class Metre(value:Double)

case class Tide(when: LocalTime, height: Metre)

trait TideSource {
  def lowsFor(day: LocalDate): Try[List[Tide]]

  // Convert "12:34" to LocalTme(12,34)
  implicit def localTimeWrapper(time_string:String) = new {
    val parts = time_string.trim.split(":")
    def toLocalTime = LocalTime.of(parts(0).toInt, parts(1).toInt)
  }

  // Convert a noisy string ("1.3 m") into a Metre(1.3)
  implicit def metreWrapper(value: String) = new {
    private def digits = value takeWhile ( n => n.isDigit || n=='.' )
    def toMetre = Metre(digits.toDouble)
  }

}
