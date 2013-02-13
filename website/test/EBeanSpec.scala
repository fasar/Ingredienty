package test

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, FakeApplication}
import play.api.test.Helpers._

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.config.GlobalProperties
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


}
