#!/bin/bash

java -fullversion

version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
if [[ "$version" < "1.8" ]]; then
	>&2 echo 
	>&2 echo "ERREUR : La version de installée de Java est inférieure à la version minimale requise (1.8+)"
	exit 1;
fi

cd /var/www/impro-photo
lastJar="$(ls -tr target | grep '\.jar$' | tail -1)"

echo 
echo "Launch JAR : ${lastJar}"
echo 

java -Xdebug -jar -Dspring.profiles.active=prod target/${lastJar} -browser
