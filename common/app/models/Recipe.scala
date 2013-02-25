package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

import play.api.Logger
import java.util.Date

import models.dao.{ IngredientDao, UserDao, IngredientRecipeQuantityDao }

case class Recipe(id: Pk[Long] = NotAssigned,
                  name: String,
                  instructions: String,
                  author: Option[User],
                  isPublic: Boolean,
                  description: String,
                  prepTimeSec: Int,
                  cookTimeSec: Int,
                  ingredients: List[(Ingredient, Double)])

object Recipe {
  def apply(id: Option[Long],
            name: String,
            instructions: String,
            authorStr: Option[String],
            isPublic: Boolean,
            description: String,
            prepTimeSec: Int,
            cookTimeSec: Int,
            ingredients: List[(Ingredient, Double)]): Recipe = {
    val author: Option[User] = authorStr.flatMap { x => UserDao.findByEmail(x) }
    id match {
      case Some(id) => new Recipe(Id(id), name, instructions, author, isPublic, description, prepTimeSec, cookTimeSec, ingredients)
      case None => new Recipe(NotAssigned, name, instructions, author, isPublic, description, prepTimeSec, cookTimeSec, ingredients)

    }
  }

  def apply(id: Pk[Long],
            name: String,
            instructions: String,
            authorStr: Option[String],
            isPublic: Boolean,
            description: String,
            prepTimeSec: Int,
            cookTimeSec: Int //ingredients: Map[Ingredient, Double]
            ): Recipe = {
    val author: Option[User] = authorStr.flatMap { x => UserDao.findByEmail(x) }
    val ingredientsQuantities = getIngredientsQuantities(id)
    new Recipe(id, name, instructions, author, isPublic, description, prepTimeSec, cookTimeSec, ingredientsQuantities)
  }

  private def getIngredientsQuantities(id: Pk[Long]): List[(Ingredient, Double)] = {
    val ingredientQuantities = IngredientRecipeQuantityDao.findByRecipeId(id.get)
    (for (
      ingredientQuantity <- ingredientQuantities;
      ingredientOpt = IngredientDao.findById(ingredientQuantity.ingredientId.get) if ingredientOpt.isDefined;
      ingredient <- ingredientOpt
    ) yield {
      val quantity = ingredientQuantity.quantity
      ingredient -> quantity
    })
  }

}
