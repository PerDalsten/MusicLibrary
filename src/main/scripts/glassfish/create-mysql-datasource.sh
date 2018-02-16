#/bin/sh

ASADMIN=${GLASSFISH_HOME:-$HOME/Development/glassfish4}/bin/asadmin

$ASADMIN create-jdbc-connection-pool --restype javax.sql.DataSource --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlDataSource --property "user=musiclibrary:password=musiclibrary:url=jdbc\:mysql\://localhost\:3306/musiclibrarydb" MusicLibrary

$ASADMIN create-jdbc-resource --connectionpoolid MusicLibrary jdbc/MusicLibraryDS
