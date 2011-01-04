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

class EasyTideScraperSpecTest extends Specification {

  object MockScraper {
	import scala.io.Source
    def use(path: String) = new EasyTideScraper {
     override def page = Source.fromFile(path, "UTF-8").mkString 
    }   
  }


   "EasyTide screen scraper" should {
     
    "locate low tides when there are two a day" in { 
   
      val tides = MockScraper.use("src/test/resources/easytide20110303.html").lowsFor(new LocalDate(2011, 1, 3))

      tides.right.get.length must be_==(2)
      
      val expected = List( Tide(new LocalTime(4,8), Metre(1.3)), 
                           Tide(new LocalTime(16,33), Metre(1.1)) )
      
      tides.right.get must be_==(expected)
    }
    
    
     
  }
   
}
