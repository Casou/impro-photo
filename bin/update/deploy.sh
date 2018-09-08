#!/bin/bash

. ./constants.sh

echo "Deploying new version"

newJar=[newJar].jar
cp $deployPath$newJar target/

