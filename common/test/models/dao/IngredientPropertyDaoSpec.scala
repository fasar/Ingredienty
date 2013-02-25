package models.dao

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models.IngredientProperty
import anorm._

object IngredientPropertyDaoSpec extends Specification {

  "UnitDao" should {
    

    "create a new ingredient property without a key"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val elem:IngredientProperty = IngredientProperty(NotAssigned, "test", Some("TEST"),3)
      val res = IngredientPropertyDao.insert(elem)
      res.isDefined must beTrue 
      res.get.name must beEqualTo("test")
      res.get.id.get must beGreaterThan(1L)
    }
    "create a new ingredient property with a bad unit" in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val elem:IngredientProperty = IngredientProperty(NotAssigned, "test", Some("TEST"),99999)
      var error = false
      try {
	  val res = IngredientPropertyDao.insert(elem)
      } catch {
        case e:Exception => error = true 
      }
      error must beTrue
      
    }

    "retrive a ingredient property by Id" in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val elem = IngredientPropertyDao.findAll(0)
        elem.id.isDefined must beTrue 
    	elem.id.get must beGreaterThanOrEqualTo(1L)
        val elem2 = IngredientPropertyDao.findById(elem.id.get)
        elem2.isDefined must beTrue
        elem2.get.id.isDefined must beTrue
    	elem2.get.id.get must beEqualTo(elem.id.get)
    }
    
    "modify an ingredient property"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val elem = IngredientPropertyDao.findAll(0)
      val elemModified = IngredientProperty(elem.id, "test", Some("TEST"),3)
      elemModified.id.get must beGreaterThanOrEqualTo(1L)
      IngredientPropertyDao.update(elem.id.get, elemModified) must beTrue
    }
    
    "delete an ingredient property"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val elem = IngredientPropertyDao.findAll(0)
      val res:Boolean = IngredientPropertyDao.delete(elem)
      res must beTrue
    }
  }
}

