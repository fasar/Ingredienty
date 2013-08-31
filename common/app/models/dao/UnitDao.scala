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
   * Retrieve all unit from the id.
   */
  def mapUnit:Map[Long, Unit] = {
    val units: List[Unit] = findAll
    val res = (for(unit <- units) yield {
      (unit.id.get, unit)
    }).toMap
    res
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
  def update(id:Long, elem: Unit): Boolean = {
    DB.withConnection { implicit connection =>
      val nbRow = SQL(
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
      nbRow >= 1
    }
  }


  /**
   * Insert a new unit.
   *
   * param : the object
   */
  def insert(elem: Unit): Option[Unit] = {
    DB.withConnection { implicit connection =>
      val id = SQL(
        """
          insert into unit 
          (id, name, name_abrv, plural, plural_abrv, cunit_type)
          values (
            (select next value for Unit_seq),
            {name}, {name_abrv}, {plural}, {plural_abrv}, {cunit_type}
          )
        """
      ).on(
        'name -> elem.name,
        'name_abrv -> elem.nameAbrv,
        'plural -> elem.plural,
        'plural_abrv -> elem.pluralAbrv,
        'cunit_type -> 999
      ).executeInsert()
      id map {x => Unit(Id(x), elem.name, elem.nameAbrv, elem.plural, elem.pluralAbrv)}
    }
  }

  /**
   * Delete an unit.
   *
   * @param id Id of the unit to delete.
   */
  def delete(id: Long): Boolean = {
    DB.withConnection { implicit connection =>
      val nbRow = SQL("delete from unit where id = {id}")
        .on('id -> id).executeUpdate()
      nbRow >= 1
    }
  }

  /**
   * Delete an unit.
   *
   * @param elem unit to delete.
   */
  def delete(elem: Unit): Boolean = {
    if (elem.id.isDefined)
      delete(elem.id.get)
    else false
  }
}