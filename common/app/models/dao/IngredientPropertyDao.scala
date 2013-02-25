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
    get[Pk[Long]]("id") ~
      get[String]("name") ~
      get[Option[String]]("description")~
      get[Long]("unit_id") map {
      case id~name~description~unitId => IngredientProperty(id, name, description, unitId)
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
  def findById(id:Long): Option[IngredientProperty] = {
    DB.withConnection { implicit connection =>
      SQL("select * from IngredientProperty where id = {id}")
        .on('id -> id)
        .as(simple.singleOpt)
    }
  }

  /**
   * Update an IngredientProperty.
   * Param id, Object
   */
  def update(id:Long, elem: IngredientProperty):Boolean = {
    DB.withConnection { implicit connection =>
      val nbRow = SQL(
        """
          update IngredientProperty
          set name = {name}, description = {description}, unit_id = {unit_id}
          where id = {id}
        """
      ).on(
        'id -> id,
        'name -> elem.name,
        'description -> elem.description,
        'unit_id -> elem.unit_id
      ).executeUpdate()
      nbRow >= 1
    }
  }


  /**
   * Insert a new IngredientProperty.
   *
   * param : the object
   */
  def insert(elem: IngredientProperty): Option[IngredientProperty] = {
    DB.withConnection { implicit connection =>
      val id = SQL(
        """
          insert into IngredientProperty 
          ( id, name, description, unit_id)
          values (
            (select next value for IngredientProperty_seq),
            {name}, {description}, {unit_id}
          )
        """
      ).on(
          'name -> elem.name,
          'description -> elem.description,
          'unit_id -> elem.unit_id
      ).executeInsert()
      id map {x=> IngredientProperty(Id(x), elem.name, elem.description, elem.unit_id)}
    }
  }

  /**
   * Delete an IngredientProperty.
   *
   * @param name Id of the IngredientProperty to delete.
   */
  def delete(id: Long): Boolean = {
    val nbRow = DB.withConnection { implicit connection =>
      SQL("delete from IngredientProperty where id = {id}")
        .on('id -> id).executeUpdate()
    }
    nbRow >= 1
  }

  /**
   * Delete an IngredientProperty.
   *
   * @param elem IngredientProperty to delete.
   */
  def delete(elem: IngredientProperty): Boolean = {
    if (elem.id.isDefined)
      delete(elem.id.get)
    else false
  }
}
