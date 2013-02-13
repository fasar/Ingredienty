package models

import javax.persistence.Table._
import javax.persistence._
import beans.BeanProperty


@Entity
@Table(name="IngredientFamily")
class IngredientFamily {
  @BeanProperty
  @Id
  @GeneratedValue
  var id: Long = 0

  @BeanProperty
  @Column(length = 50)
  var name:String = ""


  override def toString():String = {
    "IngredientFamily(" + name + ", #" + id + ")"
  }

  override def hashCode():Int = {
    (41*(41+name.hashCode()))
  }

  override def equals(e:Any):Boolean = {
    if(e != null && e.isInstanceOf[IngredientFamily]) {
      val other = e.asInstanceOf[IngredientFamily]
      (other canEqual this) &&
        other.name.equals(this.name)
    } else {
      false
    }
  }

  private def canEqual(other:Any):Boolean = {
    other.isInstanceOf[IngredientFamily]
  }
}