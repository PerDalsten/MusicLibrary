@echo off

IF "%GLASSFISH_HOME%" == "" GOTO HELP

SET ASADMIN=%GLASSFISH_HOME%\bin\asadmin

%ASADMIN% create-jdbc-resource --connectionpoolid MusicLibrary jdbc/MusicLibraryDS

GOTO END

:HELP

echo "Please set GLASSFISH_HOME before callig script. E.g.:"
echo "set GLASSFISH_HOME=%%USERPROFILE%%\Development\glassfish4& create-derby-datasource.bat" 

:END

