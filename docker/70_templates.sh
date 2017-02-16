#!/bin/sh
dockerize -template /home/app/docker/solrconfig.xml.tmpl:/home/app/src/main/resources/solrconfig.xml
dockerize -template /home/app/docker/data-config.xml.tmpl:/home/app/src/main/resources/data-config.xml
# dockerize -template /home/app/docker/solrcore.properties.tmpl:/home/app/src/main/resources/solrcore.properties
# dockerize -template /home/app/docker/log4j.xml.tmpl:/home/app/src/main/resources/log4j.xml
dockerize -template /home/app/docker/crontab.tmpl:/home/app/docker/crontab

dockerize -template /home/app/docker/context.xml.tmpl:/home/app/src/main/webapp/META-INF/context.xml
dockerize -template /home/app/docker/log4j.properties.tmpl:/home/app/src/main/resources/log4j.properties
dockerize -template /home/app/docker/solrcore.properties.tmpl:/home/app/solr_home/collection1/conf/solrcore.properties
dockerize -template /home/app/docker/dataimport.properties.tmpl:/home/app/solr_home/collection1/conf/dataimport.properties
dockerize -template /home/app/docker/db_check.tmpl:/usr/local/bin/db_check


# cp /home/app/src/main/resources/schema.xml /home/app/solr_home/collection1/conf/schema.xml
# cp /home/app/src/main/resources/solrconfig.xml /home/app/solr_home/collection1/conf/solrconfig.xml
# cp /home/app/solr_home/*.properties /data/solr/collection1/conf/
# cp /home/app/solr_home/*.xml /data/solr/collection1/conf/
# chown tomcat7. /data/solr/* -R
# chmod a+w /data/solr/* -R

cp /home/app/src/main/resources/schema.xml /home/app/solr_home/collection1/conf/schema.xml
# cp /home/app/src/main/resources/solrconfig.xml /home/app/solr_home/collection1/conf/solrconfig.xml
# cp /home/app/src/main/resources/data-config.xml /home/app/solr_home/collection1/conf/data-config.xml
mkdir /data/solr/collection1/conf
cp /home/app/solr_home/collection1/conf/*.properties /data/solr/collection1/conf/
cp /home/app/solr_home/collection1/conf/*.xml /data/solr/collection1/conf/
#
# chown tomcat7. /data/solr/* -R
# chmod a+w /data/solr/* -R
chmod a+xr /usr/local/bin/db_check

# crontab /home/app/docker/crontab
