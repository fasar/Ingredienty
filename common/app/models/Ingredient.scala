package models

import anorm._

import dao.{IngredientPropertyDao, IngredientIngredientPropertyMapDao, IngredientFamilyDao}

import play.api.Logger


import play.api.libs.json._
// you need this import to have combinators
import play.api.libs.functional.syntax._


case class Ingredient(id: Pk[Long] = NotAssigned,
                      name:String,
                      familyId: Option[Long]) {

  private val log = Logger(classOf[Ingredient])

  def apply(property:String) : Option[String] = {
    properties.get(property)
  }
  
  lazy val family:Option[IngredientFamily] = {
    IngredientFamilyDao.findById(familyId.get)
  }
  
  lazy val familyStr:String = {
    if (family.isDefined) {
      family.get.name
    } else ""
  }

  
  
  lazy val properties:Map[String, String] = {
    val properties = IngredientIngredientPropertyMapDao.findByIngredient(this)
    val res = properties.map {elem =>
      val propertyName =
        if(elem.ingredientPropertyId.isDefined) {
          val ingredientProperty = IngredientPropertyDao.findById(elem.ingredientPropertyId.get)
          val propertyNameOpt = ingredientProperty.map{_.name}
          propertyNameOpt.getOrElse {
            log.error("can't found the good propertyId \""+elem+"\" of ingredient :" + this)
            "PropertyNameNotFound"
          }
        } else {
          log.error("can't found the good propertyId \""+elem+"\" of ingredient :" + this)
          "PropertyNotFound"
        }
      (propertyName -> elem.value)
    }

    res.toMap
  }
  
  def glucides: Option[Double] = {
    try {
      val elOpt = properties.get("Glucides")
      elOpt map { x => x.toDouble }
    } catch {
      case e:Exception => None
    }
  }
  def lipides: Option[Double] = {
    try {
      val elOpt = properties.get("Lipides")
      elOpt map { x => x.toDouble }
    } catch {
      case e:Exception => None
    }
  }
  def proteines: Option[Double] = {
    try {
      val elOpt = properties.get("Protéines")
      elOpt map { x => x.toDouble }
    } catch {
      case e:Exception => None
    }
  }
}
