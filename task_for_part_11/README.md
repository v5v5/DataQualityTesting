# Deequ_task  
Install:
- install 'spark-3.1.1-bin-hadoop3.2' from https://spark.apache.org/downloads.html
- install 'winutils-master' from https://github.com/cdarlint/winutils
- create maven java project, see .'/Deequ_task'
- copy './m2/deequ-1.1.0_spark-3.0-scala-2.12.jar' to 'C:\spark-3.1.1-bin-hadoop3.2\bin'
- set system environment variables:
	- JAVA_HOME=C:\Users\v5\.jdks\adopt-openjdk-11.0.10
	- HADOOP_HOME=C:\winutils-master\hadoop-3.2.1\
- run spark-shell --conf spark.jars=deequ-1.1.0_spark-3.0-scala-2.12.jar
- downbload tsvfile from https://s3.amazonaws.com/amazon-reviews-pds/tsv/amazon_reviews_us_Camera_v1_00.tsv.gz
- unzip 'amazon_reviews_us_Camera_v1_00.tsv.gz'


