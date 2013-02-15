
import play.api._


object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
    println("Salut coucou depuis l'objet Globale")
    Logger.info("Initialize Ebean server")


  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

}