package controllers.website


import play.api._
import libs.Comet
import libs.iteratee.Enumerator
import play.api.mvc._
import views._

import models._
import models.dao.{UnitDao, IngredientDao}
import anorm._

import play.api.libs.json._



object RecipeCtrl extends Controller {
  private val log = Logger(classOf[Application])

  def recipes = Action { Ok(html.recipe.recipes(Recipe.findAll))}
    
  import models.website.Recipe4Json._
  def createRecipe = Action(parse.json) { request =>
    request.body.validate[Recipe].map{ 
      case (recipe:Recipe) => 
        Recipe.create(recipe)
        Ok(recipe.toString)
    }.recoverTotal{
      e => BadRequest("Detected error:"+ JsError.toFlatJson(e) + "\n" + e.toString)
    }
  }
  
  
}