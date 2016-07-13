package tests

import com.malliina.app.AppLoader
import play.api.ApplicationLoader.Context
import play.api.test.WithApplicationLoader
import play.api.{ApplicationLoader, Environment, Mode}

/**
  * @see http://loicdescotte.github.io/posts/play24-compile-time-di/
  */
class TestAppLoader extends WithApplicationLoader(new AppLoader)

object TestAppLoader {
  def createTestAppContext: Context = {
    val env = new Environment(new java.io.File("."), ApplicationLoader.getClass.getClassLoader, Mode.Test)
    ApplicationLoader.createContext(env)
  }
}
