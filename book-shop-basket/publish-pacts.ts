let publisher = require('@pact-foundation/pact-node');
let path = require('path');

let options = {
  provider: 'Catalog_Provider',
  pactFilesOrDirs: [path.resolve(__dirname, 'pacts')],
  pactBroker: 'https://[Your pact broker]',
  pactBrokerToken: '[Your token]',
  consumerVersion: '1.0.0'
};


publisher.publishPacts(options)
.then(function () {
  console.log('Pacts successfully published!');
})
.catch(e => {
  console.log('Pact contract publishing failed: ', e)
})
