#!/bin/bash
# Run Mysql Create Script
echo "Running create database script"
mysql -u root xcolab < $TRAVIS_BUILD_DIR/scripts/travis/xcolab_Create.sql
cp $TRAVIS_BUILD_DIR/microservices/services/src/main/resources/application.properties $HOME/.xcolab.application.properties

