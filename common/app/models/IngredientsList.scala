package models

import beans.BeanProperty
import javax.persistence._

/**
 * 
 * User: fabien
 * Date: 07/02/12
 * Time: 22:00
 * 
 */

@Entity
class IngredientsList {
  @Id
  @GeneratedValue
  var id: Long = 0

  @BeanProperty
  @Column
  var quantity:Double = 0.0D

  @BeanProperty
  @OneToOne
  var ingredient:Ingredient = null

  @ManyToOne
  @JoinColumn(name="recipe_id")
  var recipe:Recipe = null

  @BeanProperty
  @OneToOne
  @JoinColumn(name="unit_id")
  var unit:Unit = null

}
