package tests

import com.malliina.app.{AppComponents, WithAppComponents}
import org.scalatest.FunSuite
import play.api.test.FakeRequest
import play.api.test.Helpers._

class AppTestsScalaTest extends FunSuite with OneAppPerSuite2[AppComponents] with WithAppComponents {

  test("can access a component of the running test app") {
    assert(components.secretService.secret === 42)
  }

  test("can make request") {
    val result = route(app, FakeRequest(GET, "/")).get
    assert(status(result) === 200)
  }
}
