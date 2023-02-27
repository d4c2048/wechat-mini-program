package com.lee

import com.lee.dao.{OrderDao, StudentDao}

package object service {
  private[service] lazy val stuDao = StudentDao()
  private[service] lazy val odrDao = OrderDao()
}
