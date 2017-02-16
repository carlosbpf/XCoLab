#!/usr/bin/env bash

# stop script if any build fails
set -e
echo "#####################################################################################"
echo "Deploying parent dependencies"
echo "#####################################################################################"
LOG="-q -B"
mvn install -N $LOG
cd microservices/
mvn install -N $LOG
cd util/
mvn install -N $LOG
cd ../clients/
mvn install -N $LOG
echo "#####################################################################################"
echo "Deploying xcolab-utils"
echo "#####################################################################################"
cd ../util/xcolab-utils/
mvn clean compile package install clean $LOG 
echo "#####################################################################################"
echo "Deploying entity-utils"
echo "#####################################################################################"
cd ../../../
cd microservices/clients/emails-client
mvn clean compile package install clean $LOG
cd ../members-client
mvn clean compile package install clean $LOG
cd ../admin-client/
mvn clean compile package install clean $LOG
cd ../comment-client/
mvn clean compile package install clean $LOG
cd ../activities-client/
mvn clean compile package install clean $LOG
cd ../modeling-client/
mvn clean compile package install clean $LOG
cd ../contestproposal-client/
mvn clean compile package install clean $LOG
cd ../../util/entity-utils/
mvn clean compile package install clean $LOG
cd ../../..

echo "#####################################################################################"
echo "Deploying clients"
echo " "
cd microservices/clients
for D in *-client; do
    if [ -d "${D}" ]; then
        cd $D
        echo " - $D"
		pwd
		mvn clean compile package install clean $LOG
		cd ..
    fi
done
echo "#####################################################################################"
cd ../..
