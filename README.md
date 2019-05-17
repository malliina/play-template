[![Build Status](https://travis-ci.org/malliina/play-template.svg?branch=master)](https://travis-ci.org/malliina/play-template)
[![CircleCI](https://circleci.com/gh/malliina/play-template.svg?style=svg)](https://circleci.com/gh/malliina/play-template)
[![Build Status](https://malliina.visualstudio.com/play-template/_apis/build/status/play-template-CI)](https://malliina.visualstudio.com/play-template/_build/latest?definitionId=1)

# play-template

This project contains [Play Framework](http://www.playframework.com/) project templates.

- a [vanilla](vanilla) project with minimal dependencies
- a [backend](backend) project coupled to a [frontend](frontend) project that adds 
[Scala.js](https://www.scala-js.org/) and LESS assets to the mix
- a [native](native) project that demonstrates packaging a Play app to run as a Windows service

## Scala, Scala.js and webpack integration

While Scala projects use sbt, frontend dependencies use npm and are bundled with webpack. 
[scalajs-bundler](https://scalacenter.github.io/scalajs-bundler/) wraps webpack and makes the assets 
available to the Play backend.
