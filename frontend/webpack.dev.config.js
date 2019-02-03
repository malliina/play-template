const ScalaJS = require('./scalajs.webpack.config');
const Merge = require('webpack-merge');

const WebApp = Merge(ScalaJS, {
  mode: 'development'
});

module.exports = WebApp;
