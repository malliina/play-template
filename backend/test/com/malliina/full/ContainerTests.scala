package com.malliina.full

import com.dimafeng.testcontainers.{ForAllTestContainer, MySQLContainer}
import com.malliina.full.db.DatabaseConf
import org.scalatest.FunSuite
import play.api.ApplicationLoader
import play.api.test.FakeRequest
import tests.OneAppPerSuite2
import play.api.test.Helpers._

class ContainerTests extends FunSuite with OneAppPerSuite2[AppComponents] with ForAllTestContainer {
  override val container = MySQLContainer()

  override def createComponents(context: ApplicationLoader.Context) = {
    val conf = DatabaseConf(container.jdbcUrl, container.username, container.password)
    AppComponents(context, _ => conf)
  }

  test("make request") {
    val response = await(route(app, FakeRequest(GET, "/")).get)
    assert(response.header.status === 200)
  }

}
