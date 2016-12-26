package com.malliina.app

import controllers.{Assets, Home}
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.routing.Router
import router.Routes

class AppLoader extends LoggingAppLoader[AppComponents] with WithAppComponents

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context) {
  lazy val assets = new Assets(httpErrorHandler)
  val secretService = SecretService

  val home = new Home

  override val router: Router = new Routes(httpErrorHandler, new Home, assets)
}
