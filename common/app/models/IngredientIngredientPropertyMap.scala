package models

import anorm._

case class IngredientIngredientPropertyMap(ingredientId: Pk[Long],
                                           ingredientPropertyId: Pk[Long],
                                           value: String ) {

  def +(b:IngredientIngredientPropertyMap): IngredientIngredientPropertyMap = {
    if(this.ingredientId != b.ingredientId) {
      throw new IllegalArgumentException(s"arguement doesn't have the same ingredientId: ${this} != ${b}")
    }
    if(this.ingredientPropertyId != b.ingredientPropertyId ) {
      throw new IllegalArgumentException(s"arguement doesn't have the same ingredientPropertyId: ${this} != ${b}")
    }

    val dValueThis:Double = parseDouble(this.value).getOrElse(0)
    val dValueB:Double = parseDouble(b.value).getOrElse(0)

    val addition:Double = ( dValueB + dValueThis )
    val res = IngredientIngredientPropertyMap(this.ingredientId, this.ingredientPropertyId, addition.toString)
    res
  }

  def parseDouble(s:String): Option[Double] = {
    try {
      Option(s.toDouble)
    } catch {
      case _ => None
    }
  }
}

