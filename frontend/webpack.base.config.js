const ScalaJS = require('./scalajs.webpack.config');
const Merge = require('webpack-merge');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const path = require('path');
const rootDir = path.resolve(__dirname, '../../../..');
const cssDir = path.resolve(rootDir, 'src/main/resources/css');
const vendorsDir = path.resolve(rootDir, 'src/main/resources/vendors');

const WebApp = Merge(ScalaJS, {
  entry: {
    styles: [path.resolve(cssDir, './styles.js')],
    vendors: [path.resolve(vendorsDir, './vendors.js')]
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
        test: /\.less$/,
        use: [
          { loader: MiniCssExtractPlugin.loader,
            options: {
              hmr: process.env.NODE_ENV === 'development'
            }
          },
          { loader: 'css-loader', options: { importLoaders: 1, url: false } },
          'postcss-loader',
          'less-loader'
        ]
      },
      {
        test: /\.(png|woff|woff2|eot|ttf|svg)$/,
        use: [
          { loader: 'url-loader', options: { limit: 8192, name: 'static/assets/[name]-[hash].[ext]' } }
        ],
        exclude: /node_modules/
      },
      {
        test: /\.(png|woff|woff2|eot|ttf|svg)$/,
        use: [
          { loader: 'file-loader',
            options: { name: 'static/assets/[name]-[hash].[ext]' }
          }
        ],
        include: /node_modules/
      }
    ]
  },
  plugins: [
    new MiniCssExtractPlugin({filename: '[name].css'})
  ]
});

module.exports = WebApp;
