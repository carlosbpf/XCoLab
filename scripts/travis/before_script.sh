#!/bin/bash
# Run Mysql Create Script
echo "Running create database script"
mysql -u root xcolab < $TRAVIS_BUILD_DIR/scripts/travis/xcolab_Create.sql
cp $TRAVIS_BUILD_DIR/conf/application.properties $HOME/.xcolab.application.properties
