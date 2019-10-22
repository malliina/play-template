package com.malliina.full

import ch.vorburger.mariadb4j.{DB, DBConfigurationBuilder}
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class EmbeddedDatabaseTests extends FunSuite with BeforeAndAfterAll {
  private val dbConfig =
    DBConfigurationBuilder
      .newBuilder()
      .setDeletingTemporaryBaseAndDataDirsOnShutdown(true)
  lazy val db = DB.newEmbeddedDB(dbConfig.build())

  test("run test") {
    assert(1 === 1)
  }

  override def beforeAll(): Unit = {
    db.start()
  }

  override def afterAll(): Unit = {
    db.stop()
  }
}
