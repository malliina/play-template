package controllers

import com.malliina.full.FrontKeys
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
    val name = "frontend"
    val opt = if (isProd) "opt" else "fastopt"
    new AppHtml(Seq(s"$name-$opt-library.js", s"$name-$opt-loader.js", s"$name-$opt.js"))
  }
}

class AppHtml(scripts: Seq[String]) extends Tags(scalatags.Text) {
  def index(msg: String) = TagPage(
    html(
      head(
        cssLink(routes.Home.versioned("css/styles.css")),
//        script(`type` := MimeTypes.JAVASCRIPT, defer, src := routes.Home.versioned("styles-library.js")),
        scripts.map { js => script(`type` := MimeTypes.JAVASCRIPT, defer, src := routes.Home.versioned(js)) },
      ),
      body(
        div(`class` := "container", id := FrontKeys.FrontContainer)(
          h1(msg),
          h2("This app has"),
          ul(
            li("A Scala backend"),
            li("A Scala.js frontend"),
            li("LESS assets compilation"),
            li("A shared code module for the backend and frontend")
          ),
          p("The following paragraph is written by the Scala.js module:")
        )
      )
    )
  )
}
