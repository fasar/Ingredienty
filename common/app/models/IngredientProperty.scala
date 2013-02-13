package models

import javax.persistence.Table._
import beans.BeanProperty
import javax.persistence._

/**
 * 
 * User: fabien
 * Date: 02/02/12
 * Time: 15:05
 * 
 */
@Entity
@Table(name="IngredientProperty")
class IngredientProperty() {
  @BeanProperty
  @Id
  @GeneratedValue
  var id: Long = 0

  @BeanProperty
  @Column(length=50)
  var name:String = ""

  @BeanProperty
  @Column(length=500)
  var description:String = ""

  @BeanProperty
  @OneToOne
  @JoinColumn(name="unit_id")
  var unit:Unit = null

  override def toString():String = {
    "IngredientProperty(" + name + ", #" + id + ")"
  }

  override def hashCode():Int = {
    val unitHash:Int = if(unit!=null) {unit.hashCode()} else{0}
    val dscptHash:Int = if(description!=null) {description.hashCode()} else{0}
   (41*(41*(41+name.hashCode())+dscptHash) + unitHash )
  }

  override def equals(e:Any):Boolean = {
    if(e != null && e.isInstanceOf[IngredientProperty]) {
      val other = e.asInstanceOf[IngredientProperty]
      (other canEqual this) &&
        other.name.equals(this.name)
    } else {
      false
    }
  }

  private def canEqual(other:Any):Boolean = {
    other.isInstanceOf[IngredientProperty]
  }
}
