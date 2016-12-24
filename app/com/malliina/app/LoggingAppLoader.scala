package com.malliina.app

import play.api.ApplicationLoader.Context
import play.api.{Application, ApplicationLoader, BuiltInComponents, LoggerConfigurator}

trait LoggingAppLoader[T <: BuiltInComponents] extends ApplicationLoader with WithComponents[T] {
  override def load(context: Context): Application = {
    val environment = context.environment
    LoggerConfigurator(environment.classLoader)
      .foreach(_.configure(environment))
    createComponents(context).application
  }
}
