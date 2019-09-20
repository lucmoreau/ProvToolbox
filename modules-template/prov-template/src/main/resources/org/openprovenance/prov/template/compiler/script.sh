#!/bin/sh
# file generated automatically by provconvert -templatebuilder <file>
if [ "$#" -ne 2 ]; then
    echo "Illegal number of parameters: ${SCRIPT} <logfile> <provfile>"
    exit -1
fi

export CLASSPATH_PREFIX=$HOME/.m2/repository/${PACKAGE}/${NAME}_l2p/${VERSION}/${NAME}_l2p-${VERSION}.jar

PROVCONVERT=provconvert


$PROVCONVERT -infile $1 -log2prov ${INIT} -outfile $2


#  LocalWords:  pdtlib
