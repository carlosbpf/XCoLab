#!/bin/bash
# Run Mysql Create Script
echo "Running create database script"
mysql -u root xcolab < $TRAVIS_BUILD_DIR/scripts/travis/xcolab_Create.sql
echo "Copying application properties file"
cp $TRAVIS_BUILD_DIR/microservices/services/src/main/resources/application.properties $HOME/.xcolab.application.properties
echo "Copying application properties file"
mkdir $TRAVIS_BUILD_DIR/../deploy
touch $HOME/.xcolab.deploy.properties
cat "liferay.auto.deploy.dir=$TRAVIS_BUILD_DIR/../deploy" > $HOME/.xcolab.deploy.properties
tail $HOME/.xcolab.deploy.properties