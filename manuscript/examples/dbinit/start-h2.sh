#!/bin/bash

# Set these variables as necessary
H2_JAR=$HOME/.m2/repository/com/h2database/h2/1.4.190/h2-1.4.190.jar
GROOVY_INIT="groovy -cp .:..:$H2_JAR"
JAVA="java"

if [ -z "$1" ]; then
  echo "Provide name of the directory with create/init scripts as a parameter!"
  exit
fi

$JAVA -jar $H2_JAR &
sleep 1

cd $1
$GROOVY_INIT initdb.groovy