package models


import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

object Role extends Enumeration {
	type Role = Value
	val User = Value(100)
	val Admin = Value(300)
}


case class User(email: String, name: String, password: String, role: Role.Role)

object User {
  private type ThisType = User
  
  
  def apply(email: String, name: String, password: String, role: Int): User = {
    apply(email, name, password, Role(role))
  }
  
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
  def findByEmail(email: String): Option[ThisType] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user where email = {email}").on(
        'email -> email
      ).as(this.simple.singleOpt)
    }
  }

  /**
   * Retrieve all users.
   */
  def findAll: Seq[ThisType] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user").as(this.simple *)
    }
  }

  /**
   * Authenticate a User.
   */
  def authenticate(email: String, password: String): Option[ThisType] = {
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
  def create(user: ThisType): ThisType = {
    DB.withConnection { implicit connection =>
      SQL(
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

      user

    }
  }


  /**
   * Delete a User.
   *
   * @param email Id of the user to delete.
   */
  def delete(email: String) = {
    DB.withConnection { implicit connection =>
      SQL("delete from User where email = {email}")
        .on('email -> email).executeUpdate()
    }
  }

  /**
   * Delete a User.
   *
   * @param user User to delete.
   */
  def delete(user: User) {
    delete(user.email)
  }
}
