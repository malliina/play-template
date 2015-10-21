package controllers

import play.api.mvc._

/**
 *
 * @author mle
 */
class Home extends Controller {
  def index = Action(Ok(views.html.index("Hoi!")))
}
