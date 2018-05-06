package com.malliina.full

import com.malliina.play.app.DefaultApp
import controllers.{AppHtml, AssetsComponents, Home}
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.routing.Router
import play.filters.HttpFiltersComponents
import router.Routes

class AppLoader extends DefaultApp(new AppComponents(_))

class AppComponents(context: Context)
  extends BuiltInComponentsFromContext(context)
    with HttpFiltersComponents
    with AssetsComponents {

  val secretService = SecretService
  val html = AppHtml(environment.mode)

  val home = new Home(html, controllerComponents, assets)
  override val router: Router = new Routes(httpErrorHandler, home)
}
