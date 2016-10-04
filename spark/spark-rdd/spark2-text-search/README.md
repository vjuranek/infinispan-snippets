 * Start ISPN server
 * start Spark:

```bash
 export SPARK_MASTER_HOME=127.0.0.1
 $SPARK_HOME/sbin/start-master.sh --webui-port 9080
 $SPARK_HOME/sbin/start-slave.sh spark://127.0.0.1:7077 --webui-port 9081 -h 127.0.0.1 
 ```
 * upload some file into ISPN:

```java -jar target/spark-text-search-1.0-SNAPSHOT-jar-with-dependencies.jar```
 * run Spark job:

```$SPARK_HOME/bin/spark-submit --master spark://127.0.0.1:7077 --class org.infinispan.demo.TextSearch --packages org.infinispan:infinispan-spark_2.11:0.4 $WORKSPACE/infinispan-snippets/spark/spark-rdd/spark-text-search/target/spark-text-search-1.0-SNAPSHOT.jar```