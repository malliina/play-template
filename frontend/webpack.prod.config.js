const BaseWebpack = require('./webpack.base.config');
const Merge = require('webpack-merge');

const WebApp = Merge(BaseWebpack, {
  mode: 'production'
});

module.exports = WebApp;
