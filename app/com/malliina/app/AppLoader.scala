package com.malliina.app

import controllers.{Assets, Home}
import play.api.routing.Router
import play.api._
import play.api.ApplicationLoader.Context
import router.Routes

class AppLoader extends LoggingAppLoader[AppComponents] with WithAppComponents

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context) {
  lazy val assets = new Assets(httpErrorHandler)
  val secretService = SecretService
  override val router: Router = new Routes(httpErrorHandler, new Home, assets)
}
