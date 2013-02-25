package models.dao

import play.api.Play.current
import anorm.SqlParser._
import anorm._
import play.api.db.DB
import models.User

object UserDao {

    // -- Parsers

  /**
   * Parse a User from a ResultSet
   */
  private val simple = {
    get[String]("user.email") ~
      get[String]("user.name") ~
      get[String]("user.password") ~
      get[Int]("user.role") map {
      case email~name~password~role => User(email, name, password, role)
    }
  }

  // -- Queries

  /**
   * Retrieve a User from email.
   */
  def findByEmail(email: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user where email = {email}").on(
        'email -> email
      ).as(this.simple.singleOpt)
    }
  }

  /**
   * Retrieve all users.
   */
  def findAll: Seq[User] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user").as(this.simple *)
    }
  }

  /**
   * Authenticate a User.
   */
  def authenticate(email: String, password: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
         select * from user where
         email = {email} and password = {password}
        """
      ).on(
        'email -> email,
        'password -> password
      ).as(this.simple.singleOpt)
    }
  }

  /**
   * Create a User.
   */
  def insert(user: User): Option[User] = {
    DB.withConnection { implicit connection =>
      val nbRow = SQL(
        """
          insert into user values (
            {email}, {name}, {password}, {role}
          )
        """
      ).on(
        'email -> user.email,
        'name -> user.name,
        'password -> user.password,
        'role -> user.role.id
      ).executeUpdate()

      if(nbRow >=1) Some(user)
      else None

    }
  }

  /**
   * Update a user
   */
  def update(user:User): Boolean = {
    DB.withConnection { implicit connection =>
      val nbRow:Int = SQL(
        """
          update User
          set name = {name}, password = {password}, role = {role}
          where email = {email}
        """
      ).on(
        'email -> user.email,
        'name -> user.name,
        'password -> user.password,
        'role -> user.role.id
      ).executeUpdate()
      nbRow == 1
    }

  }
  

  /**
   * Delete a User.
   *
   * @param email Id of the user to delete.
   */
  def delete(email: String): Boolean = {
    DB.withConnection { implicit connection =>
      val nbRow = SQL("delete from User where email = {email}")
       .on('email -> email).executeUpdate()
      nbRow >= 1
    }
  }

  /**
   * Delete a User.
   *
   * @param user User to delete.
   */
  def delete(user: User): Boolean  = {
    delete(user.email)
  }

}