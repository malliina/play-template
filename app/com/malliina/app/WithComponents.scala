package com.malliina.app

import play.api.ApplicationLoader.Context
import play.api.BuiltInComponents

trait WithComponents[T <: BuiltInComponents] {
  def createComponents(context: Context): T
}
