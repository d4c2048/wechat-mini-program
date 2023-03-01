package com.lee.util

import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.{Calendar, Date, Locale, TimeZone}
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

  def diffHour(hour: Int = 0, min: Int = 0, sec: Int = 0, nano: Int = 0) = {
    val time = LocalTime.of(hour, min, sec, nano).toNanoOfDay
    val now = LocalTime.now().toNanoOfDay
    val fullDay = 24L * 60 * 60 * 1000 * 1000 * 1000
    val diff = time - now
    if (diff < 0) fullDay + diff else diff
  }

  /**
   * 根据指定格式获取指定地区、指定时区的当前时间
   *
   * @param pattern  时间格式
   * @param locale   地区
   * @param timeZone 时区
   * @return 格式化时间
   */
  def currentTime(pattern: String, locale: Locale, timeZone: TimeZone): String = {
    new SimpleDateFormat(pattern).format(Calendar.getInstance(timeZone, locale).getTime)
  }

  /**
   * 根据指定格式获取默认地区的当前时间
   *
   * @param pattern 时间格式
   * @return 格式化时间
   */
  def currentTime(pattern: String): String = currentTime(pattern, Locale.getDefault, TimeZone.getDefault)

  /**
   * 获取“yyyy:MM:dd HH:mm:ss”格式的默认地区的当前时间
   *
   * @return 格式化时间
   */
  def currentTime: String = currentTime("yyyy-MM-dd HH:mm:ss")
}
