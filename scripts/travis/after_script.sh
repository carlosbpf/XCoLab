cp $TRAVIS_BUILD_DIR/view/target/xcolab-view-1.0-SNAPSHOT.jar $TRAVIS_BUILD_DIR/deploy/view
mv $TRAVIS_BUILD_DIR/deploy/apache-tomcat-8.0.33/webapps/*.war $TRAVIS_BUILD_DIR/deploy/webapps/
rm -r $TRAVIS_BUILD_DIR/deploy/apache-tomcat-8.0.33/