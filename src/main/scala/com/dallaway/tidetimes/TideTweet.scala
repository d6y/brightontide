package com.dallaway.tidetimes

/*
  Copyright 2009-2010 Richard Dallaway

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
import org.joda.time.{LocalDate,DateTimeZone}

object TideTweet {

  def main(args:Array[String]) {
    
     val today = new LocalDate
     val gmt_tides = VisitBrightonScraper.lowsFor(today) 
     
     // Time tides are in GMT, but we will later convert to whatever timezone we're in:
     val tz = DateTimeZone.getDefault
     
     // Formulate the tweet to send:
     
     val tweet = gmt_tides match {
       
       case Nil => "Gah! Failed to find tide times today... @d6y help!"
       
       case tide :: Nil => today.toString("'Low tide for 'EE d MMM': '") + tide.forZone(tz)
       
       case tides => today.toString("'Low tides for 'EE d MMM': '") + tides.map {_.forZone(tz)}.mkString(", ")
     }
     
     println(tweet)
     
     // post the tweet
     
  }
  
  
  
  
}
