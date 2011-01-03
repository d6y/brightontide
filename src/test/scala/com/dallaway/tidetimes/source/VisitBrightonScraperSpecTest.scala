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

import org.specs._
import org.specs.runner._
import org.specs.matcher._

import org.joda.time.{LocalDate,LocalTime,DateTimeZone}

class VisitBrightonScraperSpecTest extends Specification {

  object MockScraper {
	import scala.io.Source
    def use(path: String) = new VisitBrightonScraper {
     override def page = Source.fromFile(path, "UTF-8").mkString 
    }   
  }


   "Visit Brighton screen scraper" should {
     
    "locate low tide in first day" in { 
   
      val tides = MockScraper.use("src/test/resources/visitbrighton07052009.html").lowsFor(new LocalDate(2009, 5, 7)).right.get
      
     
     tides.length  must be_==(2)
      
      val expected = List( Tide(new LocalTime(3,38), Metre(1.0)), 
                           Tide(new LocalTime(15,59), Metre(1.0)) )
      
      tides must be_==(expected)
    }
    
       
    "locate low tide in last day" in { 
   
      val tides = MockScraper.use("src/test/resources/visitbrighton07052009.html").lowsFor(new LocalDate(2009, 5, 13)).right.get
      tides.length must be_==(2)
      
      val expected = List( Tide(new LocalTime(7,20), Metre(1.2)), 
                           Tide(new LocalTime(19,39), Metre(1.4)) )
      
      tides must be_==(expected)
    }
   
    
    "Can handle days when there is only one tide" in {

     
      val tides = MockScraper.use("src/test/resources/visitbrighton18052009.html").lowsFor(new LocalDate(2009, 5, 18)).right.get
      tides.length must be_==(1)
      
      val expected = List( Tide(new LocalTime(11,31), Metre(2.1)) )
      
      tides must be_==(expected)
    
    }


	"Can handle days when no tide data is available" in { 
      val tides = MockScraper.use("src/test/resources/visitbrighton03012010.html").lowsFor(new LocalDate(2011, 3, 1))
	  tides must be_==( Left("Match failed") )
    }

     
  }
   
}
