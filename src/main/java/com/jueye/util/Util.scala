package com.jueye.util

import java.util.regex.{Matcher, Pattern}


/**
  * Created by soledede on 2015/12/25.
  */
object Util {
  def regexExtract(input: String, regex: String): Object = regexExtract(input, regex, -2)

  def regexExtract(input: String, regex: String, group: Int): Object = {
    if (regex == null) {
      return input
    }
    val p: Pattern = Pattern.compile(regex)
    val m: Matcher = p.matcher(input)
    if (group == -2) {
      val list = new java.util.ArrayList[java.lang.String]

      while (m.find) {
        var i: Int = 1
        while (i <= m.groupCount) {
          {
            list.add(m.group(i))
          }
          i += 1
        }
      }
      return list
    } else {
      if (m.find) {
        if (group == -1) {
          var r = ""
          val s = new StringBuilder
          var i: Int = 0
          while (i <= m.groupCount - 1) {
            {
              s ++= m.group(i)
              if (i != m.groupCount) {
                s ++= " "
              }
            }
            ({
              i += 1
            })
          }
          r =s.toString
          if(r.length==0)
            r = m.group()
          return r
        }
        else {
          return m.group(group)
        }
      }
      else return ""
    }
  }
}
