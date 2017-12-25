#!/bin/sh
dockerize -template /home/app/docker/webapp.conf.tmpl:/etc/nginx/sites-enabled/webapp.conf
service nginx start
