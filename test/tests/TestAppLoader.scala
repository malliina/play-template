package tests

import com.mle.app.AppLoader
import play.api.test.WithApplicationLoader

/**
  * @author mle
  * @see http://loicdescotte.github.io/posts/play24-compile-time-di/
  */
class TestAppLoader extends WithApplicationLoader(new AppLoader)
