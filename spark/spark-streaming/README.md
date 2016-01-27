### Running Spark ###
```
export SPARK_MASTER_IP=127.0.0.1
./sbin/start-master.sh --webui-port 9080
./sbin/start-slave.sh spark://127.0.0.1:7077 --webui-port 9081
```

### Submitting Spark job ###
```
./bin/spark-submit --master spark://127.0.0.1:7077 --class org.infinispan.demo.TemperatureAnalysis --packages org.infinispan:infinispan-spark_2.10:0.2 spark-temperature-analysis/target/spark-temperature-analysis-1.0-SNAPSHOT.jar
```