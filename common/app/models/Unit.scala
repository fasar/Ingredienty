package models

import anorm._


case class Unit(id:Pk[Long]= NotAssigned,
                name:String,
                nameAbrv:Option[String],
                plural:Option[String],
                pluralAbrv:Option[String])
                