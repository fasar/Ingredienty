
import play.api._


object Global extends GlobalSettings {

  private val log = Logger(Global.getClass)

  override def onStart(app: Application) {
    log.info("Application has started")
    Logger.info("Initialize Ebean server")


  }

  override def onStop(app: Application) {
    log.info("Application shutdown...")
  }

}