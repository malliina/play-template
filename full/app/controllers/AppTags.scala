package controllers

import com.malliina.play.tags.TagPage

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
