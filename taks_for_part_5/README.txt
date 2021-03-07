1.	Create HDFS directory /user/student and nested directories airports, airlines, routes, planes, countries

screenshot-1.png

2.	Copy each file from the local file system's airlines dataset to the corresponding HDFS directory created in step #1 
(i.e. routes.dat should be stored HDFS as /user/student/routes/routes.dat) 

screenshot-2.png

3.	Print out to the console first 9 lines of /user/student/countries/countries.dat file

hadoop fs -cat /user/student/countries/countries.dat | head -9
screenshot-3.png

4.	Compare MD5 checksums between the local file airports.dat and HDFS file /user/student/airports/airports.dat. Is there any difference?

[cloudera@quickstart student]$ md5sum airports . dat
acfcde754e66b4f224562fa74b567b2b airports.dat
[ctoudera@quickstart studentl$ hadoop fs -cat /user/student/airports/airports.dat | md5sum
acfcde754e66b4f224562fa74b567b2b

screenshot-4.png

5.	How much replicas does the /user/student/airports/airports.dat file have? Set number of replicas to 4 for the file

screenshot-5.png

6.	Print out the total size of the /user/student dir in megabytes. Why the space consumed by the directory in HDFS doesn't equal to the space consumed by the same directory in local FS?


7.	Run the filesystem checking utility for the /user directory in HDFS. Analyze utility's output in order to answer the following questions:
•	Are there any corrupted files and blocks?
•	Are there any missed blocks?
•	How HDFS is configured? How many Data Nodes and Racks are in place?
•	Where the blocks are located? (block path)
