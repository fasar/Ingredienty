package models.dao

import play.api.Play.current

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import models.{Ingredient, IngredientIngredientPropertyMap}

/**
 * Created with IntelliJ IDEA.
 * User: fabien
 * Date: 14/02/13
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
object IngredientIngredientPropertyMapDao {
  /**
   * Parse an IngredientIngredientPropertyMap from a ResultSet
   */
  private val simple = {
    get[Pk[Long]]("ingredient_id") ~
      get[String]("ingredientProperties")~
      get[Pk[Long]]("ingredientProperties_KEY") map {
      case idIng~value~idIngPp => IngredientIngredientPropertyMap(idIng, idIngPp, value)
    }
  }

  /**
   * Retrieve an IngredientIngredientPropertyMap from the Ingredient.
   */
  def findByIngredient(elem: Ingredient): List[IngredientIngredientPropertyMap] = {
    if (elem.id.isDefined)
      findByIdIngredient(elem.id.get)
    else Nil
  }

  /**
   * Retrieve an IngredientIngredientPropertyMap from the id.
   */
  def findByIdIngredient(idIngredient: Long): List[IngredientIngredientPropertyMap] = {
    DB.withConnection { implicit connection =>
      SQL("""select * from Ingredient_IngredientProperty_Map
             where Ingredient_id = {ingredientId}""")
        .on('ingredientId -> idIngredient)
        .as(simple *)
    }
  }

  /**
   * Update a computer.
   * Param id, Object
   */
  def update(elem: IngredientIngredientPropertyMap) = {
    if (elem.ingredientId.isDefined && elem.ingredientPropertyId.isDefined) {
      DB.withConnection { implicit connection =>
        SQL(
          """
            update Ingredient_IngredientProperty_Map
            set ingredientProperties = {value}
            where Ingredient_id = {idIng}
             and  ingredientProperties_KEY = {idIngPP}
          """
        ).on(
          'idIng -> elem.ingredientId.get,
          'idIngPP -> elem.ingredientPropertyId.get,
          'value -> elem.value
        ).executeUpdate()
      }
    }
  }


  /**
   * Insert a new IngredientIngredientPropertyMap.
   *
   * param : the object
   */
  def insert(elem: IngredientIngredientPropertyMap) = {
    if (elem.ingredientId.isDefined && elem.ingredientPropertyId.isDefined) {
      DB.withConnection { implicit connection =>
        SQL(
          """
            insert into Ingredient_IngredientProperty_Map values (
             Ingredient_id = {idIng}, ingredientProperties = {value}, ingredientProperties_KEY = {idIngPP}
            )
          """
        ).on(
          'idIng -> elem.ingredientId.get,
          'idIngPP -> elem.ingredientPropertyId.get,
          'value -> elem.value
        ).executeUpdate()
      }
    }
  }

  /**
   * Delete an IngredientIngredientPropertyMap.
   *
   * @param ingredientId Id of the ingredientId
   * @param ingredientPropertyId  Id of the ingredientPropertyId
   */
  def delete(ingredientId: Long, ingredientPropertyId: Long) = {
    DB.withConnection { implicit connection =>
      SQL(
        """delete from Ingredient_IngredientProperty_Map
          | where Ingredient_id = {idIng}
          | and ingredientProperties_KEY = {idIngPP}"""
      ).on(
        'idIng -> ingredientId,
        'idIngPP -> ingredientPropertyId
      ).executeUpdate()
    }
  }

  /**
   * Delete an IngredientIngredientPropertyMap.
   *
   * @param elem IngredientIngredientPropertyMap to delete.
   */
  def delete(elem: IngredientIngredientPropertyMap) {
    if (elem.ingredientId.isDefined && elem.ingredientPropertyId.isDefined)
      delete(elem.ingredientId.get, elem.ingredientPropertyId.get)
  }
}
