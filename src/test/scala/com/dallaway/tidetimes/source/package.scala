package com.dallaway.tidetimes
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
import org.specs2.matcher.{Expectable, Matcher}
import util.{Failure, Success, Try}

package object source {

  def beSuccess[T](t: => T) = new Matcher[Try[T]] {
    def apply[S <: Try[T]](value: Expectable[S]) = {
      val expected = t
      result(
        value.value == Success(t),
        value.description + " is Success with value " + expected,
        value.description + " is not Success with value " + expected,
        value)
    }
  }

  // Not tried:
  def beFailure[T](t: => T) = new Matcher[Try[T]] {
    def apply[S <: Try[T]](value: Expectable[S]) = {
      val expected = t
      result(
        value.value match {
          case Failure(x) => x == expected
          case _ => false
        },
        value.description + " is Failure with value " + expected,
        value.description + " is not Failure with value " + expected,
        value
      )
    }
  }

  def beFailure = new Matcher[Try[Any]] {
    def apply[S <: Try[Any]](value: Expectable[S]) = {
      result(
        value.value match {
          case Failure(x) => true
          case _ => false
        },
        value.description + " is Failure",
        value.description + " is not Failure",
        value)
    }
  }

}
