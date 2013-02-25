package models

import anorm._


case class IngredientProperty(id: Pk[Long],
			      name: String,
                              description: Option[String],
                              unit_id:Long)

