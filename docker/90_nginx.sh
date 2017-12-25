#!/bin/sh
dockerize -template /home/app/docker/webapp.conf.tmpl:/etc/nginx/sites-enabled/webapp.conf
if [ "${SOLR_USER?}" ]; then
  printf "${SOLR_USER}:$(openssl passwd -crypt ${SOLR_PASSWORD})\n" >> /etc/nginx/.htpasswd
fi
service nginx start
