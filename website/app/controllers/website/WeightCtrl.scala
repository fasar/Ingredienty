package controllers.website

import play.api._
import play.api.mvc._
import models._
import models.dao._
import models.website._
import models.website.WeightHelper.WeightFormObj
import anorm._
import java.util.{Date, Calendar}
import java.text._
import play.api.data.Form
import controllers.website.utils.DateUtils


object WeightCtrl extends Controller {
  private val log = Logger(WeightCtrl.getClass)
  
  //TODO: Debug -> suppr that and replace with a user management 
  private val user = UserDao.findAll(0)	
  
  def index() = Action {implicit req => 
    log.debug(s"get summary of weight")
    showSummary()
  }
  
  def showSummary()
  	  ( implicit request:Request[AnyContent]) = {
    val weigths = WeightDao.findByUser(user.email)
    Ok(views.html.weight.summary(weigths))
  }

  def addRecipeForm() = Action { implicit req =>
    val form:Form[WeightFormObj] = WeightHelper.weightForm
    val dateSrc = DateUtils.dateFormFormater.format(new Date)
    val formRes = form.bind(Map("date" -> dateSrc)).discardingErrors
    showAddRecipe(formRes)
  }
    
  def showAddRecipe(form:Form[WeightFormObj])(implicit req:Request[AnyContent]) = { 
    Ok(views.html.weight.form(form))
  }
  
  
  def addRecipe() = Action { implicit req =>
    log.debug(s"add a weight for user $user")
    
    WeightHelper.weightForm.bindFromRequest.fold(
      // Form has errors, redisplay it
      errors => showAddRecipe(errors),
      // We got a valid form, add the ingredient to the user
      weightForm => {
        implicit val userImpl:User = user
        val weight:Weight = weightForm
        WeightDao.insert(weight)
        Redirect(controllers.website.routes.WeightCtrl.index)
      }
    )
  }
  
  def deleteWeight(id:Long) = {
    WeightDao.delete(id)
    index()
  }
}