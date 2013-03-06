package models.website

import java.util.Date
import play.api.data.Form
import models.Recipe
import play.api.data.Forms._
import models.dao.IngredientDao
import models.Ingredient
import scala.Some


object RecipeHelper {
  /**
   * Add a recipe Form definition.
   *
   * Once defined it handle automatically, ,
   * validation, submission, errors, redisplaying, ...
   */
  val recipeForm: Form[Recipe] = Form(
    mapping(
      "id" -> optional(longNumber),
      "name" -> nonEmptyText,
      "instructions" -> text,
      "author" -> email,
      "isPublic" -> checked("public"),
      "description" -> nonEmptyText,
      "prepTimeSec" -> number,
      "cookTimeSec" -> number,
      "ingredients" -> list(
        tuple(
          "ingredientId" -> longNumber,
          "quantity" -> bigDecimal.verifying(_ >= 0.0)
        )
      )
    ){
      (id, name, instructions, author, isPublic, description, prepTimeSec, cookTimeSec, ingredients) => {
        //Recipe.findById(1).get
        val ingredientsList:List[(Ingredient, Double)] =  (ingredients.map{
          case (ingredientId, quantBD) =>
            val quantity = quantBD.toDouble
            val ingredientOpt = IngredientDao.findById(ingredientId)
            ingredientOpt match {
              case Some(ingredient) => Right((ingredient -> quantity))
              case None => Left(None)
            }
        }).filter(_.isRight).map {_.right.get}
        Recipe(id, name, instructions, Some(author), isPublic, description, prepTimeSec, cookTimeSec, ingredientsList)

      }
    }{ recipe =>
      val ingredients =
        (for( (ingredient, quantity) <- recipe.ingredients)
        yield {
          ingredient.id.get -> BigDecimal(quantity)
        }).toList
      val author:String = recipe.author match { case Some(user) => user.email; case None => ""}
      val id: Option[Long] = recipe.id.toOption
      Some(id, recipe.name, recipe.instructions, author,
        recipe.isPublic, recipe.description, recipe.prepTimeSec,
        recipe.cookTimeSec, ingredients)
    }
  )
  
  val addRecipeToDailyForm: Form[(Long, String, BigDecimal, Option[Date], Option[Date])] = Form(
    tuple(
      "id" -> longNumber,
      "name" -> text,
      "quantity" -> bigDecimal,
      "date" -> optional(date("dd-MM-yyyy")),
      "hour" -> optional(date("HH:mm"))
    )
  )
}
