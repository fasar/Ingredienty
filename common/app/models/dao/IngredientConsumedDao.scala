package models.dao

import play.api.Play.current
import anorm.SqlParser._
import anorm._
import play.api.db.DB
import anorm.~
import models._
import play.api.cache.Cache
import play.api.Logger
import java.util.Date


object IngredientConsumedDao {
  private val log = Logger(IngredientConsumedDao.getClass)

  /**
   * Parse an IngredientConsumedDao from a ResultSet
   * 
   * It is useful for anorm parsing
   */
  private val simple = {
      get[Long]("ingredient_id") ~
      get[String]("email") ~
      get[Date]("cdate") ~
      get[Option[Long]]("recipe_id") ~
      get[Double]("quantity")  map {
         case ingredient_id~email~cdate~recipe_id~quantity =>
           val userOpt = User.findByEmail(email)
           val ingredientOpt = IngredientDao.findById(ingredient_id)
           val recipeOpt = recipe_id map { Recipe.findById(_)}
           IngredientConsumed(userOpt.get, cdate, 
    	       ingredientOpt.get, quantity, recipeOpt.get)
    }
  }


  /**
   * Retrieve all Ingredient from the id.
   */
  def findAll:List[IngredientConsumed] = {
    DB.withConnection { implicit connection =>
      val res = SQL("select * from IngredientConsumed").as(simple *)
      log.debug("get all IngredientConsumed : " + res.size)
      res
    }
  }


  /**
   * Retrieve an Ingredient from the id.
   */
  def findByEmail(email: String): List[IngredientConsumed] = {
    DB.withConnection { implicit connection =>
      SQL("select * from IngredientConsumed where email = {email}")
        .on('email -> email)
        .as(simple *)
    }
  }

  /**
   * Retrieve an Ingredient from the name.
   */
  def findByEmail(email: String, cdate: Date): List[IngredientConsumed] = {
    val dateStart = cdate
    val dateEnd = cdate
    DB.withConnection { implicit connection =>
      SQL("""
          select * from IngredientConsumed 
          where email = {email} and cdate={cdate}
          """)
        .on('email -> email, 'cdate -> cdate)
        .as(simple *)
    }
  }

  /**
   * Update a computer.
   * Param id, Object
   */
  def update(ingredientId:Long, email:String, cdate: Date, quantity: Double) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          update Ingredient
          set quantity = {quantity}
          where ingredient_id={ingredientId} email = {email} and cdate={cdate}
        """
      ).on(
        'ingredientId -> ingredientId,
        'email -> email,
        'cdate -> cdate,
        'quantity -> quantity
      ).executeUpdate()
    }
  }
  
  /**
   * Update a computer.
   * Param id, Object
   */
  def update(ingredientConsumed: IngredientConsumed) {
    update(
        ingredientConsumed.ingredient.id.get, 
        ingredientConsumed.user.email, 
        ingredientConsumed.date, 
        ingredientConsumed.quantity
    )
  }

  /**
   * Insert a new computer.
   *
   * param : the object
   */
  def insert(elem: IngredientConsumed) = {
    val recipeId = elem.recipe map {_.id.get }
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into IngredientConsumed 
          (ingredient_id, email, cdate, recipe_id, quantity)
          values (
            {ingredient_id}, {email}, {cdate},
            {recipe_id}, {quantity}
          )
        """
      ).on(
        'ingredient_id -> elem.ingredient.id.get,
        'email -> elem.user.email,
        'cdate -> elem.date,
        'recipe_id -> recipeId,
        'quantity -> elem.quantity
      ).executeUpdate()
    }
  }

  /**
   * Delete an object.
   *
   * @param id Id of the Ingredient to delete.
   */
  def delete(elem: IngredientConsumed) = {
    DB.withConnection { implicit connection =>
      SQL("""
          delete from IngredientConsumed
          where ingredient_id={ingredientId} email = {email} and cdate={cdate}
          """
      ).on(
          'ingredient_id -> elem.ingredient.id.get,
          'email -> elem.user.email,
          'cdate -> elem.date
      ).executeUpdate()
    }
  }

}