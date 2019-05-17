const ScalaJS = require('./scalajs.webpack.config');
const Merge = require('webpack-merge');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const path = require('path');
const rootDir = path.resolve(__dirname, '../../../..');
const cssDir = path.resolve(rootDir, 'src/main/resources/css');

const WebApp = Merge(ScalaJS, {
  entry: {
    styles: [path.resolve(cssDir, './styles.js')],
  },
  module: {
    rules: [
      {
        test: /\.css$/,
        use: [
          MiniCssExtractPlugin.loader,
          { loader: 'css-loader', options: { importLoaders: 1, url: true } }
        ]
      },
      {
        test: /\.(png|woff|woff2|eot|ttf|svg)$/,
        use: [
          { loader: 'url-loader', options: { limit: 8192, name: 'static/fonts/[name]-[hash].[ext]' } }
        ]
      },
      {
        test: /\.less$/,
        use: [
          MiniCssExtractPlugin.loader,
          { loader: 'css-loader', options: { importLoaders: 1, url: false } },
          'postcss-loader',
          'less-loader'
        ]
      }
    ]
  },
  plugins: [
    new MiniCssExtractPlugin({filename: 'css/[name].css'})
  ]
});

module.exports = WebApp;
