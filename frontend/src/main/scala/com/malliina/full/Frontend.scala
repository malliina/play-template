package com.malliina.full

import org.scalajs.dom
import scalatags.JsDom.all._

object Frontend {
  def main(args: Array[String]): Unit = {
    dom.document.getElementById(FrontKeys.FrontContainer).appendChild(
      p("Hello from Scala.js!").render
    )
  }
}
