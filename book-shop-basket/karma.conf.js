// Karma configuration file, see link for more information
// https://karma-runner.github.io/1.0/config/configuration-file.html

const path = require('path');
module.exports = function (config) {
  config.set({
    basePath: '',
    frameworks: ['jasmine', 'pact', '@angular-devkit/build-angular'],
    plugins: [
      require('karma-jasmine'),
      require('karma-chrome-launcher'),
      require('karma-jasmine-html-reporter'),
      require('karma-coverage-istanbul-reporter'),
      require('@angular-devkit/build-angular/plugins/karma'),
      require('@pact-foundation/karma-pact')
    ],
    client: {
      clearContext: false // leave Jasmine Spec Runner output visible in browser
    },
    coverageIstanbulReporter: {
      dir: require('path').join(__dirname, './coverage/book-shop-basket'),
      reports: ['html', 'lcovonly', 'text-summary'],
      fixWebpackSourcePaths: true
    },
    reporters: ['progress', 'kjhtml'],
    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,
    autoWatch: true,
    browsers: ['Chrome'],
    singleRun: false,
    restartOnFileChange: true,
    // pact: [
    //   {
    //     cors: true,
    //     consumer: 'book-shop-basket',
    //     provider: 'inventory-service',
    //     port: 1234,
    //     spec: 3,
    //     log: path.resolve(process.cwd(), 'logs', 'mockserver-integration.log'),
    //     dir: path.resolve('pacts')
    //   },
    //   {
    //     cors: true,
    //     consumer: 'book-shop-basket',
    //     provider: 'checkout-service',
    //     port: 1235,
    //     spec: 3,
    //     log: path.resolve(process.cwd(), 'logs', 'mockserver-integration.log'),
    //     dir: path.resolve('pacts')
    //   }
    // ],
    // proxies: {
    //   '/v1/books': 'http://localhost:1234/v1/books',
    //   '/v1/checkouts': 'http://localhost:1235/v1/checkouts'
    // }
  });
};
