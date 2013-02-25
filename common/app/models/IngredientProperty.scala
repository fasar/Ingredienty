package models

import anorm._


case class IngredientProperty(name: Pk[String],
                              description: Option[String],
                              unit_id:Long)

