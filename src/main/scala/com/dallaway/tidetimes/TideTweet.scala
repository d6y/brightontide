package com.dallaway.tidetimes

/*
  Copyright 2009-2012 Richard Dallaway

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

     // val gmt_tides = VisitBrightonScraper.lowsFor(today) 
     val gmt_tides = EasyTideScraper.lowsFor(today) 
     
     // Time tides are in GMT, but we will later convert to whatever timezone we're in:
     val tz = DateTimeZone.getDefault
     
     // Formulate the tweet to send:
     val tweet = gmt_tides match {
            
       case Right(tide :: Nil) => today.toString("'Low tide for 'EE d MMM': '") + tide.forZone(tz)
       
       case Right(tides) => today.toString("'Low tides for 'EE d MMM': '") + tides.map {_.forZone(tz)}.mkString(", ")
       
       case _ => "No tide times found today. @d6y please help."
     }
     
     
     args match {
       
        // post the tweet via http://dev.twitter.com/pages/oauth_single_token
       case Array(consumer_key, token_value, consumer_secret, access_token_secret) => 
    
         import dispatch._
         import dispatch.twitter._
         import dispatch.oauth._
   
         val consumer = Consumer(consumer_key, consumer_secret)
         val single_access_token = Token(token_value, access_token_secret)
    
         val http = new Http()
         http(Status.update(tweet, consumer, single_access_token) >- {println(_)} )
       
       case _ => 
         	println("Not posting as consumer and token information not provided on the command line")
         	println("Usage: TideTweet consumer-key token-value consumer-secret token-secret")
            println(tweet)
 
     }
     
     
  }
  
  
  
  
}
