Development and deployment Environment
--------------------------------------

 * Ubuntu 12.04 LTS or Windows Server 2012 R2

 * JBoss AS Final 7.1.1

 * MySQL 5.5.35

 * Eclipse Kepler with JBoss Tools 4.1.1.Final and Arquillian installed from MarketPlace

 * Apache JMeter 2.3.4

JDBC Driver setup
-----------------

Necessary for proper communication with MySql.

Download the MySQL JDBC Driver 5.1.28 from http://dev.mysql.com/downloads/connector/j/ .

Quick instructions:

The jar file mysql-connector-java-5.1.28-bin.jar is installed as a module under
${JBOSS_DIR}/modules/com/mysql/main . To make it work copy modules.xml from src/main/resources under ${JBOSS_HOME}/modules/com/mysql/main .

Finally start the JBOSS management CLI (${JBOSS_HOME}/bin/jboss-cli.*) and enter

/subsystem=datasources/jdbc-driver=mysql:add(driver-name=mysql,driver-module-name=com.mysql,driver-xa-datasource-class-name=com.mysql.jdbc.jdbc2.optional.MysqlXADataSource)

Finally it is also necessary to deploy mysql-connector-java-5.1.28-bin.jar under $
{JBOSS_HOME}/standalone/deployments

For full installation instructions see https://docs.jboss.org/author/display/AS71/DataSource+configuration

JBoss standalone config file
----------------------------

This is necessary to properly configure the authentication mechanism of JBoss

cp standalone.xml ${JBOSS_HOME}/standalone/configuration

How to initialize the DB
------------------------

Configure root access with password “reverse” .

Create the basic schema with 

  mysql -u root -p teatromanzoni < teatromanzoni.sql 

How to set-up the project
-------------------------

git clone https://github.com/glgerard/tm-tkt.git

import tm-tkt in eclipse

Do the following to avoid the error
No generator named “generator” is defined in the persistence unit

Window->Preferences->Java Persistence->JPA->Errors/Warnings->Queries and Generators and set level of “Duplicate generator defined” and “Generator is not defined in the persistence unit” to warning.

How to access the deployed application
--------------------------------------

Start the server from eclipse and deploy the project then browse to 

http://localhost:8080/tm-tkt/faces/index.xhtml
