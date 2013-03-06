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



	          
object WeightDao {
  private val log = Logger(WeightDao.getClass)

  /**
   * Parse an WeightDao from a ResultSet
   * 
   * It is useful for anorm parsing
   */
  private val simple = {
      get[Pk[Long]]("id") ~
      get[String]("email") ~
      get[Date]("date") ~
      get[Double]("weight") ~
      get[Double]("fat") ~
      get[Double]("water") ~ 
      get[Double]("muscles") ~
      get[Double]("bones") ~
      get[Int]("visceralFat")  map {
         case id~email~date~weight~fat~water~muscles~bones~visceralFat =>
           val userOpt = UserDao.findByEmail(email)
           new Weight(id, userOpt.get, date, weight, fat, water, muscles, bones, visceralFat)
    }
  }

  /**
   * Retrieve all Ingredient from the id.
   */
  def findAll:List[Weight] = {
    DB.withConnection { implicit connection =>
      val res = SQL("select id, email, date, weight, fat, water, " +
      		"muscles, bones, visceralFat from Weight").as(simple *)
      log.debug("get all weight : " + res.size)
      res
    }
  }


  /**
   * Retrieve a Weight from the id.
   */
  def findById(id: Long): Option[Weight] = {
    DB.withConnection { implicit connection =>
      SQL("select id, email, date, weight, fat, water, " +
      		"muscles, bones, visceralFat from Weight"+
      		"where id = {id}")
        .on('id -> id)
        .as(simple.singleOpt)
    }
  }

  /**
   * Retrieve an Weight from the email.
   */
  def findByUser(email: String): List[Weight] = {
    DB.withConnection { implicit connection =>
      SQL("select id, email, date, weight, fat, water, " +
      		"muscles, bones, visceralFat from Weight "+
      		"where email = {email}")
        .on('email -> email)
        .as(simple *)
    }
  }
  

  /**
   * Insert a new Weight.
   *
   * param : the object
   */

  def insert(elem: Weight):Option[Weight] = {
    DB.withConnection { implicit connection =>
      val res = SQL(
        """
          insert into Weight 
          (email, date, weight, fat, water,
      		"muscles, bones, visceralFat)
          values (
            {ingredient_id}, {date}, {email}, {cdate},
            {recipe_id}, {quantity}
          )
        """
      ).on(
        'email -> elem.user.email,
        'date -> elem.date,
        'weight -> elem.weight,
        'fat -> elem.fat,
        'water -> elem.water,
        'muscles -> elem.muscles,
        'bones -> elem.bones,
        'visceralFat -> elem.visceralFat
      ).executeInsert()
      res map {x:Long =>
        val idPk:Pk[Long] = Id(x)
        Weight( idPk, elem.user, elem.date, elem.weight, 
               elem.fat, elem.water, elem.muscles, elem.bones, elem.visceralFat)}
    }
  }

  /**
   * Delete an object.
   *
   * @param id Id of the Ingredient to delete.
   */
  def delete(elem: Weight):Boolean = {
    delete(elem.id.get)
  }

  /**
   * Delete an object.
   *
   * @param id Id of the Ingredient to delete.
   */
  def delete(id:Long):Boolean = {
    log.debug(s"delete weight id $id")
    DB.withConnection { implicit connection =>
      val sql = SQL("""
          delete from Weight
          where id = {id}
          """
      )
      
      val nbRow = sql.on(
          'id -> id
      ).executeUpdate()
      nbRow == 1
    }
  }
}