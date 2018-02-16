@echo off

IF "%GLASSFISH_HOME%" == "" GOTO HELP

SET ASADMIN=%GLASSFISH_HOME%\bin\asadmin

%ASADMIN% create-jdbc-connection-pool --restype javax.sql.DataSource --datasourceclassname org.apache.derby.jdbc.ClientDataSource --property "ServerName=localhost:PortNumber=1527:DatabaseName=musiclibrarydb:User=musiclibrary:Password=MusicLibrary:ConnectionAttributes=;create\=false" MusicLibrary

GOTO END

:HELP

echo "Please set GLASSFISH_HOME before callig script. E.g.:"
echo "set GLASSFISH_HOME=%%USERPROFILE%%\Development\glassfish4& create-derby-datasource.bat" 

:END

