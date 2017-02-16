#!/bin/bash
# Run Mysql Create Script
echo "Running create database script"
mysql -u root xcolab < $TRAVIS_BUILD_DIR/scripts/travis/xcolab_Create.sql

#Roma client install
cd $TRAVIS_BUILD_DIR/..
echo "Before git clone ROMA repo"
git clone https://github.com/CCI-MIT/ROMA.git
echo "After git clone ROMA repo"
cd $TRAVIS_BUILD_DIR/../ROMA/core/
echo "Before ROMA core mvn"
mvn compile package install -DskipTests=true
cd $TRAVIS_BUILD_DIR/../client/
echo "After ROMA core mvn"
echo "Before ROMA client mvn"
mvn compile package install -DskipTests=true
echo "Before ROMA client mvn"
cd $TRAVIS_BUILD_DIR
rm -rf ROMA/
cd $TRAVIS_BUILD_DIR