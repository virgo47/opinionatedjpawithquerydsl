#!/bin/bash

if [ -z "$1" ]; then
	echo "Provide name of the directory with create/init scripts as a parameter!"
	exit
fi

source env.sh

$JAVA -jar $H2_JAR &
sleep 1

cd $1
$GROOVY_INIT initdb.groovy