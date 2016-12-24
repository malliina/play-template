package controllers

import play.api.mvc._

class Home extends Controller {
  def index = Action(Ok(AppTags.index("Hoi!")))
}
