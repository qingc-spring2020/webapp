#!/bin/bash

sudo systemctl stop tomcat.service

sudo rm -rf /opt/tomcat/latest/webapps/docs  /opt/tomcat/latest/webapps/examples /opt/tomcat/latest/webapps/host-manager  /opt/tomcat/latest/webapps/manager /opt/tomcat/latest/webapps/ROOT

sudo chown tomcat:tomcat /opt/tomcat/latest/webapps/assignment3-0.0.1-SNAPSHOT.jar

# cleanup log files
sudo rm -rf /opt/tomcat/latest/logs/catalina*
sudo rm -rf /opt/tomcat/latest/logs/*.log
sudo rm -rf /opt/tomcat/latest/logs/*.txt

sudo service amazon-cloudwatch-agent.service start
