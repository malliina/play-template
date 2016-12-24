package com.malliina.app

import controllers.{AssetsBuilder, Home}
import play.api.ApplicationLoader.Context
import play.api._
import play.api.routing.Router
import play.api.routing.sird._

class AppLoader extends LoggingAppLoader[AppComponents] with WithAppComponents

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context) {
  lazy val assets = new AssetsBuilder(httpErrorHandler)
  val secretService = SecretService
  val home = new Home

  override def router = Router.from {
    case GET(p"/") => home.index
    case GET(p"/assets/$file*") => assets.at(path = "/public", file)
  }
}
