package controllers

import play.api.mvc._

/**
 *
 * @author mle
 */
object Home extends Controller {
  def index = Action(Ok(views.html.index("Hoi!")))
}
