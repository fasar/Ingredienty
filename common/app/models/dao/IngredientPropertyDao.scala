package models.dao

import play.api.Play.current
import anorm.SqlParser._
import anorm._
import play.api.db.DB
import anorm.~
import models.IngredientProperty

/**
 * Created with IntelliJ IDEA.
 * User: fabien
 * Date: 14/02/13
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
object IngredientPropertyDao {
  /**
   * Parse an IngredientProperty from a ResultSet
   */
  private val simple = {
    get[Pk[String]]("name") ~
      get[Option[String]]("description")~
      get[Long]("unit_id") map {
      case id~description~unitId => IngredientProperty(id, description, unitId)
    }
  }


  /**
   * Retrieve all IngredientProperty from the id.
   */
  def findAll:List[IngredientProperty] = {
    DB.withConnection { implicit connection =>
      SQL("select * from IngredientProperty").as(simple *)
    }
  }

  /**
   * Retrieve an IngredientProperty from the id.
   */
  def findById(id: Long): Option[IngredientProperty] = {
    DB.withConnection { implicit connection =>
      SQL("select * from IngredientProperty where id = {id}")
        .on('id -> id)
        .as(simple.singleOpt)
    }
  }

  /**
   * Update a computer.
   * Param id, Object
   */
  def update(id:Long, elem: IngredientProperty) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          update IngredientProperty
          set name = {name}
          where id = {id}
        """
      ).on(
        'id -> id,
        'name -> elem.name
      ).executeUpdate()
    }
  }


  /**
   * Insert a new computer.
   *
   * param : the object
   */
  def insert(elem: IngredientProperty) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into IngredientProperty values (
            (select next value for IngredientProperty_seq),
            {name}
          )
        """
      ).on(
        'name -> elem.name
      ).executeUpdate()
    }
  }

  /**
   * Delete an object.
   *
   * @param name Id of the IngredientProperty to delete.
   */
  def delete(name: String) = {
    DB.withConnection { implicit connection =>
      SQL("delete from IngredientProperty where name = {name}")
        .on('name -> name).executeUpdate()
    }
  }

  /**
   * Delete an object.
   *
   * @param elem IngredientProperty to delete.
   */
  def delete(elem: IngredientProperty) {
    if (elem.name.isDefined)
      delete(elem.name.get)
  }
}
