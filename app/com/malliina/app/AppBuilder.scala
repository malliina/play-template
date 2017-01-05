package com.malliina.app

import com.malliina.play.app.WithComponents
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponents

trait AppBuilder extends WithComponents[AppComponents] {
  override def createComponents(context: Context): AppComponents =
    new AppComponents(context)
}

object AppBuilder {
  def apply[T <: BuiltInComponents](build: Context => T) = new WithComponents[T] {
    override def createComponents(context: Context) = build(context)
  }
}
