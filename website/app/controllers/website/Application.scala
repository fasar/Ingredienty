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
  private val log = Logger(Application.getClass)
  
  //TODO: Debug -> suppr that and replace with a user management 
  private val user = UserDao.findAll(0)		   
      
  def index(date:String = "") = Action {implicit req => 
    log.debug(s"get index page of date ${req.path}")
    if(date != "") {
        try {
    	val myDate = Global.dateFormaterYearMountDate.parse(date)
    	showSummary(currentDate = myDate)
        } catch {
          case e:Exception => 
            Redirect(routes.Application.index(""))
        }
    } else {
      showSummary()
    }
    
  }
  
  def showSummary(
	  form:Form[(Long, BigDecimal, Option[Date], Option[Date])] = IngredientConsumedHelper.form ,
	  currentDate:Date = Calendar.getInstance.getTime)
  	  ( implicit request:Request[AnyContent])=
  {
      val cosumed = IngredientConsumedDao.findByEmail(user.email, currentDate).sortBy{_.date}
      Ok(views.html.daily.summary(currentDate, cosumed, form))
  }
  
  
  def addIngredient(datep:String = "") = Action { implicit req =>
    log.error("add an ingredient for user : " + datep)
    val calendar:Calendar = try {
	val myDate = Global.dateFormaterYearMountDate.parse(datep)
	val res = Calendar.getInstance()
	res.setTime(myDate)
	res
    } catch {
      case e:Exception => 
        Calendar.getInstance();
    }
    
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
            val hourOpt = ingredientQuantity._4
            hourOpt map { hour =>
              val hourC = Calendar.getInstance()
              hourC.setTime(hour)
              calendar.set(Calendar.HOUR_OF_DAY, hourC.get(Calendar.HOUR_OF_DAY))
              calendar.set(Calendar.MINUTE, hourC.get(Calendar.MINUTE))
            }
            val date = calendar.getTime
            val newVal = IngredientConsumed(user, date, ing, quantity.toDouble, None)
            IngredientConsumedDao.insert(newVal)
            Redirect(routes.Application.index(datep))
          case None =>
            Redirect(routes.Application.index(datep))
        }
      }
    )
  }
  
  
  def addRecipe() = TODO
}