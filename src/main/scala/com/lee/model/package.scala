package com.lee

package object model {
  implicit val orderOrdering = Ordering.fromLessThan[Order](_.status > _.status)
}
