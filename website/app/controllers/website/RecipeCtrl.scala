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
import models.dao.{ UnitDao, IngredientDao }
import anorm._

import play.api.libs.json._

object RecipeCtrl extends Controller {

  import models.website.RecipeHelper.recipeForm;

  private val log = Logger(RecipeCtrl.getClass)

  def recipes = Action {
    log.debug("Get all recipes")
    Ok(views.html.recipe.recipes(Recipe.findAll))
  }


  def form = Action { Ok(views.html.recipe.form(recipeForm)) }


  /**
   * Show a recipe
   * @param id
   * @return
   */
  def showRecipe(id: Long) = Action {
    val existingRecipe = Recipe.findById(id)
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
          case NotImplemented =>
            log.debug("Get a new recipe : " + recipe)
            val res = Recipe.create(recipe)
            res match {
              case Some(e:Recipe) => Ok {views.html.recipe.summary(e) }
              case None => Forbidden
            }
          case Id(id) =>
            log.debug("Update a recipe : " + recipe)
            val res = Recipe.update(recipe)
            res match {
              case Some(e:Recipe) => Ok{views.html.recipe.summary(e)}
              case None => NotFound
            }
        }
      }
    )
  }


  /**
   * Display a form pre-filled with an existing Contact.
   */
  def editForm(id: Long) = Action {
    val existingRecipe = Recipe.findById(id)
    existingRecipe match {
      case Some(r: Recipe) => Ok(views.html.recipe.form(recipeForm.fill(r)))
      case None => NotFound
    }
  }
}

