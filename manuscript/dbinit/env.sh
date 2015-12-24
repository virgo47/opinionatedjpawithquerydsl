#!/bin/bash

export H2_JAR=$HOME/.m2/repository/com/h2database/h2/1.4.190/h2-1.4.190.jar
export GROOVY_INIT="groovy -cp .:..:$H2_JAR"
export JAVA="java"