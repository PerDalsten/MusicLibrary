Application server versions
===========================

Websphere Liberty 17.0.0.4
Wildfly 11.0.0.Final
Glassfish 4.1.2
TomEE Plus 7.0.4


Project creation
================

mvn archetype:generate -DgroupId=dk.purplegreen.musiclibrary -DartifactId=MusicLibrary -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false

Fix pom.xml (add Java EE 7, fix Junit version, compiler level, etc.) and web.xml (Web app version) in text editor


WLP Server config
=================

Copy derbyclient.jar to lib/derby (create) directory in server.

<server description="WLP">
	
	<httpEndpoint host="*" httpPort="9080" httpsPort="9443" id="defaultHttpEndpoint"/>
	
	<logging traceSpecification="JPA=all" traceFileName="JPA.log" maxFileSize="20" maxFiles="5" traceFormat="BASIC" />
	
	<library id="DerbyLib">
		<fileset dir="${server.config.dir}/lib/derby" includes="*.jar"/>
	</library>
	<dataSource jndiName="jdbc/MusicLibraryDS">
		<jdbcDriver libraryRef="DerbyLib"/>
		<properties.derby.client createDatabase="false" databaseName="musiclibrarydb" password="{xor}MiosNjwzNj0tPi0m" user="musiclibrary"/>
	</dataSource>

    <!-- MySQL
    <library id="MySQLLib">
    	<fileset dir="lib/mysql" includes="*.jar"/>
    </library>
    <dataSource jndiName="jdbc/MusicLibraryDS">
		<jdbcDriver libraryRef="MySQLLib"/>		
		<properties URL="jdbc:mysql://localhost" databaseName="musiclibrarydb" password="{xor}MiosNjwzNj0tPi0m" portNumber="3306" serverName="localhost" user="musiclibrary"/>
	</dataSource>
    -->
    
    <!-- HyperSQL
    <library id="HSQLLib">
		<fileset dir="${server.config.dir}/lib/hsql" includes="*.jar"/>
	</library>
	<dataSource jndiName="jdbc/MusicLibraryDS" type="javax.sql.ConnectionPoolDataSource">
		<jdbcDriver libraryRef="HSQLLib" javax.sql.ConnectionPoolDataSource="org.hsqldb.jdbc.pool.JDBCPooledDataSource"/>
		<properties url="jdbc:hsqldb:hsql://localhost/musiclibrarydb" password="{xor}MiosNjwzNj0tPi0m" user="musiclibrary"/>
	</dataSource>
    -->
    
    <webApplication id="MusicLibrary" location="MusicLibrary.war" name="MusicLibrary"/>
</server>    


Wildfly config
==============

Deploy derbyclient.jar as application and add datasource.

<datasource jta="true" jndi-name="java:/jdbc/MusicLibraryDS" pool-name="MusicLibrary" enabled="true" use-ccm="true">
	<connection-url>jdbc:derby://localhost:1527/musiclibrarydb</connection-url>
    <driver-class>org.apache.derby.jdbc.ClientDriver</driver-class>
    <driver>derbyclient.jar</driver>
    <security>
    	<user-name>musiclibrary</user-name>
        <password>musiclibrary</password>
    </security>
</datasource>

or run scripts in src/main/scripts/wildfly as needed e.g.


cd $JAVA_HOME/db/lib; /path/to/jboss-cli.sh --file=/path/to/create-derby-database-driver.cli 

/path/to/jboss-cli.sh --file=/path/to/create-derby-datasource.cli

Add to logging subsystem (if using spy on datasource):

<logger category="jboss.jdbc.spy">
	<level name="TRACE"/>
</logger>


For MySQL deploy/install driver using script and use:

<datasource jndi-name="java:/jdbc/MusicLibraryDS" pool-name="MusicLibrary">
    <connection-url>jdbc:mysql://localhost:3306/musiclibrarydb</connection-url>
    <driver>mysql</driver>
    <security>
        <user-name>musiclibrary</user-name>
        <password>musiclibrary</password>
    </security>
</datasource>

For HyperSQL deploy/install driver using script and use:

<datasource jndi-name="java:/jdbc/MusicLibraryDS" pool-name="MusicLibrary" spy="true" use-ccm="true">
    <connection-url>jdbc:hsqldb:hsql://localhost/musiclibrarydb</connection-url>
    <driver>hsql</driver>
    <security>
        <user-name>musiclibrary</user-name>
        <password>musiclibrary</password>
    </security>
</datasource>


Glassfish config
================

Add to domain.xml:

   <jdbc-connection-pool datasource-classname="org.apache.derby.jdbc.ClientDataSource" name="MusicLibrary" res-type="javax.sql.DataSource">
      <property name="PortNumber" value="1527"></property>
      <property name="Password" value="musiclibrary"></property>
      <property name="ServerName" value="localhost"></property>
      <property name="ConnectionAttributes" value=";create=false"></property>
      <property name="DatabaseName" value="musiclibrarydb"></property>
      <property name="User" value="musiclibrary"></property>
    </jdbc-connection-pool>
    <jdbc-resource pool-name="MusicLibrary" jndi-name="jdbc/MusicLibraryDS"></jdbc-resource>

or use asadmin to create datasource:

./asadmin create-jdbc-connection-pool --restype javax.sql.DataSource --datasourceclassname org.apache.derby.jdbc.ClientDataSource --property "ServerName=localhost:PortNumber=1527:DatabaseName=musiclibrarydb:User=musiclibrary:Password=MusicLibrary:ConnectionAttributes=;create\=false" MusicLibrary 

For MySQL copy driver to <Glassfish install directory>/glassfish/lib/ and use

    <jdbc-connection-pool datasource-classname="com.mysql.jdbc.jdbc2.optional.MysqlDataSource" name="MySQLMusicLibrary" res-type="javax.sql.DataSource">
      <property name="user" value="musiclibrary"></property>
      <property name="url" value="jdbc:mysql://localhost:3306/musiclibrarydb"></property>
      <property name="password" value="musiclibrary"></property>
    </jdbc-connection-pool>

or

./asadmin create-jdbc-connection-pool --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlDataSource --restype javax.sql.DataSource --property "user=musiclibrary:password=musiclibrary:url=jdbc\\:mysql\\://localhost\\:3306/musiclibrarydb" MusicLibrary


Create datasource:

./asadmin create-jdbc-resource --connectionpoolid MusicLibrary jdbc/MusicLibraryDS

Due to a bug in Glassfish 4.1 it is necessary to patch glassfish/modules/org.eclipse.persistence.moxy.jar. Append the following
to META-INF/MANIFEST.MF Import-Package:

,org.xml.sax.helpers,javax.xml.parsers;resolution:=optional,javax.naming;resolution:=optional


TomEE config
============

Copy $JAVA_HOME/db/lib/derbyclient.jar and $JAVA_HOME/db/lib/derbyLocale*.jar to $CATALINA_HOME/lib.

Add datasource to conf/tomee.xml:

<Resource id="jdbc/MusicLibraryDS" type="DataSource">
      JdbcDriver org.apache.derby.jdbc.ClientDriver
      JdbcUrl jdbc:derby://localhost:1527/musiclibrarydb
      UserName musiclibrary
      Password musiclibrary
      JtaManaged true
</Resource>


For MySQL copy driver to <TomEE install directory>/lib/ and use

<Resource id="jdbc/MusicLibraryDS" type="DataSource">
      JdbcDriver com.mysql.jdbc.Driver
      JdbcUrl jdbc:mysql://localhost:3306/musiclibrarydb
      UserName musiclibrary
      Password musiclibrary
      JtaManaged true
</Resource>


Logging
=======

Add system property dk.purplegreen.logdir to server.

WLP: add -Ddk.purplegreen.logdir=./logs to jvm.properties

Wildfly: add to standalone.xml:

    <system-properties>
      <property name="dk.purplegreen.logdir" value="${jboss.server.log.dir}"/>
    </system-properties>

or run

/path/to/jboss-cli.sh --file=/path/to/set-logdir.cli

Glassfish: add to domain.xml: 

    <system-property name="dk.purplegreen.logdir" value="../logs"></system-property>

or run

./asadmin create-system-properties dk.purplegreen.logdir=../logs 


TomEE: add to conf/system.properties:

dk.purplegreen.logdir = ../logs


URL's
=====

All: http://localhost:9080/MusicLibrary/rest/albums
By ID: http://localhost:9080/MusicLibrary/rest/albums/42
Search: http://localhost:9080/MusicLibrary/rest/albums?artist=Thin Lizzy&title=&year=
By artist: http://localhost:9080/MusicLibrary/rest/artists/83/albums

UI:http://localhost:9080/MusicLibrary/albums/index.xhtml (or just http://localhost:9080/MusicLibrary)

For Wildfly, Glassfish, and TomEE default port is 8080.


QA
==

mvn sonar:sonar
mvn site

