package controllers.website

import java.text.SimpleDateFormat

object Global {

  val dateFormaterAll = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
  val dateFormaterHours = new SimpleDateFormat("HH:mm")
  val dateFormaterYearMountDate = new SimpleDateFormat("yyyyMMdd")
  
  // regex catch only date from 2010 to 2050
  val regexDate = """(20[12345]\d(((0[13578]|1[02])([012]\d|3[01]))|((0[469]|11)([012]\d|30))|(02(([01]\d)|(2[0-9])))))""".r
    
}