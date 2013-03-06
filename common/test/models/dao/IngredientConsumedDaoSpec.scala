package models.dao

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models._
import anorm._
import java.util.Date
import java.text._

class IngredientConsumedDaoSpec extends Specification {

  "IngredientConsumedDao" should {
    
    "create a new ingredient consumed"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val user = UserDao.findAll(0)
      val Some(ingredient) = IngredientDao.findById(2002)
      val ingredientc= IngredientConsumed(user, (new Date), ingredient, 22.2, None)
      val res = IngredientConsumedDao.insert(ingredientc)
      res must beTrue 
      // res.get.user.email must beGreaterThanOrEqualTo(user.email)
    }

    "retrive consumed ingredients by email" in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val ingredientsc = IngredientConsumedDao.findByEmail("sample@sample.com")
      ingredientsc.size must beGreaterThanOrEqualTo(1)
    }
    "retrive consumed ingredients by email and date" in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss")
      val date = simpleDateFormat.parse("20130210-125328")
      val ingredientsc = IngredientConsumedDao.findByEmail("sample@sample.com", date)
      ingredientsc.size must beGreaterThanOrEqualTo(1)
    }
    
    "modify a ingredient consumed"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val ingredientsc = IngredientConsumedDao.findAll(0)
      val ingredientscModified = IngredientConsumed(ingredientsc.user, 
          ingredientsc.date, ingredientsc.ingredient, 1122, None)
      IngredientConsumedDao.update(ingredientscModified) must beTrue
    }
    
    "delete an ingredient"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val ingredientsc = IngredientConsumedDao.findAll(0)
      println(s"I will delete ${ingredientsc}")
      val res:Boolean = IngredientConsumedDao.delete(ingredientsc)
      res must beTrue
    }
  }
}