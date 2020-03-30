#!/bin/bash

cd /home/ubuntu
sudo systemctl enable amazon-cloudwatch-agent.service
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:amazon-cloudwatch-agent.json -s
sudo systemctl restart amazon-cloudwatch-agent.service
cd /opt/tomcat/latest/webapps
/usr/bin/nohup java -jar assignment3-0.0.1-SNAPSHOT.jar > /dev/project.log 2>&1 &