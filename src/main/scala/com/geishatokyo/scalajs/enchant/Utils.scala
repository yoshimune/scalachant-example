package com.geishatokyo.scalajs.enchant

import scala.scalajs.js.annotation.JSName
import scala.scalajs.js

@JSName("enchant.Counter")
case class Counter(var count: Int = 0) {
    def increment(x: Int = 1) {
      count += x
    }
    def decrement(x: Int = 1) {
      count -= x
    }
}


