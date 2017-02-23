(defproject hr-clojure "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.infinispan/infinispan-remote "9.0.0.CR1"]]
  :main ^:skip-aot infinispan.test
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
