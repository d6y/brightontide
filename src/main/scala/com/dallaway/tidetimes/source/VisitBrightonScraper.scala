package com.dallaway.tidetimes.source

/*
  Copyright 2009-2011 Richard Dallaway

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

import org.joda.time.{LocalDate,LocalTime,DateTimeZone}
import org.joda.time.format.{DateTimeFormat,DateTimeFormatter}
import scala.io.Source

object VisitBrightonScraper extends VisitBrightonScraper 

class VisitBrightonScraper extends TideSource {

  def page = Source.fromURL("http://www.visitbrighton.com/site/tourist-information/tide-timetables").mkString
  
   // Convert 7 -> "7th" etc
   implicit def ordinalWrapper(day:LocalDate) = new {
	   def ordinal = day.getDayOfMonth match {
	     case f if (List(1,21,31) contains f) => f+"st"
	     case s if (List(2,22) contains s) => s+"nd"
	     case t if (List(3,23) contains t) => t+"rd"
	     case n => n+"th"
	   }
    }
   
  override def lowsFor(day:LocalDate) : Either[Error,List[Tide]] = {
 
   // We want the records that start with the date in this format: 10th May 2009
   val date = day.ordinal + DateTimeFormat.forPattern(" MMMM yyyy").print(day); 
    
   val Pattern = 
     """|(?sm).*<div class="TidalDataEntry"><h3>DATE</h3><table class="TidalData"><tr>
        |<th>&nbsp;</th><th class="Time">Time</th><th class="Height">Height .m.</th></tr><tr>
        |<td class="Tide">High</td><td class="Time">(.+?)</td><td class="Height">(.+?)</td></tr><tr>
        |<td class="Tide">Low</td><td class="Time">(.+?)</td><td class="Height">(.+?)</td></tr>
        |</table></div>.*""".stripMargin.replaceAll("\n","").replaceFirst("DATE", date).r
    
   try  {
     val Pattern(high_times,high_heights,low_times,low_heights) = page
	 
     // The times and heights are in separate columns; multiple values separated by "<br/>"
     // When there is only one tide, a non-breaking space is used in place of the time
     val tides = for { (time_string,height) <- low_times.split("<br/>") zip low_heights.split("<br/>") 
                       if (time_string != "&nbsp;")
       				} yield 
       					Tide( time_string.toLocalTime, Metre(height.toDouble) )
	  
	  Right(tides.toList)
   }
   catch {
    case x:scala.MatchError => Left("Match failed")
    case x:NumberFormatException => Left(x.getMessage)    
   }	
  
  }
 
  
  
}
