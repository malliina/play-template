package com.mle.app

import controllers.{Home, Assets}
import play.api.routing.Router
import play.api.{Logger, BuiltInComponentsFromContext, Application, ApplicationLoader}
import play.api.ApplicationLoader.Context
import router.Routes

/**
 * @author mle
 */
class AppLoader extends ApplicationLoader {
  override def load(context: Context): Application = {
    Logger.configure(context.environment)
    new AppComponents(context).application
  }
}

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context) {
  lazy val assets = new Assets(httpErrorHandler)
  override val router: Router = new Routes(httpErrorHandler, new Home, assets)
}
