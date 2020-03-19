CircleCI 2.0 Java Demo Application using Maven and Spring#

This is an example application showcasing how to run a Java web on CircleCI 2.0.

This application uses the following tools:

Maven
Java 8
MySQL
Spring

Unit testing
'Junit'


## Build Instructions
Instructions for IntelliJ Editor
1) Git pull from master - git pull origin master
4) Create jar file
   
## Deploy Instructions
1) Run "java .jar" on Command

## Restful API
|HTTP Method |	   Endpoint  |  Authenticated Endpoint |  Description|
| --- | --- | --- | ---|
POST 	|/v1/user 	No 	Create account for user
POST |	/v1/bill/ 	|Yes 	|Create a new bill
GET 	|/v1/bills 	|Yes 	|Get all bills
GET |	/v1/bill/{id} |	Yes |	Get a bill
DELETE 	|/v1/bill/{id} |	Yes |	Delete a bill
PUT 	|/v1/bill/{id} |	Yes 	|Update bill information
GET 	|/v1/user/self |	Yes 	|Get user information 
PUT 	|/v1/user/self |	Yes 	|Update user information

## CircleCi
Circleci used for the continuous deployment.
Please read ./circleci/config.yml
