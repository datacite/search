#!/bin/sh

dockerize -template /home/app/docker/context.xml.tmpl:/home/app/src/main/webapp/META-INF/context.xml
dockerize -template /home/app/docker/log4j.properties.tmpl:/home/app/src/main/resources/log4j.properties
dockerize -template /home/app/docker/solrcore.properties.tmpl:/home/app/solr_home/collection1/conf/solrcore.properties
dockerize -template /home/app/docker/dataimport.properties.tmpl:/home/app/solr_home/collection1/conf/dataimport.properties
dockerize -template /home/app/docker/db_check.tmpl:/usr/local/bin/db_check
dockerize -template /home/app/docker/head.vm.tmpl:src/main/resources/velocity/ui/head.vm

cp /home/app/src/main/resources/schema.xml /home/app/solr_home/collection1/conf/schema.xml
dockerize -template /home/app/docker/solrconfig.xml.tmpl:/home/app/solr_home/collection1/conf/solrconfig.xml
dockerize -template /home/app/docker/data-config.xml.tmpl:/home/app/solr_home/collection1/conf/data-config.xml
mkdir /data/solr/collection1/conf
cp /home/app/solr_home/collection1/conf/*.properties /data/solr/collection1/conf/
cp /home/app/solr_home/collection1/conf/*.xml /data/solr/collection1/conf/
cp /home/app/solr_home/collection1/conf/*.html /data/solr/collection1/conf/

chown tomcat7. /data/solr/* -R
chmod a+w /data/solr/* -R
chmod a+xr /usr/local/bin/db_check
