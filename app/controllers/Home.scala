package controllers

import play.api.http.Writeable
import play.api.mvc._

import scala.language.implicitConversions
import scalatags.Text.TypedTag
import scalatags.Text.all._

/**
 *
 * @author mle
 */
object Home extends Controller {
  //  implicit val tagWriteable =
  // Writeable[TypedTag[String]]((t: TypedTag[String]) => t.toString().getBytes("UTF-8"), Option("text/html"))
  //  implicit def tagWriteable(implicit codec: Codec): Writeable[TypedTag[String]] =
  // Writeable.wString(codec).map(tag => tag.toString())
  implicit def tagWriteable(implicit codec: Codec): Writeable[TypedTag[String]] =
    Writeable(t => codec.encode(t.toString()), Some("text/html"))

  def index = Action(Ok(indexContent))

  def indexContent =
    html(
      head(

      ),
      body(
        h1("Hello, world!")
      )
    )
}
