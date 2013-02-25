package models

case class User(email: String, name: String, password: String, role: Role.Role)

object User {
  private type ThisType = User
  
  
  def apply(email: String, name: String, password: String, role: Int): User = {
    apply(email, name, password, Role(role))
  }
  
}
