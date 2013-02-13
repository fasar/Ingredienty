package models

import javax.persistence._
import beans.BeanProperty

@Entity
class Ingredient() {
  @BeanProperty
  @Id
  @GeneratedValue
  var id: Long = 0

  @BeanProperty
  @Column(length=120)
  var name:String = ""

  @BeanProperty
  @OneToOne
  @JoinColumn(name="ingredientFamily_id")
  var family:IngredientFamily = null;

  //@OneToMany
  //@JoinTable(name="Ingredient_IngredientProperty_Map")
  //@MapKeyColumn(name="IgrendientProperty_Id_Key")

  //@ElementCollection(fetch=FetchType.EAGER)
  //  @MapKey(name = "language")


  @BeanProperty
  @ElementCollection
  //@MapKeyColumn(name = "pipo", length=20)
  //@MapKey(name="pipo")
  @Column(length=25)
  @JoinTable(name="Ingredient_IngredientProperty_Map")
  var ingredientProperties:java.util.Map[IngredientProperty, java.lang.String] = null //new java.util.HashMap[IngredientProperty, Double]



  override def toString():String = {
    "Ingredient(" + name + ", " + family.toString + ", #" + id + ")"
  }

  override def hashCode():Int = {
    val ingredientPropertiesHash = if(ingredientProperties!=null) {ingredientProperties.hashCode()} else {0}
    41*(41*(41+name.hashCode())+ family.hashCode() ) + ingredientPropertiesHash
  }

  override def equals(e:Any):Boolean = {
    if(e != null && e.isInstanceOf[Ingredient]) {
      val other = e.asInstanceOf[Ingredient]
      (other canEqual this) &&
        other.name.equals(this.name) &&
        other.family.equals(this.family)
    } else {
      false
    }
  }

  private def canEqual(other:Any):Boolean = {
    other.isInstanceOf[Ingredient]
  }
}