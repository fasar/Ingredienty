package controllers.website

import play.api._
import play.api.mvc._
import models._
import models.dao._
import models.website._
import anorm._
import java.util.{Date, Calendar}
import java.text._
import play.api.data.Form


object WeightCtrl extends Controller {
  private val log = Logger(WeightCtrl.getClass)
  
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
	    currentDate:Date = Calendar.getInstance.getTime)
  	  ( implicit request:Request[AnyContent])=
  {
      val weigths = WeightDao.findByUser(user.email).sortBy{_.date}
      Ok(views.html.weight.summary(weigths))
  }
}