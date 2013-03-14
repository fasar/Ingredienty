package models.website

import java.util.Date
import play.api.data.Form
import models._
import play.api.data.Forms._
import models.dao.IngredientDao
import models.Ingredient
import scala.Some
import anorm._

     
object WeightHelper {
  
  implicit def convWeightToWeigthFromObj(weight:Weight): WeightFormObj = {
    WeightFormObj(weight.id, weight.date, weight.weight, 
          weight.fat, weight.water, 
          weight.muscles, weight.bones, 
          weight.visceralFat)
  }
  
  implicit def convWeigthFromObjToWeight(weight:WeightFormObj)
      		(implicit user:User): Weight = {
      Weight(weight.id, user, weight.date, weight.weight, 
          weight.fat, weight.water, 
          weight.muscles, weight.bones, 
          weight.visceralFat)
  }
  
  case class WeightFormObj(id:Pk[Long],
		  date: Date,
	          weight: Double,
	          fat: Double,
	          water: Double,
	          muscles: Double,
	          bones: Double,
	          visceralFat:Int) {
    
    def setAuthor(user:User): Weight = {
      Weight(id, user, date, 
            weight.toDouble, fat.toDouble, 
            water.toDouble, muscles.toDouble, 
            bones.toDouble, visceralFat)
    }
  }
  
  
  /**
   * Add a recipe Form definition.
   *
   * Once defined it handle automatically, ,
   * validation, submission, errors, redisplaying, ...
   */
  val weightForm: Form[WeightFormObj] = Form(
    mapping(
      "id" -> optional(longNumber),
      "date" -> date("dd-MM-yyyy"),
      "weight" -> bigDecimal,
      "fat" -> bigDecimal,
      "water" -> bigDecimal,
      "muscles" -> bigDecimal,
      "bones" -> bigDecimal,
      "visceralFat" -> number
    ) {
      (id, date, weight, fat, water, muscles, bones, visceralFat) => {
        val idPk = id match {
          case Some(x) => new Id(x)
          case None => NotAssigned
        }
        WeightFormObj(idPk, date, 
            weight.toDouble, fat.toDouble, 
            water.toDouble, muscles.toDouble, 
            bones.toDouble, visceralFat)
      }
    }{ weight =>
      val id: Option[Long] = weight.id.toOption
      Some(id, weight.date, weight.weight, 
          weight.fat, weight.water, 
          weight.muscles, weight.bones, 
          weight.visceralFat)
    }
  )

}
