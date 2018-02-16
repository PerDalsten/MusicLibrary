#/bin/sh

ASADMIN=${GLASSFISH_HOME:-$HOME/Development/glassfish4}/bin/asadmin

$ASADMIN create-jdbc-connection-pool --restype javax.sql.DataSource --datasourceclassname org.apache.derby.jdbc.ClientDataSource --property "ServerName=localhost:PortNumber=1527:DatabaseName=musiclibrarydb:User=musiclibrary:Password=MusicLibrary:ConnectionAttributes=;create\=false" MusicLibrary

$ASADMIN create-jdbc-resource --connectionpoolid MusicLibrary jdbc/MusicLibraryDS
