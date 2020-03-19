#!/bin/bash
cd /opt/tomcat/latest/webapps
/usr/bin/nohup java -jar assignment3-0.0.1-SNAPSHOT.jar > /dev/project.log &
exit 0