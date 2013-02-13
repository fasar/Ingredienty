package models

import beans.BeanProperty
import javax.persistence.{ElementCollection, Column, GeneratedValue, Id}
import javax.persistence.Table._
import javax.persistence._
import java.util.HashMap


/**
 * 
 * User: fabien
 * Date: 02/02/12
 * Time: 14:38
 * 
 */
@Entity
@Table(name="Recipes")
class Recipe() {
  @BeanProperty
  @Id
  @GeneratedValue
  var id: Long = 0

  @BeanProperty
  @Column
  val name:String = ""

  @BeanProperty
  @Column(columnDefinition="LONGTEXT")
  val instructions:String =""

  @BeanProperty
  @Column
  val author:String =""

  @BeanProperty
  @Column
  val public:Boolean = true

  @BeanProperty
  @Column
  val description:String = ""

  @BeanProperty
  @Column
  val prepTimeSec:Int =0

  @BeanProperty
  @Column
  val cookTimeSec:Int = 0

  @BeanProperty
  @Column
  val yields:Int = 0

  @BeanProperty
  @OneToMany(mappedBy="recipe")
  val ingredients:java.util.Set[IngredientsList] = new java.util.HashSet[IngredientsList]()


  def getPropertie(ingredientPropertie:String):String = {
    "A Implem"
  }

  override def toString():String = {
    "Recipe(" + name + ")"
  }
}
