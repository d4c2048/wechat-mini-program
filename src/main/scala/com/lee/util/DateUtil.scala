package com.lee.util

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import scala.annotation.tailrec

object DateUtil {
  private[this] val ONE_DAY_MS = 24 * 60 * 60 * 1000
  private[this] val PATTERN = "yyyy-MM-dd HH:mm:ss"

  def getDiffMs(from: Date, to: Date): Long = {
    to.getTime - from.getTime
  }

  def getDiffMs(from: Date): Long = {
    getDiffMs(from, Calendar.getInstance.getTime)
  }

  def overOneDay(from: Date, to: Date): Boolean = {
    overSomeDays(from, to, 1)
  }

  def overOneDay(from: Date): Boolean = {
    overOneDay(from, Calendar.getInstance.getTime)
  }

  def overSomeDays(from: Date, to: Date, days: Int): Boolean = {
    getDiffMs(from, to) > days * ONE_DAY_MS
  }

  def overSomeDays(from: Date, days: Int): Boolean = {
    overSomeDays(from, Calendar.getInstance.getTime, days)
  }

  def format(date: Date, pattern: String): String = {
    val formatter = new SimpleDateFormat(pattern)
    formatter.format(date)
  }

  def format(date: Date): String = {
    format(date, PATTERN)
  }

  def parseDate(source: String): Date = {
    val formatter = new SimpleDateFormat
    formatter.parse(source)
  }
}
