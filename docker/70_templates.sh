#!/bin/sh
dockerize -template /home/app/docker/solrcore.properties.tmpl:/home/app/solr_home/collection1/conf/solrcore.properties
dockerize -template /home/app/docker/solrconfig.xml.tmpl:/home/app/src/main/resources/solrconfig.xml
dockerize -template /home/app/docker/log4j.xml.tmpl:/home/app/src/main/resources/log4j.xml
