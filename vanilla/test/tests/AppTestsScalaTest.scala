package tests

import com.malliina.app.AppComponents
import play.api.test.FakeRequest
import play.api.test.Helpers._

class TestAppSuite extends AppSuite(new AppComponents(_))

class AppTestsScalaTest extends TestAppSuite {

  test("can access a component of the running test app") {
    assert(components.secretService.secret === 42)
  }

  test("can make request") {
    val result = route(app, FakeRequest(GET, "/")).get
    assert(status(result) === 200)
  }
}
