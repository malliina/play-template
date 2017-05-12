package com.malliina.app

import com.malliina.play.app.DefaultApp
import controllers.{Assets, Home}
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.routing.Router
import router.Routes

class AppLoader extends DefaultApp(new AppComponents(_))

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context) {
  val secretService = SecretService
  val home = new Home

  lazy val assets = new Assets(httpErrorHandler)
  override val router: Router = new Routes(httpErrorHandler, home, assets)
}
