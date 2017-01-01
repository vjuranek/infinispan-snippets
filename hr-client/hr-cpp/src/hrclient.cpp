#include <iostream>

#include "infinispan/hotrod/ConfigurationBuilder.h"
#include "infinispan/hotrod/RemoteCacheManager.h"
#include "infinispan/hotrod/RemoteCache.h"
#include "infinispan/hotrod/JBasicMarshaller.h"

using namespace infinispan::hotrod;
using namespace std;

const int MAGIC = 2051;

int main(int argc, char** argv) {
        
  ConfigurationBuilder builder;
  builder
    .addServer()
    .host(argc > 1 ? argv[1] : "127.0.0.1")
    .port(argc > 2 ? atoi(argv[2]) : 11222);
   
  RemoteCacheManager cacheManager(builder.build(), false);

  // Marshaller<int> *km = new JBasicMarshaller<int>();
  // void (*kd)(Marshaller<int> *) = &Marshaller<int>::destroy;
  // Marshaller<string> *vm = new JBasicMarshaller<string>();
  // void (*vd)(Marshaller<string> *) = &Marshaller<string>::destroy;
  // RemoteCache<int, string> cache = cacheManager.getCache<int, string>(km,kd,vm,vd);
  RemoteCache<int, string> cache = cacheManager.getCache<int, string>();
  cacheManager.start();

  int key(1);
  string value("test");
  cache.put(key, value);
  cout << "Value from cache: " << *(cache.get(key))  << endl;

  cacheManager.stop();
  return 0;
}
 
