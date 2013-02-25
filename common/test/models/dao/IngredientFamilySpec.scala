package models.dao

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models.IngredientFamily
import anorm._

class IngredientFamilySpec extends Specification {

  "IngredientFamilyDao" should {
    "create a new family without a key"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val ingredientFam = IngredientFamily(NotAssigned, "Family1")
      val res = IngredientFamilyDao.insert(ingredientFam)
      res.isDefined must beTrue 
      res.get.id.get must beGreaterThanOrEqualTo(1L)
    }

    "retrive a family by Id" in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val ingredient = IngredientDao.findAll(0)
        ingredient.id.isDefined must beTrue 
    	ingredient.id.get must beGreaterThanOrEqualTo(1L)

        val family = IngredientFamilyDao.findAll(0)
        family.id.isDefined must beTrue 
    	family.id.get must beGreaterThanOrEqualTo(1L)
        val family2 = IngredientFamilyDao.findById(family.id.get)
        family2.isDefined must beTrue
        family2.get.id.isDefined must beTrue 
    	family2.get.id.get must beEqualTo(family.id.get)
    }
    
    "modify a family"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val elem = IngredientFamilyDao.findAll(0)
      val ingredientFamilyModified:IngredientFamily = IngredientFamily(elem.id, "AUTRE")
      ingredientFamilyModified.id.get must beGreaterThanOrEqualTo(1L)
      ingredientFamilyModified.name must beEqualTo("AUTRE")
      IngredientFamilyDao.update(elem.id.get, ingredientFamilyModified) must beTrue
    }
    
    "delete a family"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val elem = IngredientDao.findAll(0)
      val res:Boolean = IngredientDao.delete(elem)
      res must beTrue
    }
  }
}