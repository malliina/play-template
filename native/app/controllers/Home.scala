package controllers

import com.malliina.pn.{AppTags, BuildInfo}
import controllers.Assets.Asset
import play.api.mvc._

class Home(comps: ControllerComponents, assets: AssetsBuilder) extends AbstractController(comps) {
  val version = BuildInfo.version
  val gitHash = BuildInfo.gitHash

  def index = Action(Ok(AppTags.index(s"Version $version; git $gitHash")))

  def at(path: String, file: Asset) = assets.versioned(path, file)
}
