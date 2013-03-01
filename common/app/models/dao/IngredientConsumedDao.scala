package models.dao

import play.api.Play.current
import anorm.SqlParser._
import anorm._
import play.api.db.DB
import anorm.~
import models._
import play.api.cache.Cache
import play.api.Logger
import java.util.{Date, Calendar}


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
           val userOpt = UserDao.findByEmail(email)
           val ingredientOpt = IngredientDao.findById(ingredient_id)
           val recipeOpt = recipe_id map { id => RecipeDao.findById(id)}
           IngredientConsumed(userOpt.get, cdate, 
    	       ingredientOpt.get, quantity, recipeOpt.getOrElse(None))
    }
  }


  /**
   * Retrieve all Ingredient from the id.
   */
  def findAll:List[IngredientConsumed] = {
    DB.withConnection { implicit connection =>
      val res = SQL("select ingredient_id, email, cdate, recipe_id, quantity from ConsumedIngredient").as(simple *)
      log.debug("get all IngredientConsumed : " + res.size)
      res
    }
  }


  /**
   * Retrieve an Ingredient from the id.
   */
  def findByEmail(email: String): List[IngredientConsumed] = {
    DB.withConnection { implicit connection =>
      SQL("select ingredient_id, email, cdate, recipe_id, quantity from ConsumedIngredient where email = {email}")
        .on('email -> email)
        .as(simple *)
    }
  }

  /**
   * Retrieve an Ingredient from the name.
   */
  def findByEmail(email: String, cdate: Date): List[IngredientConsumed] = {
    val calendar = Calendar.getInstance()
    calendar.setTime(cdate)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val dateStart = calendar.getTime
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    val dateEnd = calendar.getTime
    
    DB.withConnection { implicit connection =>
      SQL("""
          select ingredient_id, email, cdate, recipe_id, quantity from ConsumedIngredient 
          where email = {email} and cdate >= {dateStart} and cdate < {dateEnd}
          """)
        .on('email -> email, 
            'dateStart -> dateStart,
            'dateEnd -> dateEnd)
        .as(simple *)
    }
  }

  /**
   * Update a computer.
   * Param id, Object
   */
  def update(ingredientId:Long, 
      email:String, cdate: Date, 
      quantity: Double, recipe:Option[Long]):Boolean = 
  {
    DB.withConnection { implicit connection =>
      val nbRow = SQL(
        """
          update ConsumedIngredient
          set quantity = {quantity}, recipe_id = {recipeId}
          where ingredient_id = {ingredientId} and email = {email} and cdate = {cdate}
        """
      ).on(
        'ingredientId -> ingredientId,
        'email -> email,
        'cdate -> cdate,
        'quantity -> quantity,
        'recipeId -> recipe
      ).executeUpdate()
      nbRow >= 1
    }
  }
  
  /**
   * Update a computer.
   * Param id, Object
   */
  def update(ingredientConsumed: IngredientConsumed):Boolean = {
    val recipeId = ingredientConsumed.recipe map { _.id.get }
    update(
        ingredientConsumed.ingredient.id.get, 
        ingredientConsumed.user.email, 
        ingredientConsumed.date, 
        ingredientConsumed.quantity,
        recipeId
    )
  }

  /**
   * Insert a new computer.
   *
   * param : the object
   */
  def insert(elem: IngredientConsumed): Option[IngredientConsumed] = {
    val recipeId = elem.recipe map {_.id.get }
    DB.withConnection { implicit connection =>
      val nbRow = SQL(
        """
          insert into ConsumedIngredient 
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
      if(nbRow >= 1) Some(elem)
      else None
    }
  }

  /**
   * Delete an object.
   *
   * @param id Id of the Ingredient to delete.
   */
  def delete(elem: IngredientConsumed):Boolean = {
    DB.withConnection { implicit connection =>
      val sql = SQL("""
          delete from ConsumedIngredient
          where ingredient_id = {ingredientId} and email = {email} and cdate = {cdate}
          """
      )
      
      val nbRow = sql.on(
          'ingredientId -> elem.ingredient.id.get,
          'email -> elem.user.email,
          'cdate -> elem.date
      ).executeUpdate()
      nbRow >= 1
    }
  }

}