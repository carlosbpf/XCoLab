# Run Mysql Create Script
mysql -u travis -p xcolab < xcolab_Create.sql

#Roma client install
git clone https://github.com/CCI-MIT/ROMA.git
cd ROMA/core/
mvn compile package install -DskipTests=true
cd ../client/
mvn compile package install -DskipTests=true
