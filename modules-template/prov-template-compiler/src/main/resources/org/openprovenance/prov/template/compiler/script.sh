#!/bin/sh
# file generated automatically by provconvert -templatebuilder <file>
if [ "$#" -ne 2 ]; then
    if [ "$#" -ne 3 ]; then
      echo "Illegal number of parameters: ${SCRIPT} <logfile> <provfile>"
      echo "Illegal number of parameters: ${SCRIPT} kernel <logfile> <jsonfile>"
      exit -1
    fi
fi

export CLASSPATH_PREFIX=$HOME/.m2/repository/${GROUP}/${NAME}_l2p/${VERSION}/${NAME}_l2p-${VERSION}.jar:$HOME/.m2/repository/${GROUP}/${NAME}_cli/${VERSION}/${NAME}_cli-${VERSION}.jar

PROVCONVERT=./provconvert


$PROVCONVERT -infile $1 -log2prov ${INIT} -outfile $2


#  LocalWords:  pdtlib
