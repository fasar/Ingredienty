package models.dao

import play.api.Play.current
import anorm.SqlParser._
import anorm._
import play.api.db.DB
import anorm.~
import models.Ingredient
import play.api.cache.Cache
import play.api.Logger


object IngredientDao {
  private val log = Logger(IngredientDao.getClass)

  /**
   * Parse an Ingredient from a ResultSet
   * 
   * It is useful for anorm parsing
   */
  private val simple = {
    get[Pk[Long]]("ingredient.id") ~
      get[String]("ingredient.name") ~
      get[Option[Long]]("ingredient.ingredientfamily_id")  map {
      case id~name~family => Ingredient(id, name, family)
    }
  }


  /**
   * Retrieve all Ingredient from the id.
   */
  def findAllReq:List[Ingredient] = {
    DB.withConnection { implicit connection =>
      val res = SQL("select * from ingredient").as(simple *)
      log.debug("get all ingredients : " + res.size)
      res
    }
  }

  /**
   * Retrieve all Ingredient from the id.
   */
  def findAll:List[Ingredient] = {
    val timeout = 7200
    Cache.getOrElse[List[Ingredient]]("ingredientDao.ingredients", timeout) {
      log.info("setup a cache for all ingredients for " + timeout + " seconds")
      this.findAllReq
    }
  }

  /**
   * Retrieve an Ingredient from the id.
   */
  def findById(id: Long): Option[Ingredient] = {
    DB.withConnection { implicit connection =>
      SQL("select * from Ingredient where id = {id}")
        .on('id -> id)
        .as(simple.singleOpt)
    }
  }

  /**
   * Retrieve an Ingredient from the name.
   */
  def findByName(name: String): Option[Ingredient] = {
    DB.withConnection { implicit connection =>
      SQL("select * from Ingredient where name = {name}")
        .on('name -> name)
        .as(simple.singleOpt)
    }
  }

  /**
   * Update a computer.
   * Param id, Object
   */
  def update(id:Long, elem: Ingredient): Boolean = {
    DB.withConnection { implicit connection =>
      val nbRow:Int = SQL(
        """
          update Ingredient
          set name = {name}, family_id = {family_id}
          where id = {id}
        """
      ).on(
        'id -> id,
        'name -> elem.name,
        'family_id -> elem.familyId
      ).executeUpdate()
      nbRow == 1
    }
  }


  /**
   * Insert a new computer.
   *
   * param : the object
   */
  def insert(elem: Ingredient): Option[Ingredient] = {
    DB.withConnection { implicit connection =>
      val id = SQL(
        """
          insert into Ingredient values (
            (select next value for ingredient_seq),
            {name}, {family_id}
          )
        """
      ).on(
        'name -> elem.name,
        'family_id -> elem.familyId
      ).executeInsert()
      val res = id map {x=> Ingredient(Id(x), elem.name, elem.familyId)}
      res
    }
  }

  /**
   * Delete an object.
   *
   * @param id Id of the Ingredient to delete.
   */
  def delete(id: Long): Boolean = {
    DB.withConnection { implicit connection =>
      val res = SQL("delete from Ingredient where id = {id}")
        .on('id -> id).executeUpdate()
      res == 1
    }
  }

  /**
   * Delete an object.
   *
   * @param elem Ingredient to delete.
   */
  def delete(elem: Ingredient): Boolean = {
    if (elem.id.isDefined)
      delete(elem.id.get)
    else false
  }
}
