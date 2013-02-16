package controllers.website

import play.api._
import libs.Comet
import libs.iteratee.Enumerator
import libs.json.{JsString, Json}
import play.api.mvc._
import concurrent.ExecutionContext

import models._
import models.dao.{UnitDao, IngredientDao}
import anorm._


object Application extends Controller {
  private val log = Logger(classOf[Application])

  def index = Action {
    req =>
      implicit val myCustomCharset = Codec.javaSupported("iso-8859-1")
      Ok(views.html.index("Ok, got a request : " + req))
  }

  def hello(name: String) = Action {
    Ok(views.html.index("Hello " + name) )
  }


  def stream = Action {
    import ExecutionContext.Implicits.global
    val Max:Long = 3000000000L
    def from(n: Int): Stream[Long] =
      Stream.cons(n, if(n>Max) Stream.empty else from(n + 1))

    val dataContent = Enumerator.enumerate(from(1).map{_.toString + ",\n"})
    Ok.stream(
      dataContent
    )
  }


  def comet = Action {
    val events = Enumerator(
      """<script>console.log('kiki')</script>""",
      """<script>console.log('foo')</script>""",
      """<script>console.log('bar')</script>"""
    )
    Ok.stream(events >>> Enumerator.eof).as(HTML)
  }

  def comet2 = Action {
    val events = Enumerator("kiki", "foo", "bar")
    Ok.stream(events &> Comet(callback = "parent.cometMessage"))
  }

  def cometEx = Action {
    Ok(views.html.cometex())
  }



  def upload = Action(parse.multipartFormData) { request =>
    request.body.file("picture").map { picture =>
      import java.io.File
      val filename = picture.filename
      val contentType = picture.contentType
      picture.ref.moveTo(new File("/tmp/picture"))
      Ok("File uploaded")
    }.getOrElse {
      routes.Application.index()
      Redirect(controllers.website.routes.Application.index).flashing(
        "error" -> "Missing file"
      )
    }
  }

  def uploadForm = Action { request =>
      Ok(views.html.upload())
  }

  def redirect = Action { request =>
    Redirect(controllers.website.routes.Application.index)
  }

  def recette = TODO

  def newPlat = Action { Ok(views.html.platNew()) }

  def ingredients = Action {
    val ingredients = IngredientDao.findAll

    Ok(views.html.ingredients(ingredients))
  }

  def ingredientsQuery() = Action { implicit request =>
    val searchedTerm:String = request.queryString.getOrElse("term", Seq(""))(0)
    val ingredients = IngredientDao.findAll
    val listIngredients =
      for(ingredient <- ingredients if ingredient.name.toLowerCase.contains(searchedTerm.toLowerCase))
      yield {
        Map("label" -> ingredient.name, "value" -> ingredient.id.toString)
      }
    val listIngredientsJson = listIngredients.map {mapToJson}
      //ingredients.withFilter(_.name.contains(searchedTerm)).map(_.name)
    Ok(Json.toJson(listIngredients))
  }

  private def mapToJson(map: Map[String,String]):String = {
    val jsonTxt = (for(x <- map) yield {
      "\"" +x._1 +"\" : \"" + x._2 +"\""
    }).mkString("{", ",","}")
    jsonTxt
  }

  def ingredientApports(id: Long) = Action { implicit request =>
    val ingredient = IngredientDao.findById(id)
    val resMap =
      for(ingre <- ingredient )
      yield {
        val ingredientProperties = ingre.ingredientProperties
        val res = Map("id" -> ingre.id.toString,
          "name"-> ingre.name,
          "famille"-> ingre.familyStr,
          "glucides" -> ingredientProperties.getOrElse("Glucides", "not found"),
          "lipides" -> ingredientProperties.getOrElse("Lipides", "not found"),
          "proteines" -> ingredientProperties.getOrElse("Protéines", "not found")
        )
        res
      }

    val res =
      for(map <- resMap) yield {
        val jsonTxt = (for(x <- map) yield {
          "\"" +x._1 +"\": \"" + x._2 +"\""
        }).mkString("{", ",","}")
        Ok(Json.parse(jsonTxt))
      }
    //val res = jsonMap.map { x=>  Ok(Json.parse(x)) } //Ok(Json.toJson(List("Un", "Deux"))) } //Ok(Json.toJson(x))}
    res.getOrElse(NotFound)
  }

  def ingredientCherche() = TODO

}