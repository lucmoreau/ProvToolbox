#!/bin/sh
# file generated automatically by provconvert -templatebuilder <file>
# modules-template/prov-template-compiler/src/main/resources/org/openprovenance/prov/template/compiler/script.sh



export CLASSPATH_PREFIX=$HOME/.m2/repository/${GROUP}/${NAME}_l2p/${VERSION}/${NAME}_l2p-${VERSION}.jar:$HOME/.m2/repository/${GROUP}/${NAME}_cli/${VERSION}/${NAME}_cli-${VERSION}.jar

PROVCONVERT=./provconvert

POSITIONAL=()

ARG1="$1"

while [[ $# -gt 0 ]]; do
  key="$1"

  case $key in
    -r|-relationOffset|--relationOffset)
      _RELATION_OFFSET="$2"
      shift # past argument
      shift # past value
      ;;
    -s|-setOffset|--setOffset)
      _SET_OFFSET="$2"
      shift # past argument
      shift # past value
      ;;
    -l|-levelOffset|--levelOffset)
      _LEVEL_OFFSET="$2"
      shift # past argument
      shift # past value
      ;;
    -L|-levelNumber|--levelNumber)
      _LEVEL_NUMBER="$2"
      shift # past argument
      shift # past value
      ;;
    -T|-knownTypes|--knownTypes)
      _KNOWN_TYPES="$2"
      shift # past argument
      shift # past value
      ;;
    -R|-knownRelations|--knownRelations)
      _KNOWN_RELATIONS="$2"
      shift # past argument
      shift # past value
      ;;
    -i|-infile|--infile)
      _INFILE="$2"
      shift # past argument
      shift # past value
      ;;
    -o|-outfile|--outfile)
      _OUTFILE="$2"
      shift # past argument
      shift # past value
      ;;
    -t|-translation|--translation)
          _TRANSLATION="$2"
          shift # past argument
          shift # past value
          ;;
    -addLevel0|--addLevel0)
          _ADD_LEVEL0="$2"
          shift # past argument
          shift # past value
          ;;
    -propertyConverters|--propertyConverters)
          _PROPERTY_CONVERTERS="$2"
          shift # past argument
          shift # past value
          ;;
    --default)
      export DEFAULT=YES
      shift # past argument
      ;;
    *)    # unknown option
      POSITIONAL+=("$1") # save it in an array for later
      shift # past argument
      ;;
  esac
done

if [ -z "$_INFILE" ]; then
  echo "NO input file"
  exit 1
fi

if [ -z "$_OUTFILE" ]; then
  echo "NO output file"
  exit 1;
fi


if [ "$ARG1" = "kernel" ]; then
   (export PROPERTY_CONVERTERS="$_PROPERTY_CONVERTERS" ADD_LEVEL0="$_ADD_LEVEL0" LEVEL_NUMBER="$_LEVEL_NUMBER" TRANSLATION="$_TRANSLATION" KNOWN_TYPES="$_KNOWN_TYPES" KNOWN_RELATIONS="$_KNOWN_RELATIONS" RELATION_OFFSET="$_RELATION_OFFSET"; export LEVEL_OFFSET="$_LEVEL_OFFSET"; export SET_OFFSET="$_SET_OFFSET"; $PROVCONVERT -infile $_INFILE -log2prov ${INIT} -outfile $_OUTFILE -log2kernel)
else
   $PROVCONVERT -infile "$_INFILE" -log2prov ${INIT} -outfile "$_OUTFILE"
fi



#  LocalWords:  pdtlib
