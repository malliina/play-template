package com.malliina.pn

import controllers.{AssetsComponents, Home}
import play.api.ApplicationLoader.Context
import play.api._
import play.api.routing.Router
import play.filters.HttpFiltersComponents
import router.Routes

class DefaultApp[T <: BuiltInComponents](build: Context => T) extends LoggingAppLoader[T] {
  override def createComponents(context: Context): T = build(context)
}

trait LoggingAppLoader[T <: BuiltInComponents] extends ApplicationLoader with WithComponents[T] {
  override def load(context: Context): Application = {
    val environment = context.environment
    LoggerConfigurator(environment.classLoader)
      .foreach(_.configure(environment))
    createComponents(context).application
  }
}

trait WithComponents[T <: BuiltInComponents] {
  def createComponents(context: Context): T
}

class AppLoader extends DefaultApp(new AppComponents(_))

class AppComponents(context: Context)
  extends BuiltInComponentsFromContext(context)
    with HttpFiltersComponents
    with AssetsComponents {
  val home = new Home(controllerComponents, assets)
  override val router: Router = new Routes(httpErrorHandler, home)
}
