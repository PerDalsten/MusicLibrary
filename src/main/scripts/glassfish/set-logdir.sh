#/bin/sh

ASADMIN=${GLASSFISH_HOME:-$HOME/Development/glassfish4}/bin/asadmin

$ASADMIN create-system-properties dk.purplegreen.logdir=../logs 