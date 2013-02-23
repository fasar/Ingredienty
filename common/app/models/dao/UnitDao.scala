package models.dao

import play.api.Play.current
import anorm.SqlParser._
import anorm._
import play.api.db.DB
import anorm.~
import models.Unit

object UnitDao {
  /**
   * Parse an Unit from a ResultSet
   */
  private val simple = {
    get[Pk[Long]]("id") ~
      get[String]("name") ~
      get[Option[String]]("name_abrv") ~
      get[Option[String]]("plural") ~
      get[Option[String]]("plural_abrv") map {
      case id~name~nameAbrv~plural~pluralAbrv => Unit(id, name, nameAbrv, plural, pluralAbrv)
    }
  }


  /**
   * Retrieve all unit from the id.
   */
  def findAll:List[Unit] = {
    DB.withConnection { implicit connection =>
      SQL("select * from unit").as(simple *)
    }
  }

  /**
   * Retrieve an unit from the id.
   */
  def findById(id: Long): Option[Unit] = {
    DB.withConnection { implicit connection =>
      SQL("select * from unit where id = {id}")
        .on('id -> id)
        .as(simple.singleOpt)
    }
  }

  /**
   * Update a Unit.
   * Param id, Object
   */
  def update(id:Long, elem: Unit) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          update unit
          set name = {name}, name_abrv = {name_abrv}, plural = {plural}, plural_abrv = {plural_abrv}
          where id = {id}
        """
      ).on(
        'id -> id,
        'name -> elem.name,
        'name_abrv -> elem.nameAbrv,
        'plural -> elem.plural,
        'plural_abrv -> elem.pluralAbrv
      ).executeUpdate()
    }
  }


  /**
   * Insert a new unit.
   *
   * param : the object
   */
  def insert(elem: Unit) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into unit values (
            (select next value for computer_seq),
            {name}, {name_abrv}, {plural}, {plural_abrv}
          )
        """
      ).on(
        'name -> elem.name,
        'name_abrv -> elem.nameAbrv,
        'plural -> elem.plural,
        'plural_abrv -> elem.pluralAbrv
      ).executeUpdate()
    }
  }

  /**
   * Delete an unit.
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
   * Delete an unit.
   *
   * @param elem unit to delete.
   */
  def delete(elem: Unit) {
    if (elem.id.isDefined)
      delete(elem.id.get)
  }
}