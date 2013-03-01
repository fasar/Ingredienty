package models

case class User(email: String, name: String, password: String, role: Role.Role) {
  
  override def toString = s"User($email, $name, xxx, ${role.toString})"
}

object User {
  private type ThisType = User
  
  
  def apply(email: String, name: String, password: String, role: Int): User = {
    apply(email, name, password, Role(role))
  }
  
}
