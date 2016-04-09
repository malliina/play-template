package com.mle.app

import controllers.{Assets, Home}
import play.api.routing.Router
import play.api._
import play.api.ApplicationLoader.Context
import router.Routes

class AppLoader extends ApplicationLoader {
  override def load(context: Context): Application = {
    LoggerConfigurator(context.environment.classLoader)
      .foreach(_.configure(context.environment))
    new AppComponents(context).application
  }
}

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context) {
  lazy val assets = new Assets(httpErrorHandler)
  override val router: Router = new Routes(httpErrorHandler, new Home, assets)
}
