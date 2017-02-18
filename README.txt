Project creation
================

mvn archetype:generate -DgroupId=dk.purplegreen.musiclibrary -DartifactId=MusicLibrary -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false

Fix pom.xml (add Java EE 7, fix Junit version, compiler level, etc.) and web.xml (Web app version) in text editor


WLP Server config
=================

Copy derbyclient.jar to lib/derby (create) directory in server.

<server description="WLP">
	
	<httpEndpoint host="*" httpPort="9080" httpsPort="9443" id="defaultHttpEndpoint"/>
	
	<library id="DerbyLib">
		<fileset dir="${server.config.dir}/lib/derby" includes="*.jar"/>
	</library>
	<dataSource jndiName="jdbc/MusicLibraryDS">
		<jdbcDriver libraryRef="DerbyLib"/>
		<properties.derby.client createDatabase="false" databaseName="musiclibrarydb" password="{xor}KzosKw==" user="musiclibrary"/>
	</dataSource>

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
        <password>test</password>
    </security>
</datasource>

or run scripts in src/main/scripts/wildfly as needed e.g.


cd $JAVA_HOME/db/lib; /path/to/jboss-cli.sh --file=/path/to/create-database-driver.cli 

/path/to/jboss-cli.sh --file=/path/to/create-datasource.cli


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


URL's
=====

All: http://localhost:9080/MusicLibrary/rest/albums
By ID: http://localhost:9080/MusicLibrary/rest/albums/42
Search: http://localhost:9080/MusicLibrary/rest/albums?artist=Thin Lizzy&title=&year=
By artist: http://localhost:9080/MusicLibrary/rest/artist/83/albums

UI:http://localhost:9080/MusicLibrary/albums/index.xhtml (or just http://localhost:9080/MusicLibrary)

For Wildfly default port is 8080.


QA
==

mvn sonar:sonar
mvn site

