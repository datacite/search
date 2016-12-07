#!/bin/sh
dockerize -template /home/app/docker/solrconfig.xml.tmpl:/home/app/src/main/resources/solrconfig.xml
dockerize -template /home/app/docker/data-config.xml.tmpl:/home/app/src/main/resources/data-config.xml
dockerize -template /home/app/docker/solrcore.properties.tmpl:/home/app/src/main/resources/solrcore.properties
# dockerize -template /home/app/docker/log4j.xml.tmpl:/home/app/src/main/resources/log4j.xml
dockerize -template /home/app/docker/crontab.tmpl:/home/app/docker/crontab

dockerize -template /home/app/docker/context.xml.tmpl:/home/app/src/main/webapp/META-INF/context.xml
dockerize -template /home/app/docker/log4j.properties.tmpl:/home/app/src/main/resources/log4j.properties
dockerize -template /home/app/docker/solrcore.properties.tmpl:/home/app/solr_home/collection1/conf/solrcore.properties
dockerize -template /home/app/docker/dataimport.properties.tmpl:/home/app/solr_home/collection1/conf/dataimport.properties

mkdir /data
mkdir /data/solr
cp /home/app/src/main/resources/*xml /home/app/solr_home/collection1/conf/
cp -r /home/app/solr_home/* /data/solr/
chown tomcat7. /data/solr/* -R
chmod a+w /data/solr/* -R

crontab /home/app/docker/crontab
