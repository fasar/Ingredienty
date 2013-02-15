package models

import anorm._

import dao.{IngredientPropertyDao, IngredientIngredientPropertyMapDao, IngredientFamilyDao}

import play.api.Logger


case class Ingredient(id: Pk[Long] = NotAssigned,
                      name:String,
                      family_id: Option[Long]) {

  private val log = Logger(classOf[Ingredient])

  lazy val family:Option[IngredientFamily] = {
    IngredientFamilyDao.findById(family_id.get)
  }
  lazy val familyStr:String = {
    if (family.isDefined) {
      family.get.name
    } else ""
  }

  lazy val ingredientProperties:Map[String, String] = {
    val properties = IngredientIngredientPropertyMapDao.findByIngredient(this)
    val res = properties.map {elem =>
      val propertyName =
        if(elem.ingredientPropertyId.isDefined) {
          val ingredientProperty = IngredientPropertyDao.findById(elem.ingredientPropertyId.get)
          val propertyNameOpt = ingredientProperty.map{_.name.get}
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
}


case class IngredientFamily(id: Pk[Long] = NotAssigned,
                            name: String)


case class IngredientProperty(name: Pk[String],
                              description: Option[String],
                              unit_id:Long)


case class Unit(id:Pk[Long]= NotAssigned,
                name:String,
                nameAbrv:Option[String],
                plural:Option[String],
                pluralAbrv:Option[String])


case class IngredientIngredientPropertyMap(ingredientId: Pk[Long],
                                           ingredientPropertyId: Pk[Long],
                                           value: String )
