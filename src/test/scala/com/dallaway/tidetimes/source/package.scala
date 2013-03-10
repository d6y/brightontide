package com.dallaway.tidetimes

import org.specs2.matcher.{Expectable, Matcher}
import util.{Failure, Success, Try}

package object source {

  def beSuccess[T](t: =>T) = new Matcher[Try[T]] {
    def apply[S <: Try[T]](value: Expectable[S]) = {
      val expected = t
      result(value.value == Success(t),
        value.description + " is Success with value " + expected,
        value.description + " is not Success with value " + expected,
        value)
    }
  }

  // Not tried:
  def beFailure[T](t: => T) = new Matcher[Try[T]] {
    def apply[S <: Try[T]](value: Expectable[S]) = {
      val expected = t
      result( value.value match {
        case Failure(x) => x == expected
        case _ => false
      },
        value.description + " is Failure with value " + expected,
        value.description + " is not Failure with value " + expected,
        value)
    }
  }

  def beFailure = new Matcher[Try[Any]] {
    def apply[S <: Try[Any]](value: Expectable[S]) = {
      result( value.value match {
        case Failure(x) => true
        case _ => false
      },
        value.description + " is Failure",
        value.description + " is not Failure",
        value)
    }
  }



}
