package models.website

import play.api.data.Form
import models.Recipe
import play.api.data.Forms._
import models.dao.IngredientDao
import models.Ingredient
import scala.Some
import java.util.Date

object IngredientConsumedHelper {
  /**
   * Add a recipe Form definition.
   *
   * Once defined it handle automatically, ,
   * validation, submission, errors, redisplaying, ...
   */
  val form: Form[(Long, BigDecimal, Option[Date], Option[Date])] = Form(
    tuple(
      "id" -> longNumber,
      "quantity" -> bigDecimal,
      "date" -> optional(date("dd-MM-yyyy")),
      "hour" -> optional(date("hh:mm"))
    )
  )

}