package controllers.website

import play.api._
import libs.Comet
import libs.iteratee.Enumerator
import play.api.mvc._
import concurrent.ExecutionContext

object Application extends Controller {
  
  def index = Action {
    req =>
      implicit val myCustomCharset = Codec.javaSupported("iso-8859-1")
      Ok(views.html.index("Ok, got a request : " + req))
    //Ok(views.html.index("Your new application is ready."))
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

  def ingredients = TODO
}