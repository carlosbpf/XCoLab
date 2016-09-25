#!/bin/bash

# stop script if any build fails
set -e

cd themes/climatecolab-theme
  mvn clean compile package install
  mvn clean compile liferay:build-css package liferay:deploy clean
cd ../..

cd themes/solve-theme
  mvn clean compile liferay:build-css package liferay:deploy clean
cd ../..

cd themes/fow-theme
  mvn clean compile liferay:build-css package liferay:deploy clean
cd ../..