package com.mle.play

import java.util.concurrent.TimeUnit

import scala.concurrent.{ExecutionContext, Future}

/**
 * @author mle
 */
object Starter extends PlayLifeCycle {
  override def appName: String = "p"

  def stop() = {
    nettyServer foreach (server => {
      server.allChannels.close().await(2, TimeUnit.SECONDS)
      server.stop()
    })
    Future(System.exit(0))(ExecutionContext.Implicits.global)
  }
}
