#!/bin/sh
exec 2>&1
cd /data
exec /sbin/setuser tomcat7 $CATALINA_HOME/bin/catalina.sh run
