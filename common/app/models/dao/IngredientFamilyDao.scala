package models.dao

import play.api.Play.current
import anorm.SqlParser._
import anorm._
import play.api.db.DB
import models.IngredientFamily

/**
 * Created with IntelliJ IDEA.
 * User: fabien
 * Date: 14/02/13
 * Time: 14:10
 * To change this template use File | Settings | File Templates.
 */
object IngredientFamilyDao {
  /**
   * Parse an IngredientFamily from a ResultSet
   */
  private val simple = {
    get[Pk[Long]]("id") ~
      get[String]("name")   map {
      case id~name => IngredientFamily(id, name)
    }
  }


  /**
   * Retrieve all IngredientFamily from the id.
   */
  def findAll:List[IngredientFamily] = {
    DB.withConnection { implicit connection =>
      SQL("select * from IngredientFamily").as(simple *)
    }
  }

  /**
   * Retrieve an IngredientFamily from the id.
   */
  def findById(id: Long): Option[IngredientFamily] = {
    DB.withConnection { implicit connection =>
      SQL("select * from IngredientFamily where id = {id}")
        .on('id -> id)
        .as(simple.singleOpt)
    }
  }

  /**
   * Update an IngredientFamily.
   * Param id, Object
   */
  def update(id:Long, elem: IngredientFamily): Boolean = {
    DB.withConnection { implicit connection =>
      val nbRow = SQL(
        """
          update IngredientFamily
          set name = {name}
          where id = {id}
        """
      ).on(
        'id -> id,
        'name -> elem.name
      ).executeUpdate()
      nbRow >= 1
    }
  }


  /**
   * Insert a new IngredientFamily.
   *
   * param : the object
   */
  def insert(elem: IngredientFamily):Option[IngredientFamily] = {
    DB.withConnection { implicit connection =>
      val resId =  SQL(
        """
          insert into IngredientFamily (id, name) values (
            (select next value for IngredientFamily_seq),
            {name}
          )
        """
      ).on(
        'name -> elem.name
      ).executeInsert()
      resId map {id => IngredientFamily(Id(id), elem.name) }
    }
  }

  /**
   * Delete an IngredientFamily.
   *
   * @param id Id of the IngredientFamily to delete.
   */
  def delete(id: Long) = {
    DB.withConnection { implicit connection =>
      SQL("delete from IngredientFamily where id = {id}")
        .on('id -> id).executeUpdate()
    }
  }

  /**
   * Delete an IngredientFamily.
   *
   * @param elem IngredientFamily to delete.
   */
  def delete(elem: IngredientFamily) {
    if (elem.id.isDefined)
      delete(elem.id.get)
  }
}
