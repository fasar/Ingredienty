package models

import anorm._

case class IngredientIngredientPropertyMap(ingredientId: Pk[Long],
                                           ingredientPropertyId: Pk[Long],
                                           value: String )

