package com.malliina.full.db

import org.flywaydb.core.Flyway

object MyDB {
  def withMigrations(conf: DatabaseConf): MyDB = {
    val flyway = Flyway.configure.dataSource(conf.url, conf.username, conf.password).load()
    flyway.migrate()
    MyDB(conf)
  }

  def apply(conf: DatabaseConf) = new MyDB(conf)
}

class MyDB(conf: DatabaseConf) {}
