package com.lee

import com.lee.dao.StudentDao

package object service {
  private[service] val stuDao = StudentDao()
}
