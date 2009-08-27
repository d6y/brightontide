package com.dallaway.tidetimes

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

import source._
import org.joda.time.{LocalDate,DateTimeZone}

/**
 TODO: Handle:
 * 
 * java.io.IOException: Server returned HTTP response code: 503 for URL: http://www.visitbrighton.com/site/tourist-information/tide-timetables
	at sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1170)
	at java.net.URL.openStream(URL.java:1007)
	at scala.io.Source$$anon$5.<init>(Source.scala:168)
	at scala.io.Source$.fromURL(Source.scala:164)
	at scala.io.Source$.fromURL(Source.scala:151)
	at com.dallaway.tidetimes.source.VisitBrightonScraper.page(VisitBrightonScraper.scala:27)
	at com.dallaway.tidetimes.source.VisitBrightonScraper.lowsFor(VisitBrightonScraper.scala:59)
	at com.dallaway.tidetimes.TideTweet$.main(TideTweet.scala:27)
	at com.dallaway.tidetimes.TideTweet.main(TideTweet.scala)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
	at java.lang.reflect.Method.invoke(Method.java:585)
	at scala.tools.nsc.ObjectRunner$$anonfun$run$1.apply(ObjectRunner.scala:75)
	at scala.tools.nsc.ObjectRunner$.withContextClassLoader(ObjectRunner.scala:49)
	at scala.tools.nsc.ObjectRunner$.run(ObjectRunner.scala:74)
	at scala.tools.nsc.MainGenericRunner$.main(MainGenericRunner.scala:154)
	at scala.tools.nsc.MainGenericRunner.main(MainGenericRunner.scala)
 
 */
object TideTweet {

  def main(args:Array[String]) {
    
     val today = new LocalDate
     val gmt_tides = VisitBrightonScraper.lowsFor(today) 
     
     // Time tides are in GMT, but we will later convert to whatever timezone we're in:
     val tz = DateTimeZone.getDefault
     
     // Formulate the tweet to send:
     
     val tweet = gmt_tides match {
       
       case Nil => "Gah! Failed to find tide times today... Help!"
       
       case tide :: Nil => today.toString("'Low tide for 'EE d MMM': '") + tide.forZone(tz)
       
       case tides => today.toString("'Low tides for 'EE d MMM': '") + tides.map(_.forZone(tz)).mkString(", ")
     }
     
     println(tweet)
     
     // Only post the tweet if we have a username and password supplied on the command line:
     args match {  
       case Array(username,password) => send(tweet,username,password)
       case _ => println("Usage: TideTweet twitter_username twitter_password")
     }
     
  }
  
  
  def send(msg:String, username:String, password:String) {
  
    import java.net.{URL,URLEncoder}
    import org.apache.commons.codec.binary.Base64
    
    val url = new URL("http://twitter.com/statuses/update.xml?status="+URLEncoder.encode(msg,"UTF-8")) 
    
    val clear = username + ":" + password
    val auth = new String(Base64.encodeBase64(clear.getBytes("UTF-8")))
        
    // Thank you: http://www.nabble.com/Doing-a-HTTP-Post-td23289069.html
    val urlConn = url.openConnection 
    urlConn.setDoInput(true) 
    urlConn.setDoOutput(true) 
    urlConn.setUseCaches(false) 
    urlConn.setRequestProperty("Authorization", "Basic "+auth) 

    val out = new java.io.DataOutputStream( urlConn.getOutputStream ) 
    out.flush 
    out.close 

    val in = urlConn.getInputStream() 
    println(io.Source.fromInputStream(in).mkString) 
    in.close 
    
    
  }
  
}
