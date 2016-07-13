package com.malliina.app

import play.api.ApplicationLoader.Context
import play.api.{Application, ApplicationLoader, BuiltInComponents, LoggerConfigurator}

trait LoggingAppLoader[T <: BuiltInComponents] extends ApplicationLoader with WithComponents[T] {
  override def load(context: Context): Application = {
    LoggerConfigurator(context.environment.classLoader)
      .foreach(_.configure(context.environment))
    createComponents(context).application
  }
}
