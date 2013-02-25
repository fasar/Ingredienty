package models


object Role extends Enumeration {
	type Role = Value
	val User = Value(100)
	val Admin = Value(300)
}
