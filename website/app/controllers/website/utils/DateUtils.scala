package controllers.website.utils

import java.text.SimpleDateFormat

/**
 * Created with IntelliJ IDEA.
 * User: fabien
 * Date: 29/08/13
 * Time: 21:12
 * To change this template use File | Settings | File Templates.
 */
object DateUtils {
  val dateFormaterAll = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
  val dateFormaterHours = new SimpleDateFormat("HH:mm")
  val dateFormaterDays = new SimpleDateFormat("dd-MM-yyyy")

  val dateFormaterYearMountDate = new SimpleDateFormat("yyyyMMdd")
  val dateFormFormater = new SimpleDateFormat("dd-MM-yyyy")
  val dateUrlFormater = new SimpleDateFormat("yyyyMMdd")

  // regex catch only date from 2010 to 2050
  val regexDate = """(20[12345]\d(((0[13578]|1[02])([012]\d|3[01]))|((0[469]|11)([012]\d|30))|(02(([01]\d)|(2[0-9])))))""".r

}

