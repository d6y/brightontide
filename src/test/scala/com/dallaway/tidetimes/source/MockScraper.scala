package com.dallaway.tidetimes.source

import scala.io.Source

object MockScraper {

  def use(path: String) = new VisitBrightonScraper {
   override def page = Source.fromFile(path, "UTF-8").mkString 
  }   
  
}
