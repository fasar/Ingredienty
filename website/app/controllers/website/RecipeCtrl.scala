package controllers.website

import play.api._
import libs.Comet
import libs.iteratee.Enumerator
import play.api.mvc._
import play.api.data.Forms._
import play.api.data._

import views._

import html.helper.repeat
import models._
import models.dao._
import anorm._

import play.api.libs.json._

object RecipeCtrl extends Controller {

  import models.website.RecipeHelper.recipeForm;

  private val log = Logger(RecipeCtrl.getClass)

  def recipes = Action {
    log.debug("Get all recipes")
    Ok(views.html.recipe.recipes(RecipeDao.findAll))
  }


  def form = Action { Ok(views.html.recipe.form(recipeForm)) }


  /**
   * Show a recipe
   * @param id
   * @return
   */
  def show(id: Long) = Action{
    val existingRecipe = RecipeDao.findById(id)
    existingRecipe match {
      case Some(r: Recipe) => Ok(views.html.recipe.summary(r))
      case None => NotFound
    }
  }



  /**
   * Handle form submission.
   */
  def submit = Action { implicit request =>
    recipeForm.bindFromRequest.fold(
      // Form has errors, redisplay it
      errors => BadRequest(views.html.recipe.form(errors)),

      // We got a valid recipe, display the summary
      recipe => {
        recipe.id match {
          case NotAssigned =>
            log.debug("Get a new recipe : " + recipe)
            val res = RecipeDao.insert(recipe)
            res match {
              case Some(e:Recipe) => Ok {views.html.recipe.summary(e) }
              case None => Forbidden
            }
          case Id(id) =>
            log.debug("Update a recipe : " + recipe)
            val res = RecipeDao.update(id, recipe)
            res match {
              case true => Ok{views.html.recipe.summary(recipe)}
              case false => NotFound
            }
        }
      }
    )
  }


  /**
   * Display a form pre-filled with an existing Contact.
   */
  def editForm(id: Long) = Action {
    val existingRecipe = RecipeDao.findById(id)
    existingRecipe match {
      case Some(r: Recipe) => Ok(views.html.recipe.form(recipeForm.fill(r)))
      case None => NotFound
    }
  }
}

