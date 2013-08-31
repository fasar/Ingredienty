package business.ingredients.properties

import java.util.Date
import models._
import models.dao.{IngredientPropertyDao, IngredientIngredientPropertyMapDao, IngredientConsumedDao}
import models.IngredientIngredientPropertyMap
import models.IngredientProperty
import scala.collection.mutable

/**
 * Created with IntelliJ IDEA.
 * User: fabien
 * Date: 31/08/13
 * Time: 19:03
 * To change this template use File | Settings | File Templates.
 */
object UtilsIngredients {

  def ???():Nothing = {
    throw new Exception("not implemented")
  }

  def calcApport(user:User, date:Date):Map[IngredientProperty, IngredientIngredientPropertyMap] = {
    val apportsByIngredients =  getApportsByIngredients(user, date)
    val resList:List[(IngredientProperty, IngredientIngredientPropertyMap)] =
      (for((ing, apport) <- apportsByIngredients;
           intPpt <- apport ) yield {
        val ingPpt: IngredientProperty = intPpt._1
        val ingIngPptMap: IngredientIngredientPropertyMap = intPpt._2
        (ingPpt, ingIngPptMap)
      }).toList
    val res = new mutable.HashMap[IngredientProperty, IngredientIngredientPropertyMap]()
    for( (ingPpt, ingIngPpt) <- resList) {
      val count = res.get(ingPpt).getOrElse(IngredientIngredientPropertyMap(ingIngPpt.ingredientId, ingIngPpt.ingredientPropertyId, "0"))
      res.put(ingPpt, ingIngPpt + count)
    }
    val resImmutable = collection.immutable.HashMap() ++ res
    return resImmutable
  }

  def getApportsByIngredients(user:User, date:Date): Map[IngredientConsumed, Map[IngredientProperty, IngredientIngredientPropertyMap]] = {
    //prepare consumed ingredient and its properties
    val consumed = IngredientConsumedDao.findByEmail(user.email, date).sortBy{_.date}
    val properties:Map[IngredientConsumed, Map[IngredientProperty, IngredientIngredientPropertyMap]] =
      (for(ing <- consumed ) yield {
        val propertiesValue = IngredientIngredientPropertyMapDao.findByIngredient(ing.ingredient);
        val propertiesMaps:Map[IngredientProperty, IngredientIngredientPropertyMap] =
          (for(propertyValue <- propertiesValue;
               propertyObj = IngredientPropertyDao.findById(propertyValue.ingredientPropertyId.get)
               if(propertyObj.isDefined)
          ) yield {
            Pair(propertyObj.get, propertyValue)
          }).toMap

        Pair(ing, propertiesMaps)
      }).toMap
    properties
  }
}
