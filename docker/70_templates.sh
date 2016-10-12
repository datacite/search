#!/bin/sh
dockerize -template /home/app/docker/solrconfig.xml.tmpl:/home/app/src/main/resources/solrconfig.xml
dockerize -template /home/app/docker/solrcore.properties.tmpl:/home/app/src/main/resources/solrcore.properties
dockerize -template /home/app/docker/log4j.xml.tmpl:/home/app/src/main/resources/log4j.xml
dockerize -template /home/app/docker/crontab.tmpl:/home/app/docker/crontab
dockerize -template /home/app/docker/context.xml.tmpl:/home/app/src/main/webapp/META-INF/context.xml

crontab /home/app/docker/crontab
