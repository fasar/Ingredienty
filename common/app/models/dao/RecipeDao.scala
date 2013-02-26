package models.dao

import play.api.Play.current
import anorm.SqlParser._
import anorm._
import play.api.db.DB
import anorm.~
import models._
import play.api.Logger


object RecipeDao {
  private val log = Logger(RecipeDao.getClass)

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
  def findAll: List[Recipe] = {
    DB.withConnection { implicit connection =>
      SQL("select * from Recipe").as(this.simple *)
    }
  }

  /**
   * Retrieve a Recipe from an id.
   */
  def findById(id: Long): Option[Recipe] = {
    DB.withConnection { implicit connection =>
      SQL("select * from Recipe where id = {id}").on(
        'id -> id).as(this.simple.singleOpt)
    }
  }

  /**
   * Create a Recipe.
   */
  def insert(element: Recipe): Option[Recipe] = {
    try {insertTrans(element)}
    catch {
      case e:Exception => 
        log.error(s"can't inster a recipe : ${element}. Exception : ${e}\n ${e.getStackTrace().slice(0, 14).mkString("\n")}")
        None
    }
  }
  
  def insertTrans(element: Recipe): Option[Recipe] = {
    DB.withTransaction { implicit connection =>
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
          'authorEmail -> element.author.map{_.email},
          'is_public -> element.isPublic,
          'description -> element.description,
          'prepTimeSec -> element.prepTimeSec,
          'cookTimeSec -> element.cookTimeSec).executeInsert()
      val res = id.map { id =>
        val newRecipe = Recipe(Id(id), element.name,
          element.instructions, element.author,
          element.isPublic, element.description,
          element.prepTimeSec, element.cookTimeSec,
          element.ingredients)
        log.error(s"J'ai une nouvelle recette en cour d'insertion : ${newRecipe}")
        createIngredientsQuantities(newRecipe)
        newRecipe
      }
      res
    }
  }

  private def createIngredientsQuantities(recipe: Recipe)
              (implicit connection: java.sql.Connection):List[IngredientRecipeQuantity] = {
    val ingredientsQuantities = recipe.ingredients
    log.error(s"Je créé des ingredients pour : ${recipe}")
    val newIngredientsQuantities =
      for ((ingredient, quantity) <- ingredientsQuantities) yield {
        val nbRow = SQL(
          """
            insert into IngredientsRecipeQuantity_Map values (
              {recipe_id}, {ingredient_id}, {quantity}
            )
          """).on(
            'recipe_id -> recipe.id.get,
            'ingredient_id -> ingredient.id.get,
            'quantity -> quantity).executeUpdate()
        if (nbRow >= 1) {
          IngredientRecipeQuantity(recipe.id, ingredient.id, quantity)
        } else {
          val errorStr = s"Can't add ingredient ${ingredient} with quantity ${quantity} for the recipe : ${recipe}"
          log.error(errorStr)
          throw new Exception(errorStr)
        }
      }
    
    newIngredientsQuantities
  }

  /**
   * Update a Recipe.
   */
  def update(id:Long, element: Recipe): Boolean = {
    try {updateTrans(id, element)}
    catch {
      case e:Exception => 
        log.error(s"can't update a recipe : ${element}. \n Exception : ${e}\n ${e.getStackTrace().slice(0, 14).mkString("\n")}")
        false
    }  
  }
  
  def updateTrans(id:Long, element: Recipe): Boolean = {
   DB.withTransaction { implicit connection =>
     val nbRow = SQL(
        """
          update Recipe
          set 
            name={name}, instructions={instructions}, author_email={authorEmail},
            is_public={is_public}, description={description}, 
            prep_time_sec={prepTimeSec}, cook_time_sec={cookTimeSec}
          where id = {recipeId}
        """).on(
        'recipeId -> id,
        'name -> element.name,
        'instructions -> element.instructions,
        'authorEmail -> element.author.map{_.email},
        'is_public -> element.isPublic,
        'description -> element.description,
        'prepTimeSec -> element.prepTimeSec,
        'cookTimeSec -> element.cookTimeSec).executeUpdate()
      if(nbRow>=1) {
        log.error(s"J'ai une recette en cour d'update : ${element} avec l'id : ${id}")
        val newRecipe = Recipe(Id(id), element.name,
                  element.instructions, element.author,
                  element.isPublic, element.description,
                  element.prepTimeSec, element.cookTimeSec,
                  element.ingredients)
          deleteAllIngredients(newRecipe)
          createIngredientsQuantities(newRecipe)
          true
      } else false
    }
  }
  /**
   * Delete a all ingredients of a recipe
   *
   * @param element IngredientRecipeQuantity to delete.
   */
  private def deleteAllIngredients(recipe: Recipe)
  	(implicit connection: java.sql.Connection):Boolean = {
    val ingredientsQuantities = recipe.ingredients
    val nbRow = for (recipeId <- recipe.id) 
    yield {
      SQL(
        """
          delete from IngredientsRecipeQuantity_Map
	  where recipe_id = {recipeId}
        """).on(
          'recipeId -> recipeId
        ).executeUpdate()
    }
    nbRow.getOrElse(0) >=1
  }
  
  
  /**
   * Delete a Recipe.
   *
   * @param id Id of the Recipe to delete.
   */
  def delete(id: Long):Boolean = {
    DB.withConnection { implicit connection =>
      val nbRow = SQL("delete from recipe where id = {id}")
        .on('id -> id).executeUpdate()
      nbRow >= 1
    }
  }

  /**
   * Delete a Recipe.
   *
   * @param element Recipe to delete.
   */
  def delete(element: Recipe):Boolean = {
    (element.id map {delete(_)}).getOrElse(false)
  }

  
}


