package models.website

import scala.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: fabien
 * Date: 31/08/13
 * Time: 00:47
 * To change this template use File | Settings | File Templates.
 */
object EnumNavRub extends Enumeration {
  type EnumNavRub = Value
  val MAIN, RECIPES, INGREDIENTS, WEIGHT = Value
}
