#!/bin/bash

cygwin=false
case "`uname`" in
  CYGWIN*) cygwin=true;;
esac

OS=`uname`

PRG="$0"
PRGDIR=`dirname "$PRG"`
APP_HOME=$PRGDIR/../..

bin=`pwd`
TEST_SOURCE=`expr match "$bin" '.*src/main/scala.*'`

if [ $TEST_SOURCE -gt 0 ] ; then
  APP_HOME=$APP_HOME/../../../target/vsw.nlp.core-release/vsw.nlp.core
fi

ABS_APP_HOME=$APP_HOME

#JAVA_BASE="d:/java"
#if [ "$OS" == "Darwin" ] ; then
#  JAVA_BASE="/Users/tuannguyen/java" 
#elif [ "$OS" == "Linux" ] ; then
#  JAVA_BASE="/opt" 
#  JAVA_HOME="/opt/jdk1.6"
#fi
#

if [ -d "$APP_HOME/logs" ] ; then
  echo ""
else
  mkdir $APP_HOME/logs
fi

WORKING_DIR=$APP_HOME/working
if [ -d "$APP_HOME/report" ] ; then
  echo ""
else
  mkdir $APP_HOME/report
  mkdir $WORKING_DIR
fi

LIB="$APP_HOME/lib" ;


CLASSPATH="$JAVA_HOME/lib/tools.jar"

for f in $LIB/*.jar; do
  CLASSPATH=${CLASSPATH}:$f;
done
CLASSPATH=${CLASSPATH}:$WORKING_DIR;

if $cygwin; then
  JAVA_HOME=`cygpath --absolute --windows "$JAVA_HOME"`
  CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
  ABS_APP_HOME=`cygpath --absolute --windows "$ABS_APP_HOME"`
  WORKING_DIR=`cygpath --absolute --windows "$WORKING_DIR"`
fi

CLASS=$1
shift
echo "USING APP HOME: $ABS_APP_HOME" 
echo "CLASSPATH     : $CLASSPATH" 
scalac -deprecation -classpath $CLASSPATH -d $WORKING_DIR $PRGDIR/$CLASS.scala && \
scala -classpath $CLASSPATH $CLASS  -apphome $ABS_APP_HOME "$@"
