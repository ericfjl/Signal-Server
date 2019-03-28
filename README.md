Signal-Server
=================

Documentation
-------------

### Looking for protocol documentation? Check out the website!
    1.signal: https://signal.org/docs/
    2.Introduction to Signal Private Messenger: https://www.youtube.com/watch?v=46ozjP-R2-E
    3.pdf: https://conference.hitb.org/hitbsecconf2017ams/materials/D2T1%20-%20Markus%20Vervier%20-%20Hunting%20for%20Vulnerabilities%20in%20Signal.pdf
    4.TextSecure Protocol: https://www.youtube.com/watch?v=7WnwSovjYMs
    5.install : https://gist.github.com/aqnouch/9a371af0614f4fe706a951c2b97651e7


Notice
------------

## Prerequisites
To be sure to have the latest version of the programmes.
	sudo apt-get update 

### Install Java
    java --version
        openjdk version "1.8.0_191"
    if is openjdk
        sudo apt remove openjdk*
    if not install java
        sudo apt-get install oracle-java8-installer

### Install Redis
	sudo apt-get install -y redis-server

### Install database
	sudo apt-get install postgresql postgresql-contrib -y
    psql -U postgres -h localhost
    create database abusedb   owner postgres;
    create database accountdb owner postgres;
    create database messagedb owner postgres;

### Install Signal-Server
    git clone https://github.com/ericfjl/Signal-Server.git
    cd Signal-server
    (mvn package)/(mvn install -DskipTests)
    java -jar target/TextSecureServer-2.26.jar abusedb   migrate config/signal_local.yml
    java -jar target/TextSecureServer-2.26.jar accountdb migrate config/signal_local.yml
    java -jar target/TextSecureServer-2.26.jar messagedb migrate config/signal_local.yml
    java -jar target/TextSecureServer-2.26.jar server config/signal_local.yml


### fix bugs
    1.fix bug(can't run for Certificate)org.whispersystems.textsecuregcm.storage.DirectoryReconciliationClient
      // PEMReader       reader      = new PEMReader(new InputStreamReader(new ByteArrayInputStream(caCertificatePem.getBytes())));
      final Reader filereader = new FileReader(caCertificatePem);
      final PEMReader reader = new PEMReader(filereader);
    2.fix bug(fix package javax.xml.bind.annotation.adapters does not exist) pom.xml
    https://stackoverflow.com/questions/52502189/java-11-package-javax-xml-bind-does-not-exist
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



License
---------------------

Copyright 2019-2019 Open Whisper Systems

Licensed under the AGPLv3: https://www.gnu.org/licenses/agpl-3.0.html
