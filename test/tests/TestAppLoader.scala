package tests

import com.malliina.app.AppLoader
import play.api.test.WithApplicationLoader

/**
  * @see http://loicdescotte.github.io/posts/play24-compile-time-di/
  */
class TestAppLoader extends WithApplicationLoader(new AppLoader)
