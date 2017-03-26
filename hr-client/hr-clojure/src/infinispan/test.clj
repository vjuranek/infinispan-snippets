(ns infinispan.test
  (:gen-class)
  (:import (org.infinispan.client.hotrod RemoteCacheManager
                                         RemoteCache)
           (org.infinispan.client.hotrod.configuration ConfigurationBuilder)
           (org.infinispan.client.hotrod.impl ConfigurationProperties)))

(defn -main
  "Creates simple HR client, puts key/value into remote server and reads it back"
  [& args]
  (def conf (ConfigurationBuilder.))
  ;;(.port (.host (.addServer conf) "127.0.0.1") ConfigurationProperties/DEFAULT_HOTROD_PORT)
  (-> (.addServer conf) (.host "127.0.0.1") (.port ConfigurationProperties/DEFAULT_HOTROD_PORT))
  (def cm (RemoteCacheManager. (.build conf)))
  (def cache (.getCache cm))
  (println "Sending key -> value")
  (.put cache "key" "value")
  (def value (.get cache "key"))
  (println "Got key -> " value)
  (.stop cm))
