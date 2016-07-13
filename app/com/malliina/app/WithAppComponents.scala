package com.malliina.app

import play.api.ApplicationLoader.Context

trait WithAppComponents extends WithComponents[AppComponents] {
  override def createComponents(context: Context): AppComponents = new AppComponents(context)
}
