package com.malliina.full

import org.scalajs.dom.document
import org.scalajs.jquery.JQueryStatic
import scalatags.JsDom.all._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Frontend {
  private val jq = MyJQuery
  private val popper = Popper
  private val bs = Bootstrap

  def main(args: Array[String]): Unit = {
    document
      .getElementById(FrontKeys.FrontContainer)
      .appendChild(
        div(
          p("Hello from Scala.js!"),
          i(`class` := "fas fa-trophy")
        ).render
      )
  }
}

@js.native
@JSImport("jquery", JSImport.Namespace)
object MyJQuery extends JQueryStatic

@js.native
@JSImport("popper.js", JSImport.Namespace)
object Popper extends js.Object

@js.native
@JSImport("bootstrap", JSImport.Namespace)
object Bootstrap extends js.Object
