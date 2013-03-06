package models

import anorm._
import java.util.Date

case class Weight(id:Pk[Long],
		  user:User,
		  date: Date,
	          weight: Double,
	          fat: Double,
	          water: Double,
	          muscles: Double,
	          bones: Double,
	          visceralFat:Int)
	          
	          