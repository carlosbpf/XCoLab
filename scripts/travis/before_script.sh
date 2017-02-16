#!/bin/bash
# Run Mysql Create Script
mysql -u root xcolab < $TRAVIS_BUILD_DIR/scripts/travis/xcolab_Create.sql

#Roma client install
cd $TRAVIS_BUILD_DIR
git clone https://github.com/CCI-MIT/ROMA.git
cd $TRAVIS_BUILD_DIR/ROMA/core/
mvn compile package install -DskipTests=true
cd $TRAVIS_BUILD_DIR/client/
mvn compile package install -DskipTests=true
cd ../
rm -rf ROMA/
cd $TRAVIS_BUILD_DIR