package com.malliina.pn

import scalatags.Text.all._

object AppTags {
  def index(msg: String) = TagPage(
    html(
      body(
        h1(msg)
      )
    )
  )
}
