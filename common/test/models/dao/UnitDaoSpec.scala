package models.dao

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models.Unit
import anorm._

object UnitDaoSpec extends Specification {

  "UnitDao" should {
    

    "create a new unit without a key"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val elem:Unit = Unit(NotAssigned, "tttt", Some("t"), Some("tttts"), Some("ts"))
      val res = UnitDao.insert(elem)
      res.isDefined must beTrue 
      res.get.id.get must beGreaterThanOrEqualTo(1L)
    }

    "retrive a unit by Id" in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val elem = UnitDao.findAll(0)
        elem.id.isDefined must beTrue 
    	elem.id.get must beGreaterThanOrEqualTo(1L)
        val elem2 = UnitDao.findById(elem.id.get)
        elem2.isDefined must beTrue
        elem2.get.id.isDefined must beTrue
    	elem2.get.id.get must beEqualTo(elem.id.get)
    }
    
    "modify a unit"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val elem = UnitDao.findAll(0)
      val elemModified:Unit = Unit(elem.id, "tttt", Some("t"), Some("tttts"), Some("ts"))
      elemModified.id.get must beGreaterThanOrEqualTo(1L)
      UnitDao.update(elem.id.get, elemModified) must beTrue
    }
    
    "delete a unit"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val elem = UnitDao.findAll(0)
      val res:Boolean = UnitDao.delete(elem)
      res must beTrue
    }
  }
}