package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

import play.api.Logger
import java.util.Date

import models.dao.{ IngredientDao }

case class Recipe(id: Pk[Long] = NotAssigned,
                  name: String,
                  instructions: String,
                  author: Option[User],
                  isPublic: Boolean,
                  description: String,
                  prepTimeSec: Int,
                  cookTimeSec: Int,
                  ingredients: List[(Ingredient, Double)])

object Recipe {
  private type ThisType = Recipe

  def apply( id: Option[Long],
             name: String,
             instructions: String,
             authorStr: Option[String],
             isPublic: Boolean,
             description: String,
             prepTimeSec: Int,
             cookTimeSec: Int,
             ingredients: List[(Ingredient, Double)]): Recipe = {
    val author: Option[User] = authorStr.flatMap {x=> User.findByEmail(x) }
    id match {
      case Some(id) => new Recipe(Id(id), name, instructions, author, isPublic, description, prepTimeSec, cookTimeSec, ingredients)
      case None => new Recipe(NotAssigned, name, instructions, author, isPublic, description, prepTimeSec, cookTimeSec, ingredients)

    }
  }

  def apply(id: Pk[Long],
            name: String,
            instructions: String,
            authorStr: Option[String],
            isPublic: Boolean,
            description: String,
            prepTimeSec: Int,
            cookTimeSec: Int //ingredients: Map[Ingredient, Double]
            ): Recipe = {
    val author: Option[User] =  authorStr.flatMap {x=> User.findByEmail(x) }
    val ingredientsQuantities = getIngredientsQuantities(id)
    new Recipe(id, name, instructions, author, isPublic, description, prepTimeSec, cookTimeSec, ingredientsQuantities)
  }

  private def getIngredientsQuantities(id: Pk[Long]): List[(Ingredient, Double)] = {
    val ingredientQuantities = IngredientRecipeQuantity.findByRecipeId(id.get)
    (for (
      ingredientQuantity <- ingredientQuantities;
      ingredientOpt = IngredientDao.findById(ingredientQuantity.ingredientId.get) if ingredientOpt.isDefined;
      ingredient <- ingredientOpt
    ) yield {
      val quantity = ingredientQuantity.quantity
      ingredient -> quantity
    })
  }

  // -- Parsers

  /**
   * Parse a Recipe from a ResultSet
   */
  private val simple = {
    get[Pk[Long]]("id") ~
      get[String]("name") ~
      get[String]("instructions") ~
      get[Option[String]]("author_email") ~
      get[Boolean]("is_public") ~
      get[String]("description") ~
      get[Int]("prep_time_sec") ~
      get[Int]("cook_time_sec") map {
        case id ~ name ~ instructions ~ author ~ ispublic ~ description ~ prepTimeSec ~ cookTimeSec =>
          Recipe(id, name, instructions, author, ispublic, description, prepTimeSec, cookTimeSec)
      }
  }

  // -- Queries

  /**
   * Retrieve all Recipes.
   */
  def findAll: Seq[ThisType] = {
    DB.withConnection { implicit connection =>
      SQL("select * from Recipe").as(this.simple *)
    }
  }

  /**
   * Retrieve a Recipe from an id.
   */
  def findById(id: Long): Option[ThisType] = {
    DB.withConnection { implicit connection =>
      SQL("select * from Recipe where id = {id}").on(
        'id -> id).as(this.simple.singleOpt)
    }
  }
  
  /**
   * Create a Recipe.
   */
  def create(element: ThisType): Option[ThisType] = {
    DB.withConnection { implicit connection =>
      val id: Option[Long] = SQL(
        """
          insert into recipe (name, instructions, author_email, is_public, description, prep_time_sec, cook_time_sec)
          values (
            {name}, {instructions}, {authorEmail},
            {is_public}, {description}, {prepTimeSec}, {cookTimeSec}
          )
        """).on(
          'name -> element.name,
          'instructions -> element.instructions,
          'authorEmail -> Option.empty[String],
          'is_public -> element.isPublic,
          'description -> element.description,
          'prepTimeSec -> element.prepTimeSec,
          'cookTimeSec -> element.cookTimeSec).executeInsert()
      id.map{id =>
          val recipe = Recipe(Id(id), element.name,
              element.instructions, element.author,
              element.isPublic, element.description,
              element.prepTimeSec, element.cookTimeSec,
              element.ingredients)
          createIngredientsQuantities(recipe)
          recipe
      }
    }
  }

  private def createIngredientsQuantities(recipe: Recipe)(implicit connection: java.sql.Connection) = {
    val ingredientsQuantities = recipe.ingredients
    for ((ingredient, quantity) <- ingredientsQuantities) {
      SQL(
        """
          insert into IngredientsRecipeQuantity_Map values (
            {recipe_id}, {ingredient_id}, {quantity}
          )
        """).on(
          'recipe_id -> recipe.id.get,
          'ingredient_id -> ingredient.id.get,
          'quantity -> quantity).executeUpdate()
    }
  }


  /**
   * Update a Recipe.
   */
  def update(element: ThisType): Option[ThisType] = {
    throw new Exception("Not implemented yet")
  }


  /**
   * Delete a Recipe.
   *
   * @param id Id of the Recipe to delete.
   */
  def delete(id: Long) {
    DB.withConnection { implicit connection =>
      SQL("delete from recipe where id = {id}")
        .on('id -> id).executeUpdate()
    }
  }

  /**
   * Delete a Recipe.
   *
   * @param element Recipe to delete.
   */
  def delete(element: ThisType) {
    if (element.id.isDefined) { delete(element.id.get); }
  }

}

case class IngredientRecipeQuantity(
  recipeId: Pk[Long],
  ingredientId: Pk[Long],
  quantity: Double);

object IngredientRecipeQuantity {
  type ThisType = IngredientRecipeQuantity

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
  def findByRecipeId(recipe_id: Long): List[ThisType] = {
    DB.withConnection { implicit connection =>
      SQL("select * from IngredientsRecipeQuantity_Map where recipe_id = {recipe_id}").on(
        'recipe_id -> recipe_id).as(this.simple *)
    }
  }

  /**
   * Create an IngredientRecipeQuantity.
   */
  def create(element: ThisType): ThisType = {
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
  def delete(recipeId: Long, ingredientId: Long) = {
    DB.withConnection { implicit connection =>
      SQL(
        "delete from IngredientsRecipeQuantity_Map where recipe_id = {recipe_id} and ingredient_id = {ingredient_id}").on(
          'recipe_id -> recipeId,
          'ingredient_id -> ingredientId).executeUpdate()
    }
  }

  /**
   * Delete a User.
   *
   * @param element IngredientRecipeQuantity to delete.
   */
  def delete(element: ThisType) {
    for (
      recepeId <- element.recipeId;
      ingredientId <- element.ingredientId
    ) {
      delete(recepeId, ingredientId)
    }
  }

}
