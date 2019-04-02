# Signal-Server
## Documentation
- [signal docs](https://signal.org/docs)
- [youtube about Introduction to Signal Private Messenger](https://www.youtube.com/watch?v=46ozjP-R2-E)
- [Markus Vervier - Hunting for Vulnerabilities in Signal pdf](https://conference.hitb.org/hitbsecconf2017ams/materials/D2T1%20-%20Markus%20Vervier%20-%20Hunting%20for%20Vulnerabilities%20in%20Signal.pdf)
- [youtube about TextSecure Protocol](https://www.youtube.com/watch?v=7WnwSovjYMs)
- [Signal Server Installation Guide](https://gist.github.com/aqnouch/9a371af0614f4fe706a951c2b97651e7)

## Installation Guide

### Prerequisites
To be sure to have the latest version of the programmes.
`````
$ sudo apt-get update 
`````
### Install Java oracle-java8
    $ sudo add-apt-repository ppa:webupd8team/java
    $ apt-get update
    $ sudo apt-get install oracle-java8-installer
    $ sudo apt-get install oracle-java8-set-default

### Install Redis
	$ sudo apt-get install -y redis-server

### Install database
	$ sudo apt-get install postgresql postgresql-contrib -y
    $ sudo -u postgres psql
    postgres=# alter user postgres password 'pwd123';
    $ psql -U postgres -h localhost
    Password for user postgres: 
    postgres=# create database abusedb   owner postgres;
    postgres=# create database accountdb owner postgres;
    postgres=# create database messagedb owner postgres;

### Install Private Contact Discovery Service
- [Contact Discovery Service](https://github.com/ericfjl/ContactDiscoveryService)

## Install Signal-Server
### get code
    $ git clone https://github.com/ericfjl/Signal-Server.git
### Building the service
    $ cd Signal-server
    $ mvn install -DskipTests
### init database
    $ java -jar target/TextSecureServer-2.26.jar abusedb   migrate config/signal_local.yml
    $ java -jar target/TextSecureServer-2.26.jar accountdb migrate config/signal_local.yml
    $ java -jar target/TextSecureServer-2.26.jar messagedb migrate config/signal_local.yml
### Running the service
    $ java -jar target/TextSecureServer-2.26.jar server config/signal_local.yml

## fix https://github.com/signalapp/Signal-Server 's bugs 

### fix bug(can't run for Certificate)org.whispersystems.textsecuregcm.storage.DirectoryReconciliationClient)
`````
// PEMReader       reader      = new PEMReader(new InputStreamReader(new ByteArrayInputStream(caCertificatePem.getBytes())));
final Reader filereader = new FileReader(caCertificatePem);
final PEMReader reader = new PEMReader(filereader);
`````
 ### fix bug(package javax.xml.bind.annotation.adapters does not exist) pom.xml [stackoverflow](https://stackoverflow.com/questions/52502189/java-11-package-javax-xml-bind-does-not-exist)
`````
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
</dependency>
<dependency>
    <groupId>com.sun.xml.bind</groupId>
    <artifactId>jaxb-core</artifactId>
    <version>2.3.0</version>
</dependency>
<dependency>
    <groupId>com.sun.xml.bind</groupId>
    <artifactId>jaxb-impl</artifactId>
    <version>2.3.0</version>
</dependency>
`````

License
---------------------

Copyright 2019-2019 Open Whisper Systems

Licensed under the AGPLv3: https://www.gnu.org/licenses/agpl-3.0.html
