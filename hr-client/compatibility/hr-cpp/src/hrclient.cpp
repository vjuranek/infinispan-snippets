#include <iostream>

#include "infinispan/hotrod/Configuration.h" 
#include "infinispan/hotrod/ConfigurationBuilder.h"
#include "infinispan/hotrod/RemoteCacheManager.h"
#include "infinispan/hotrod/RemoteCache.h"
#include "infinispan/hotrod/JBasicMarshaller.h"

using namespace infinispan::hotrod;
using namespace std;


int main(int argc, char** argv) {
        
  ConfigurationBuilder builder;
  builder
    .addServer()
    .host(argc > 1 ? argv[1] : "127.0.0.1")
    .port(argc > 2 ? atoi(argv[2]) : 11222)
    .protocolVersion(Configuration::PROTOCOL_VERSION_24);
   
  RemoteCacheManager cacheManager(builder.build(), false);

  Marshaller<string> *km = new JBasicMarshaller<string>();
  void (*kd)(Marshaller<string> *) = &Marshaller<string>::destroy;
  Marshaller<string> *vm = new JBasicMarshaller<string>();
  void (*vd)(Marshaller<string> *) = &Marshaller<string>::destroy;
  RemoteCache<string, string> cache = cacheManager.getCache<string, string>(km,kd,vm,vd);
  cacheManager.start();

  string key("cpp-key");
  string value("cpp-value");
  cache.put(key, value);
  cout << "C++ value from cache: " << *(cache.get(key))  << endl;
  string *javaVal = cache.get("java-key");
  if (javaVal != NULL) {
    cout << "Java value from cache: " << *(javaVal)  << endl;
  }

  cacheManager.stop();
  return 0;
}
 
