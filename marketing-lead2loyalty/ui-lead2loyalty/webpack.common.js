/* eslint-disable */

"use strict";

const path = require("path");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const TSConfigPathsPlugin = require("tsconfig-paths-webpack-plugin");
const CopyWebpackPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const ESLintPlugin = require("eslint-webpack-plugin");

const SOURCE_ROOT = __dirname + "/src/main";

const resolve = {
  extensions: [".js", ".ts"],
  plugins: [
    new TSConfigPathsPlugin({
      configFile: "./tsconfig.json",
    }),
  ],
};

module.exports = {
  resolve: resolve,
  entry: {
    lead2loyalty: SOURCE_ROOT + "/lead2loyalty/main.ts",
    lead2loyaltyvendor: SOURCE_ROOT + "/lead2loyalty/vendor/vendor.ts",
    www: SOURCE_ROOT + "/www/main.ts",
    wwwvendor: SOURCE_ROOT + "/www/vendor/vendor.ts",
    global: SOURCE_ROOT + "/global/global.ts"
  },
  output: {
    filename: () => {
      return "clientlib-[name]/[name].js";
    },
    path: path.resolve(__dirname, "dist"),
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader",
        },
      },
      {
        test: /\.css$/,
        use: ["style-loader", "css-loader"],
      },
      {
        test: /\.tsx?$/,
        exclude: /node_modules/,
        use: [
          {
            loader: "ts-loader",
          },
          {
            loader: "glob-import-loader",
            options: {
              resolve: resolve,
            },
          },
        ],
      },
      {
        test: /\.scss$|\.css$/,
        use: [
          MiniCssExtractPlugin.loader,
          {
            loader: "css-loader",
            options: {
              url: false,
            },
          },
          {
            loader: "postcss-loader",
            options: {
              plugins() {
                return [require("autoprefixer")];
              },
            },
          },
          {
            loader: "sass-loader",
          },
          {
            loader: "glob-import-loader",
            options: {
              resolve: resolve,
            },
          },
        ],
      },
    ],
  },
  plugins: [
    new CleanWebpackPlugin(),
    new ESLintPlugin({
      extensions: ["js", "ts", "tsx"],
    }),
    new MiniCssExtractPlugin({
      filename: "clientlib-[name]/[name].css",
    }),
    new CopyWebpackPlugin({
      patterns: [
        {
          from: path.resolve(__dirname, SOURCE_ROOT + "/resources"),
          to: "./clientlib-site/",
        },
      ],
    }),
  ],
  stats: {
    assetsSort: "chunks",
    builtAt: true,
    children: false,
    chunkGroups: true,
    chunkOrigins: true,
    colors: false,
    errors: true,
    errorDetails: true,
    env: true,
    modules: false,
    performance: true,
    providedExports: false,
    source: false,
    warnings: true,
  },
};
