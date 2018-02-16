@echo off

IF "%GLASSFISH_HOME%" == "" GOTO HELP

SET ASADMIN=%GLASSFISH_HOME%\bin\asadmin

%ASADMIN% create-system-properties dk.purplegreen.logdir=${com.sun.aas.instanceRoot}/logs

GOTO END

:HELP

echo "Please set GLASSFISH_HOME before callig script. E.g.:"
echo "set GLASSFISH_HOME=%%USERPROFILE%%\Development\glassfish4& set-logdir.bat" 

:END

