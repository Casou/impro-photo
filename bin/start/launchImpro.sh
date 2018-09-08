#!/bin/bash
# Script à lancer depuis la racine de l'application

ROUGE='\033[0;31m'
NONE='\033[0m'

path="target/"

lastFile=$(ls -1t $path | grep .jar | head -1)

echo "Lancement de la version ${lastFile}"

if [ -z "$lastFile" ]; then
    >&2 echo -e "${ROUGE}Could not launch the server.${NONE}"
    >&2 echo -e "${ROUGE}*** No jar file found.${NONE}"
    exit 1
fi

#java -Dport=8888 com.bparent.improPhoto.ImproPhotoApplication --spring.profiles.active=dev
java -Xdebug -Dport=8888 -jar $path$lastFile
#--spring.profiles.active=prod