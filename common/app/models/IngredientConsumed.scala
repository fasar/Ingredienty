package models

import java.util.Date

case class IngredientConsumed(user:User, date:Date, 
    ingredient:Ingredient, quantity:Double, recipe:Option[Recipe]) 
