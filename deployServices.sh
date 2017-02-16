#!/bin/sh

# stop script if any build fails
set -e
LOG="-q -B"
echo "#####################################################################################"
echo "Deploying service-utils"
echo "#####################################################################################"
cd microservices/util/service-utils
mvn clean compile package install clean $LOG
cd ../../..

echo "#####################################################################################"
echo "Deploying services"
echo ""
cd microservices/services
for D in *-service; do
    if [ -d "${D}" ]; then
        cd $D
        echo " - $D"
        pwd
        mvn clean compile package spring-boot:repackage $LOG
        cd ..
    fi
done
cd ../..
echo "#####################################################################################"