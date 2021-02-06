# MapReduce Program

## Overview
Hadoop MapReduce program to find the most viewed Wiki page name and its pageview number.

## Technologies
- sbt
- HDFS
- YARN
- MapReduce
- Hadoop
- Hive
- Docker

## Features
- Create a jar file.
- Find the most viewed English Wiki page on 10/20/2020.
- Sort the output by values in either a descending order or an ascending order.

## Getting Started / Usage
In order to run this program properly, you will need to do the following prerequisites: <br/>
- Be sure to have Apache Hadoop installed in your JVM or a local cluster.
- Be sure to create plugins.sbt under project folder in order to create jar file.<br/>

If all of the prerequisites above are met, go ahead and clone this repo by using the command below:
```
git clone https://github.com/revature-scalawags/MapReduce_Program.git
```
In order to create a jar file, use the command below:
```
sbt clean compile assembly
```
Once a jar file is created, copy the file located within /target/scala-2.13 direcotry and paste it to JVM or a local cluster.<br/>

In order to run a jar file using Apache Hadoop in a Docker container, use the command below:
```
hadoop jar WordCount-assembly-1.0.jar mapreducer.EnglishPageViewCount input output
```


## Contributors
spark131008