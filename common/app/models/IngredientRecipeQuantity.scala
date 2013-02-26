package models

import anorm._

case class IngredientRecipeQuantity(
  recipeId: Pk[Long],
  ingredientId: Pk[Long],
  quantity: Double);
