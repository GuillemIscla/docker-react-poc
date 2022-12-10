#!/usr/bin/env bash

ORIGINAL_DIR=$(pwd)
SCRIPT_DIR="$( cd -- "$( dirname -- "${BASH_SOURCE[0]:-$0}"; )" &> /dev/null && pwd 2> /dev/null; )";

help () {
  echo ""
  echo "Builds each react project on the folder 'webapps' and copies the content in"
  echo "a folder inside the resources which is not tracked by git"
  echo ""
  echo "Optional arguments"
  echo -e "  \e[1m-p\e[0m    List of projects to build (comma separated list, default is all)"
  echo -e "  \e[1m-h\e[0m    Show this help"
  echo ""
}

while getopts :p:h opt
do
    case "$opt" in
    p) SPECIFIC=($OPTARG)
      ;;
    h) help; exit
      ;;
    esac
done

specific_array=($SPECIFIC)

cd $SCRIPT_DIR/../webapps
DIRECTORIES=$(find . -maxdepth 1 -mindepth 1 -type d)

for VARIABLE in $DIRECTORIES
do
  if [[ -z "$SPECIFIC" || "${IFS}./${specific_array[*]}${IFS}" =~ "${IFS}${VARIABLE}${IFS}" ]] ; then
    cd $VARIABLE
    echo ""
    echo -e "\e[1m\e[4m\e[95mBuilding project ${VARIABLE:2}\e[0m"
    echo ""
    npm run build
    cd $SCRIPT_DIR/..
    mkdir -p server/src/main/resources/public/webapps/$VARIABLE
    cp -r webapps/$VARIABLE/build/* server/src/main/resources/public/webapps/$VARIABLE
    cd $SCRIPT_DIR/../webapps
  else
    echo ""
    echo -e "\e[1m\e[4m\e[95mOmitting project ${VARIABLE:2}\e[0m"
    echo ""
  fi
done

cd $ORIGINAL_DIR
