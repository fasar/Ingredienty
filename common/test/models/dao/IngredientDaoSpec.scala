package models.dao

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models.Ingredient
import anorm._
import org.hibernate.validator.constraints.NotEmpty

class IngredientDaoSpec extends Specification {

  "IngredientDao" should {
    

    "create a new ingredient without a key"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val ingredient= Ingredient(NotAssigned, "Carottes", None)
      val res = IngredientDao.insert(ingredient)
      res.isDefined must beTrue 
      res.get.id.get must beGreaterThanOrEqualTo(1L)
    }

    "retrive an ingredient by Id" in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val ingredient = IngredientDao.findAll(0)
        ingredient.id.isDefined must beTrue 
    	ingredient.id.get must beGreaterThanOrEqualTo(1L)
        val ingredient2 = IngredientDao.findById(ingredient.id.get)
        ingredient2.isDefined must beTrue
        ingredient2.get.id.isDefined must beTrue
    	ingredient2.get.id.get must beEqualTo(ingredient.id.get)
    }
    
    "modify an ingredient"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val ingredient = IngredientDao.findAll(0)
      val ingredientModified:Ingredient = Ingredient(ingredient.id, "AUTRE", ingredient.familyId)
      ingredientModified.id.get must beGreaterThanOrEqualTo(1L)
      ingredientModified.name must beEqualTo("AUTRE")
    }
    
    "delete an ingredient"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val ingredient = IngredientDao.findAll(0)
      val res:Boolean = IngredientDao.delete(ingredient)
      res must beTrue
    }
  }
}