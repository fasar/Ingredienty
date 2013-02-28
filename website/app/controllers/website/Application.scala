package controllers.website

import play.api._
import libs.Comet
import libs.iteratee.Enumerator
import libs.json.{JsString, Json}
import play.api.mvc._
import models._
import models.dao._
import models.website.IngredientConsumedHelper
import anorm._
import java.util._
import java.text._
import play.api.data.Form

 
object Application extends Controller {
  private val log = Logger(classOf[Application])
  
  //TODO: Debug -> suppr that
  private val user = UserDao.findAll(0)		   
      
  //TODO: Debug -> suppr that
  val simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss")
  val date = simpleDateFormat.parse("20130210-125328")
      
  def index() = Action {
    req => showSummary()

  }
  
  def showSummary(
	  form:Form[(Long, BigDecimal, Option[Date], Option[Date])] = IngredientConsumedHelper.form 
	  ) = {
      //TODO: Manage date
      val dateStr = DateFormat.getDateInstance().format(date)
      val cosumed = IngredientConsumedDao.findByEmail(user.email, date)
      Ok(views.html.daily.summary(dateStr, cosumed, form))
  }
  
  
  def addIngredient() = Action { implicit req =>
    IngredientConsumedHelper.form.bindFromRequest.fold(
      // Form has errors, redisplay it
      errors => showSummary(errors),
      // We got a valid form, add the ingredient to the user
      ingredientQuantity => {
        val ingredient = IngredientDao.findById(ingredientQuantity._1)
        val quantity = ingredientQuantity._2
        //TODO: Manage date
        ingredient match {
          case Some(ing) => 
            val newVal = IngredientConsumed(user, date, ing, quantity.toDouble, None)
            IngredientConsumedDao.insert(newVal)
            Redirect(routes.Application.index)
          case None =>
            Redirect(routes.Application.index)
        }
        
	
      }
    )
  }
}