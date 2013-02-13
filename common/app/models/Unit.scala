package models

import javax.persistence.Table._
import beans.BeanProperty
import javax.persistence._

/**
 * 
 * User: fabien
 * Date: 04/02/12
 * Time: 12:37
 * 
 */

@Entity
@Table(name="Unit")
class Unit {
  @BeanProperty
  @Id
  @GeneratedValue
  var id: Long = 0

  @BeanProperty
  @Column(length = 50)
  var name:String = ""

  @BeanProperty
  @Column(length = 10)
  var nameAbrv:String = ""

  @BeanProperty
  @Column(length = 50)
  var plural:String = ""

  @BeanProperty
  @Column(length = 10)
  var pluralAbrv:String = ""

  @BeanProperty
  @Column
  var cunitType:Int = 0

  def unitType:UnitType.Value = UnitType(cunitType)
  def unitType_=(vunitType:UnitType.Value ) { cunitType = vunitType.id }


  override def toString():String = {
    "Unit(" + name + ", " + nameAbrv + ", #" + id + ")"
  }

  override def hashCode():Int = {
    (41*(41*(41*(41+name.hashCode())+nameAbrv.hashCode() ) +plural.hashCode()) + pluralAbrv.hashCode())
  }

  override def equals(e:Any):Boolean = {
    if(e != null && e.isInstanceOf[Unit]) {
      val other = e.asInstanceOf[Unit]
      (other canEqual this) &&
        other.name.equals(this.name) &&
        other.nameAbrv.equals(this.nameAbrv) &&
        other.plural.equals(this.plural) &&
        other.pluralAbrv.equals(this.pluralAbrv)
    } else {
      false
    }
  }

  private def canEqual(other:Any):Boolean = {
    other.isInstanceOf[Unit]
  }


  def getAbrv():String = {
    if(this.nameAbrv != null && this.nameAbrv != "")
      this.nameAbrv
    else
      this.name
  }

}
