Project creation
================

mvn archetype:generate -DgroupId=dk.purplegreen.musiclibrary -DartifactId=MusicLibrary -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false

Fix pom.xml (add Java EE 7, fix Junit version, compiler level, etc.) and web.xml (Web app version) in text editor


WLP Server config
=================

<server description="WLP Used From Eclipse">
	<featureManager>
		<feature>javaee-7.0</feature>
		<feature>localConnector-1.0</feature>
	</featureManager>
	
	<httpEndpoint host="*" httpPort="9080" httpsPort="9443" id="defaultHttpEndpoint"/>
	
	<library id="DerbyLib">
		<fileset dir="C:\Development\jdk1.8\db\lib" includes="*.jar"/>
	</library>	
	<dataSource jndiName="jdbc/MusicLibraryDS">
		<jdbcDriver libraryRef="DerbyLib"/>
		<properties.derby.client createDatabase="false" databaseName="musiclibrarydb" password="{xor}KzosKw==" user="musiclibrary"/>
	</dataSource>

    <webApplication id="MusicLibrary" location="MusicLibrary.war" name="MusicLibrary"/>
</server>    


URL's
=====

All: http://localhost:9080/MusicLibrary/rest/albums
By ID: http://localhost:9080/MusicLibrary/rest/albums/42
Search: http://localhost:9080/MusicLibrary/rest/albums?artist=Thin Lizzy&title=&year=