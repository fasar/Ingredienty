package models.dao

import play.api.Play.current
import anorm.SqlParser._
import anorm._
import play.api.db.DB
import anorm.~
import models.Ingredient

/**
 * Created with IntelliJ IDEA.
 * User: fabien
 * Date: 14/02/13
 * Time: 12:38
 * To change this template use File | Settings | File Templates.
 */
object IngredientDao {
  /**
   * Parse an Ingredient from a ResultSet
   */
  private val simple = {
    get[Pk[Long]]("ingredient.id") ~
      get[String]("ingredient.name") ~
      get[Option[String]]("ingredient.family")  map {
      case id~name~family => Ingredient(id, name, family, Map.empty)
    }
  }


  /**
   * Retrieve all unit from the id.
   */
  def findAll:List[Ingredient] = {
    DB.withConnection { implicit connection =>
      SQL("select * from ingredient").as(simple *)
    }
  }

  /**
   * Retrieve an unit from the id.
   */
  def findById(id: Long): Option[Ingredient] = {
    DB.withConnection { implicit connection =>
      SQL("select * from Ingredient where id = {id}")
        .on('id -> id)
        .as(IngredientDao.simple.singleOpt)
    }
  }

  /**
   * Update a computer.
   * Param id, Object
   */
  def update(id:Long, elem: Ingredient) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          update Ingredient
          set name = {name}, family = {family}
          where id = {id}
        """
      ).on(
        'id -> id,
        'name -> elem.name,
        'family -> elem.family
      ).executeUpdate()
    }
  }


  /**
   * Insert a new computer.
   *
   * param : the object
   */
  def insert(elem: Ingredient) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into Ingredient values (
            (select next value for ingredient_seq),
            {name}, {family}
          )
        """
      ).on(
        'name -> elem.name,
        'family -> elem.family
      ).executeUpdate()
    }
  }

  /**
   * Delete an object.
   *
   * @param id Id of the unit to delete.
   */
  def delete(id: Long) = {
    DB.withConnection { implicit connection =>
      SQL("delete from unit where id = {id}")
        .on('id -> id).executeUpdate()
    }
  }

  /**
   * Delete an object.
   *
   * @param elem unit to delete.
   */
  def delete(elem: Ingredient) {
    if (elem.id.isDefined)
      delete(elem.id.get)
  }
}
