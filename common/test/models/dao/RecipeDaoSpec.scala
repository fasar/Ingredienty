package models.dao

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models._
import models.dao._
import anorm._

object RecipeDaoSpec extends Specification {
  private object IngredientPrivateSpec {
    val ingredients = List(
      (IngredientDao.findById(2000).get, 14.3),
      (IngredientDao.findById(2002).get, 14.3),
      (IngredientDao.findById(2004).get, 14.3))
    val invalidIngredients = List(
      (IngredientDao.findById(2000).get, 14.3),
      (IngredientDao.findById(2002).get, 14.3),
      (Ingredient(NotAssigned, "ingredientError", None), 33.3),
      (IngredientDao.findById(2004).get, 14.3))

  }
          
  "RecipeDao" should {
    
        
    "create a new recipe without a key"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val elem:Recipe = Recipe(NotAssigned, "name", "inst", None, true, 
	      		 	"dsc", 10, 10, IngredientPrivateSpec.ingredients)
      val res = RecipeDao.insert(elem)
      res.isDefined must beTrue 
      res.get.id.get must beGreaterThanOrEqualTo(1L)
    }
    "not create a new recipe with an ingredient not in db"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val elem:Recipe = Recipe(NotAssigned, "name", "inst", None, true, 
	      		 	"dsc", 10, 10, IngredientPrivateSpec.invalidIngredients)
      val res = RecipeDao.insert(elem)
      res.isDefined must beFalse 
    }
    "retrive a recipe by Id" in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val elem = RecipeDao.findAll(0)
        elem.id.isDefined must beTrue 
    	elem.id.get must beGreaterThanOrEqualTo(1L)
        val elem2 = RecipeDao.findById(elem.id.get)
        elem2.isDefined must beTrue
        elem2.get.id.isDefined must beTrue
    	elem2.get.id.get must beEqualTo(elem.id.get)
    }
    
    "modify a recipe with no ingredients"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val elem = RecipeDao.findAll(0)
      val elemModified:Recipe = Recipe(NotAssigned, "name", "inst", None, true, 
	      		 	"dsc", 10, 10, List.empty)
      RecipeDao.update(elem.id.get, elemModified) must beTrue
    }
    
    "modify a recipe with ingredients"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val elem = RecipeDao.findAll(0)
      val elemModified:Recipe = Recipe(NotAssigned, "name", "inst", None, true, 
	      		 	"dsc", 10, 10, IngredientPrivateSpec.ingredients)
      RecipeDao.update(elem.id.get, elemModified) must beTrue
    }
    
    "modify a recipe with bad ingredients"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val elem = RecipeDao.findAll(0)
      val elemModified:Recipe = Recipe(NotAssigned, "name", "inst", None, true, 
	      		 	"dsc", 10, 10, IngredientPrivateSpec.invalidIngredients)
      RecipeDao.update(elem.id.get, elemModified) must beFalse
    }
    
    "delete a recipe"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val elem = RecipeDao.findAll(0)
      val res:Boolean = RecipeDao.delete(elem)
      res must beTrue
    }
  }
}