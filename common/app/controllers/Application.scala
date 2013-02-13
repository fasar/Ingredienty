package controllers.common

import play.api._
import play.api.mvc._

/**
 * Created with IntelliJ IDEA.
 * User: fabien
 * Date: 12/02/13
 * Time: 20:31
 * To change this template use File | Settings | File Templates.
 */
object Application extends Controller {

  def index = Action { Ok{"Todo"} }

}
