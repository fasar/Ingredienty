package views

import scala.Enumeration;


class EnumNavRub  {
}

object EnumNavRub extends Enumeration {
  type EnumNavRub = Value
  val MAIN, RECIPES, INGREDIENTS, WEIGHT = Value
}

