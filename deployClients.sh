#!/usr/bin/env bash

# stop script if any build fails
set -e

mvn install -N
cd microservices/
mvn install -N
cd util/
mvn install -N
cd ../clients/
mvn install -N
cd ../util/xcolab-utils/
mvn clean compile package install clean
cd ../../../
cd microservices/clients/emails-client
mvn clean compile package install clean
cd ../members-client
mvn clean compile package install clean
cd ../admin-client/
mvn clean compile package install clean
cd ../comment-client/
mvn clean compile package install clean
cd ../activities-client/
mvn clean compile package install clean
cd ../modeling-client/
mvn clean compile package install clean
cd ../contestproposal-client/
mvn clean compile package install clean
cd ../../util/entity-utils/
mvn clean compile package install clean
cd ../../..

cd microservices/clients
for D in *-client; do
    if [ -d "${D}" ]; then
        cd $D
		pwd
		mvn clean compile package install clean
		cd ..
    fi
done
cd ../..
