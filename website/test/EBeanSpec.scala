package test

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, FakeApplication}
import play.api.test.Helpers._

import models._

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow
import models.old.UnitType
;
//import com.avaje.ebean.config.GlobalProperties
import java.lang.String



class EBeanSpec extends Specification {

  "H2 database" should {
    "contain many ingredients" in {
      val sql:java.lang.String = "select count(*) as count from Ingredient";
      val row:SqlRow = Ebean.createSqlQuery(sql).findUnique();

      val i:Integer = row.getInteger("count");

      System.out.println("Got "+i+"  - DataSource good.");
      i.toInt must be_==(1353)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }

  def unitTrans(func: => scala.Unit) {
    Ebean.beginTransaction()
    try {
      func
      Ebean.commitTransaction()
    } catch {
      case e:Exception =>  Ebean.rollbackTransaction()
    }
    Ebean.endTransaction()
  }

  "EBean" should {
    val unit:old.Unit = new old.Unit
    unit.setName("testUnit")
    unit.setPlural("testUnits")
    unit.setCunitType(UnitType.Other.id)

    "create some Units" in {
      unitTrans(Ebean.save(unit))
    }
    "select a Units" in {
      val u2 = Ebean.find(classOf[old.Unit], unit.getId());
      println("Mon id  est : " + u2.getId)
      u2 must not beNull
    }
    "update some Units" in {
      unit.setName("testUnit2")
      unit.setPlural("testUnits2")
      unitTrans( Ebean.update(unit) )
    }
    "delete some Units" in {
      Ebean.beginTransaction()
      unitTrans( Ebean.delete(unit) )
    }

  }


}
