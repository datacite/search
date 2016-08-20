#!/bin/sh
dockerize -template /home/app/docker/solrconfig.xml.tmpl:/home/app/src/main/resources/solrconfig.xml
dockerize -template /home/app/docker/log4j.xml.tmpl:/home/app/src/main/resources/log4j.xml
dockerize -template /home/app/docker/crontab.tmpl:/etc/cron.d/solr-client
