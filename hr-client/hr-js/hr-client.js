var infinispan = require('infinispan');
 
var client = infinispan.client({port: 11222, host: '127.0.0.1'}, {version: '2.5', cacheName: 'default'});
client.then(function(client) {
  console.log("Connected");
  var putGetPromise = client.put('key', 'value').then(function () {
    return client.get('key').then(function (value) {
      console.log('key = ' + value);
    })
  });
 
  return putGetPromise.finally(function() {
    return client.disconnect().then(function() { console.log("Disconnected") });
  });
});
