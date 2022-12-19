/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2020 Adobe Systems Incorporated
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
/* eslint-disable */

const path = require("path");

const BUILD_DIR = path.join(__dirname, "dist");
const CLIENTLIB_DIR = path.join(
  __dirname,
  "ui.apps",
  "src",
  "main",
  "content",
  "jcr_root",
  "apps",
  "ui-lead2loyalty",
  "clientlibs"
);

const libsBaseConfig = {
  allowProxy: true,
  serializationFormat: "xml",
  cssProcessor: ["default:none", "min:none"],
  jsProcessor: ["default:none", "min:none"],
};

// Config for `aem-clientlib-generator`
module.exports = {
  context: BUILD_DIR,
  clientLibRoot: CLIENTLIB_DIR,
  libs: [

    {
      ...libsBaseConfig,
      name: "clientlib-lead2loyaltyvendor",
      categories: ["lib.lead2loyaltyvendor"],
      
      assets: {
        // Copy entrypoint scripts and stylesheets into the respective ClientLib
        // directories
        js: {
          cwd: "clientlib-lead2loyaltyvendor",
          files: ["**/*.js"],
          flatten: false,
        },
        css: {
          cwd: "clientlib-lead2loyaltyvendor",
          files: ["**/*.css"],
          flatten: false,
        },
      },
    },

    {
      ...libsBaseConfig,
      name: "clientlib-lead2loyalty",
      categories: ["lib.lead2loyalty"],
      embed: ["core.wcm.components.carousel.v1"],
      dependencies: ["lib.lead2loyaltyvendor","core.lead2loyalty.components.page.v2.editor"],
      assets: {
        // Copy entrypoint scripts and stylesheets into the respective ClientLib
        // directories
        js: {
          cwd: "clientlib-lead2loyalty",
          files: ["**/*.js"],
          flatten: false,
        },
        css: {
          cwd: "clientlib-lead2loyalty",
          files: ["**/*.css"],
          flatten: false,
        },
        resources: {
          cwd: "clientlib-site",
          files: ["**/*.*"],
          flatten: false,
          ignore: ["**/*.js", "**/*.css"],
        },
      },
    },


    {
      ...libsBaseConfig,
      name: "clientlib-wwwvendor",
      categories: ["site.wwwvendor"],
      
      assets: {
        // Copy entrypoint scripts and stylesheets into the respective ClientLib
        // directories
        js: {
          cwd: "clientlib-wwwvendor",
          files: ["**/*.js"],
          flatten: false,
        },
        css: {
          cwd: "clientlib-wwwvendor",
          files: ["**/*.css"],
          flatten: false,
        },
      },
    },

    {
      ...libsBaseConfig,
      name: "clientlib-www",
      categories: ["site.www"],
      embed: ["core.wcm.components.carousel.v1"],
      dependencies: ["site.wwwvendor"],
      assets: {
        // Copy entrypoint scripts and stylesheets into the respective ClientLib
        // directories
        js: {
          cwd: "clientlib-www",
          files: ["**/*.js"],
          flatten: false,
        },
        css: {
          cwd: "clientlib-www",
          files: ["**/*.css"],
          flatten: false,
        },
        resources: {
          cwd: "clientlib-site",
          files: ["**/*.*"],
          flatten: false,
          ignore: ["**/*.js", "**/*.css"],
        },
      },
    },
    {
      ...libsBaseConfig,
      name: "clientlib-global",
      categories: ["site.global"],
      assets: {
        // Copy entrypoint scripts and stylesheets into the respective ClientLib
        // directories
        js: {
          cwd: "clientlib-global",
          files: ["**/*.js"],
          flatten: false,
        },
        css: {
          cwd: "clientlib-global",
          files: ["**/*.css"],
          flatten: false,
        },
        resources: {
          cwd: "clientlib-global",
          files: ["**/*.*"],
          flatten: false,
          ignore: ["**/*.js", "**/*.css"],
        },
      },
    },

  ],
};
