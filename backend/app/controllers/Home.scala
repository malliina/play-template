package controllers

import controllers.Assets.Asset
import play.api.mvc._

class Home(html: AppHtml, comps: ControllerComponents, assets: AssetsBuilder)
  extends AbstractController(comps) {

  def index = Action(Ok(html.index("Hello!")))

  def static(file: String) = assets.at("/public", s"static/$file", aggressiveCaching = true)

  def versioned(path: String, file: Asset): Action[AnyContent] =
    assets.versioned(path, file)
}
