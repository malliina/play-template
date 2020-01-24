package com.malliina.full.db

import play.api.Configuration

case class DatabaseConf(url: String, username: String, password: String)

object DatabaseConf {
  def apply(conf: Configuration): DatabaseConf = {
    def read(key: String) = conf.get[String](s"play.db.$key")
    DatabaseConf(read("url"), read("username"), read("password"))
  }
}
