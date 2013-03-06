package models.dao

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models.Unit
import anorm._

object WeightDaoSpec extends Specification {

  "WeightDao" should {
    "be implemented"  in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      1 must beEqualTo(2)
    }
  }
}