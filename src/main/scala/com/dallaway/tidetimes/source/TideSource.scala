package com.dallaway.tidetimes.source

/*
  Copyright 2009 Richard Dallaway

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


case class Metre(value:Double) {
  override def toString = value+"m"
}

import org.joda.time.{LocalDate,LocalTime,DateTimeZone}
  
case class Tide(when:LocalTime, height:Metre) {
  override val toString = when.toString("HH:mm") + " (" + height + ")"   
  def forZone(destZone:DateTimeZone) = Tide(when.toDateTimeToday(DateTimeZone.forID("GMT")).withZone(destZone).toLocalTime(), height)  
}

trait TideSource {	
	def lowsFor(day:LocalDate): List[Tide]
}
