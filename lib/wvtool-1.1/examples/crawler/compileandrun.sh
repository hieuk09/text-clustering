#!/bin/sh
CLASSPATH=../../wvtool.jar

for JAR in ../../lib/*.jar
do
CLASSPATH=${CLASSPATH}:${JAR}
done
javac -cp ${CLASSPATH} WVToolCrawlerExample.java
java -cp ${CLASSPATH}:. WVToolCrawlerExample 
