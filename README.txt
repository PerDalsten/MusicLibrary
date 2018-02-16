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
	
	<cors allowedOrigins="http://localhost,http://localhost:8080" domain="/MusicLibrary/rest" allowedMethods="GET,POST,PUT,DELETE,HEAD,OPTIONS" allowedHeaders="Content-Type"></cors>
	
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


For MySQL install driver using script and use:

<datasource jndi-name="java:/jdbc/MusicLibraryDS" pool-name="MusicLibrary">
    <connection-url>jdbc:mysql://localhost:3306/musiclibrarydb</connection-url>
    <driver>mysql</driver>
    <security>
        <user-name>musiclibrary</user-name>
        <password>musiclibrary</password>
    </security>
</datasource>


Add to <subsystem xmlns="urn:jboss:domain:undertow:3.1"> to support CORS (client deployed on different server): 

        ... 
	    <filter-ref name="Access-Control-Allow-Origin"/>
 	    <filter-ref name="Access-Control-Allow-Methods"/>
	    <filter-ref name="Access-Control-Allow-Headers"/>
    </host>

        ...
		<response-header name="Access-Control-Allow-Origin" header-name="Access-Control-Allow-Origin" header-value="http://localhost"/>
        <response-header name="Access-Control-Allow-Methods" header-name="Access-Control-Allow-Methods" header-value="GET, POST, PUT, DELETE, HEAD, OPTIONS"/>
        <response-header name="Access-Control-Allow-Headers" header-name="Access-Control-Allow-Headers" header-value="accept, content-type"/>
     </filters>


Glassfish config
==============

Add to domain.xml:

   <jdbc-connection-pool datasource-classname="org.apache.derby.jdbc.ClientDataSource" name="MusicLibrary" res-type="javax.sql.DataSource">
      <property name="PortNumber" value="1527"></property>
      <property name="Password" value="MusicLibrary"></property>
      <property name="ServerName" value="localhost"></property>
      <property name="ConnectionAttributes" value=";create=false"></property>
      <property name="DatabaseName" value="musiclibrarydb"></property>
      <property name="User" value="musiclibrary"></property>
    </jdbc-connection-pool>
    <jdbc-resource pool-name="MusicLibrary" jndi-name="jdbc/MusicLibraryDS"></jdbc-resource>

or use asadmin to create datasource:

./asadmin create-jdbc-connection-pool --restype javax.sql.DataSource --datasourceclassname org.apache.derby.jdbc.ClientDataSource --property "ServerName=localhost:PortNumber=1527:DatabaseName=musiclibrarydb:User=musiclibrary:Password=MusicLibrary:ConnectionAttributes=;create\=false" MusicLibrary 

./asadmin create-jdbc-resource --connectionpoolid MusicLibrary jdbc/MusicLibraryDS


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


URL's
=====

All: http://localhost:9080/MusicLibrary/rest/albums
By ID: http://localhost:9080/MusicLibrary/rest/albums/42
Search: http://localhost:9080/MusicLibrary/rest/albums?artist=Thin Lizzy&title=&year=
By artist: http://localhost:9080/MusicLibrary/rest/artists/83/albums

UI:http://localhost:9080/MusicLibrary/albums/index.xhtml (or just http://localhost:9080/MusicLibrary)

For Wildfly and Glassfish default port is 8080.


QA
==

mvn sonar:sonar
mvn site

