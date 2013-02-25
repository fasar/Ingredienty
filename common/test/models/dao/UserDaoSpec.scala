package models.dao


import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models.{User, Role}
import anorm._

class UserDaoSpec extends Specification {

  "UserDao" should {
    

    "create a new user without a key"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val user = User("test@test.com", "test", "secret", Role.Admin)
      val res = UserDao.insert(user)
      res.isDefined must beTrue 
      res.get.email must beEqualTo("test@test.com")
    }

    "retrive a user by email" in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val user = UserDao.findAll(0)
        user.email must not beNull
        val email = user.email
        val user2 = UserDao.findByEmail(email)
        user2.isDefined must beTrue
        user2.get.email must beEqualTo(email)
    	user2.get.email must beEqualTo(email)
    }
    
    "modify a user"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val user = UserDao.findAll(0)
      val userModified = User(user.email, "Autre", "ultrasecret", user.role)
      userModified.email must beEqualTo(user.email)
      UserDao.update(userModified)
    }
    
    "delete a user"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val user = UserDao.findAll(0)
      val res:Boolean = UserDao.delete(user)
      res must beTrue
    }
  }
}
