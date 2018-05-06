package controllers

import com.malliina.html.Tags
import com.malliina.play.tags.TagPage
import controllers.AppHtml.callAttr
import play.api.Mode
import play.api.http.MimeTypes
import play.api.mvc.Call
import scalatags.Text.GenericAttr
import scalatags.Text.all._

object AppHtml {
  implicit val callAttr = new GenericAttr[Call]

  def apply(mode: Mode): AppHtml = apply(mode == Mode.Prod)

  def apply(isProd: Boolean): AppHtml = {
    val jsFile = if (isProd) "frontend-opt.js" else "frontend-fastopt.js"
    new AppHtml(jsFile)
  }
}

class AppHtml(jsFile: String) extends Tags(scalatags.Text) {

  def index(msg: String) = TagPage(
    html(
      head(
        cssLink(routes.Home.versioned("css/main.css")),
        script(`type` := MimeTypes.JAVASCRIPT, attr("defer").empty, src := routes.Home.versioned(jsFile)),
      ),
      body(
        h1(msg),
        h2("This app has"),
        ul(
          li("A Scala backend"),
          li("A Scala.js frontend"),
          li("LESS assets compilation"),
          li("A shared code module for the backend and frontend")
        ),
        p("""The frontend module writes "Hi" to the browser console.""")
      )
    )
  )
}
