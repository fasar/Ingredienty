package models

import anorm._


case class IngredientFamily(id: Pk[Long] = NotAssigned,
                            name: String)
                            
      