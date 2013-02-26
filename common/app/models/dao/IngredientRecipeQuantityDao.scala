package models.dao

import play.api.Play.current
import anorm.SqlParser._
import anorm._
import play.api.db.DB
import anorm.~
import models._


object IngredientRecipeQuantityDao {

  // -- Parsers
  /**
   * Parse a User from a ResultSet
   */
  private val simple = {
    get[Pk[Long]]("recipe_id") ~
      get[Pk[Long]]("ingredient_id") ~
      get[Double]("quantity") map {
        case recipeId ~ ingredientId ~ quantity => IngredientRecipeQuantity(recipeId, ingredientId, quantity)
      }
  }

  /**
   * Retrieve a IngredientRecipeQuantity from email.
   */
  def findByRecipeId(recipe_id: Long): List[IngredientRecipeQuantity] = {
    DB.withConnection { implicit connection =>
      SQL("select * from IngredientsRecipeQuantity_Map where recipe_id = {recipe_id}").on(
        'recipe_id -> recipe_id).as(this.simple *)
    }
  }

  /**
   * Create an IngredientRecipeQuantity.
   */
  def create(element: IngredientRecipeQuantity): IngredientRecipeQuantity = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into IngredientsRecipeQuantity_Map values (
            {recipe_id}, {ingredient_id}, {quantity}
          )
        """).on(
          'recipe_id -> element.recipeId,
          'ingredient_id -> element.ingredientId,
          'quantity -> element.quantity).executeUpdate()

      element

    }
  }

  /**
   * Delete an IngredientRecipeQuantity.
   *
   * @param recipeId of the IngredientRecipeQuantity to delete.
   */
  def delete(recipeId: Long, ingredientId: Long): Boolean = {
    DB.withConnection { implicit connection =>
      val nbRow = SQL(
        "delete from IngredientsRecipeQuantity_Map where recipe_id = {recipe_id} and ingredient_id = {ingredient_id}").on(
          'recipe_id -> recipeId,
          'ingredient_id -> ingredientId
      ).executeUpdate()
      nbRow >= 1
    }
  }

  /**
   * Delete a IngredientRecipeQuantity.
   *
   * @param element IngredientRecipeQuantity to delete.
   */
  def delete(element: IngredientRecipeQuantity): Boolean = {
    (for (
      recepeId <- element.recipeId;
      ingredientId <- element.ingredientId
    ) yield {
      delete(recepeId, ingredientId)
    }).getOrElse(false)
  }
  
}
