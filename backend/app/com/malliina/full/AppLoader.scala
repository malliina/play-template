package com.malliina.full

import com.malliina.full.db.{DatabaseConf, MyDB}
import com.malliina.play.app.DefaultApp
import controllers.{AppHtml, AssetsComponents, Home}
import play.api.ApplicationLoader.Context
import play.api.routing.Router
import play.api.{BuiltInComponentsFromContext, Configuration, Mode}
import play.filters.HttpFiltersComponents
import router.Routes

class AppLoader extends DefaultApp(ctx => new AppComponents(ctx, conf => DatabaseConf(conf)))

object AppComponents {
  def apply(context: Context, parseConf: Configuration => DatabaseConf): AppComponents =
    new AppComponents(context, parseConf)
}

class AppComponents(context: Context, parseConf: Configuration => DatabaseConf)
    extends BuiltInComponentsFromContext(context)
    with HttpFiltersComponents
    with AssetsComponents {
  val dbConf = parseConf(configuration)
  val db = if (environment.mode == Mode.Dev) MyDB(dbConf) else MyDB.withMigrations(dbConf)
  val secretService = SecretService
  val html = AppHtml(environment.mode)
  val home = new Home(html, controllerComponents, assets)
  override val router: Router = new Routes(httpErrorHandler, home)
}
