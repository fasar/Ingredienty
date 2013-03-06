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
import java.util.{Date, Calendar}
import java.text._
import play.api.data.Form
import models.website.RecipeHelper
import org.omg.CosNaming.NamingContextPackage.NotFound

 
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
	  form:Form[(Long, BigDecimal, Option[Date], Option[Date])] = 
	    IngredientConsumedHelper.form ,
	    currentDate:Date = Calendar.getInstance.getTime)
  	  ( implicit request:Request[AnyContent])=
  {
      val cosumed = IngredientConsumedDao.findByEmail(user.email, currentDate).sortBy{_.date}
      Ok(views.html.daily.summary(currentDate, cosumed, form))
  }
  
  
  def addConsumedIngredient(datep:String = "") = Action { implicit req =>
    log.debug("add an ingredient for user at: " + datep)
    val calendar:Calendar = getCalendarFromString(datep)
    
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
            val hour = if(hourOpt.isDefined) {
              hourOpt.get 
            } else {
              new Date()
            }
             
            val hourC = Calendar.getInstance()
            hourC.setTime(hour)
            calendar.set(Calendar.HOUR_OF_DAY, hourC.get(Calendar.HOUR_OF_DAY))
            calendar.set(Calendar.MINUTE, hourC.get(Calendar.MINUTE))

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
  
  private def getCalendarFromString(datep:String): Calendar = {
    val calendar:Calendar = try {
	val myDate = Global.dateFormaterYearMountDate.parse(datep)
	val res = Calendar.getInstance()
	res.setTime(myDate)
	res
    } catch {
      case e:Exception => 
        val res = Calendar.getInstance();
        res.set(Calendar.SECOND, 0)
        res.set(Calendar.MILLISECOND, 0)
        res
    }
    calendar
  }
  
  
  def deleteConsumedIngredient(datep:String, hourp:String, idIngredient:Long) = Action {
    log.debug(s"delete ingredient $idIngredient cosumed at $datep $hourp")
    val calendar:Calendar = getCalendarFromString(datep)
    val hour = Global.dateFormaterHours.parse(hourp)
    val calHour = Calendar.getInstance
    calHour.setTime(hour)
    calendar.set(Calendar.HOUR_OF_DAY, calHour.get(Calendar.HOUR_OF_DAY))
    calendar.set(Calendar.MINUTE, calHour.get(Calendar.MINUTE))
    calendar.set(Calendar.SECOND, calHour.get(Calendar.SECOND))
    calendar.set(Calendar.MILLISECOND, calHour.get(Calendar.MILLISECOND))
    IngredientConsumedDao.delete(idIngredient, user.email, calendar.getTime)
    Redirect(routes.Application.index(datep))
  }
  
  def showAddRecipeForm(datep:String, recipeId:Long) = Action { implicit request => 
    log.debug(s"get a form for add a recipe $recipeId at date $datep")
    val date = 
      if(Global.regexDate.findFirstIn(datep).isDefined) {
	  val dated = Global.dateFormaterYearMountDate.parse(datep)
	  Global.dateFormFormater.format(dated)
      } else ""
    val form = RecipeHelper.addRecipeToDailyForm
    val recipeOpt = RecipeDao.findById(recipeId)
    if(recipeOpt.isDefined) {
      val recipe = recipeOpt.get
      val hour = Global.dateFormaterHours.format(new Date)
      val defaultValues = Map("id" -> recipeId.toString, 
          "name" -> recipe.name, "date" -> date, 
          "hour" -> hour)
      val formDefaultV = form.bind(defaultValues)
      Ok(views.html.daily.addRecipeToDailyForm(formDefaultV))
      
    } else {
	NotFound
    }
  }
  

  
  
  def addRecipe() = Action { implicit request => 
    RecipeHelper.addRecipeToDailyForm.bindFromRequest fold (
      // Form has errors, redisplay it
      errors => Ok(views.html.daily.addRecipeToDailyForm(errors)),
      // We got a valid form, add the ingredient to the user
      recipeForm => {
        val (id:Long, name:String, quantityBD:BigDecimal, dateOpt, hourOpt) = recipeForm
        val quantityDb = quantityBD.toDouble
        val calendar = getCalendar(dateOpt, hourOpt)
        val date = calendar.getTime
        val dateStr = Global.dateFormaterAll.format(date)
        log.debug(f"add recipe id $id at date $dateStr with quantity $quantityDb%1.2f")
        val recipeOpt = RecipeDao.findById(id)
        if(recipeOpt.isDefined) {
          val recipe = recipeOpt.get
          val rawQauntityIngredients = recipe.ingredients
          val totalQuantityRecipe = 
            rawQauntityIngredients.foldRight(0.0) { case (elem, acc) => acc + elem._2}
          val quantityIngredients = 
            for(qi <- rawQauntityIngredients)
            yield {
              val quantity = (qi._2/totalQuantityRecipe * quantityDb)
              (qi._1, quantity)
              IngredientConsumed(user, date, qi._1, quantity, recipeOpt)
            }
          IngredientConsumedDao.insert(quantityIngredients)
          Redirect(routes.Application.index(Global.dateFormaterYearMountDate.format(date)))
        } else {
          log.debug(f"can't add recipe because id $id is not found")
          Redirect(routes.Application.index(Global.dateFormaterYearMountDate.format(date)))
        }
      }
    )
  }
  
  private def getCalendar(datep:Option[String], hourp:Option[String]): Calendar = {
    val calDay:Calendar = getCalendarFromString(datep.getOrElse(""))
    val calHour = if(hourp.isDefined) {
      val hour = Global.dateFormaterHours.parse(hourp.get)
      val cal = Calendar.getInstance
      cal.setTime(hour)
      cal
    } else {
      Calendar.getInstance
    }
    getCalendar(calDay, calHour)
    
  }
  
  private def getCalendar(datep:Option[Date], hourp:Option[Date]): Calendar = {
    val date = datep.getOrElse(new Date)
    val hour = hourp.getOrElse(new Date)
    val calDay:Calendar = Calendar.getInstance()
    calDay.setTime(date)
    val calHour = Calendar.getInstance
    calHour.setTime(hour)  
    getCalendar(calDay, calHour)
  }
  
  private def getCalendar(calDay:Calendar, calHour:Calendar): Calendar = { 
    calDay.set(Calendar.HOUR_OF_DAY, calHour.get(Calendar.HOUR_OF_DAY))
    calDay.set(Calendar.MINUTE, calHour.get(Calendar.MINUTE))
    calDay.set(Calendar.SECOND, 0)
    calDay.set(Calendar.MILLISECOND, 0)
    calDay
  }
}