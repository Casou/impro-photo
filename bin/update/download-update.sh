#!/bin/sh
# Script à lancer depuis la racine de l'application

# Paramètres
# 1 : URL

echo
echo

. ./constants.sh

tempTargetPath="/tmp/"
tempTargetFile="improPhotoUpdate.zip"
tempTargetFilePath=$tempTargetPath$tempTargetFile

urlDownload=$1

# Download into temp
echo "${Cyan}Downloading update from URL ${Blue}$urlDownload${NONE}"
wget -O $tempTargetFilePath $urlDownload 1>/dev/null 2>/dev/null

# Move ZIP from tmp to the deploy folder
deployZipFilePath=$deployPath$tempTargetFile
targetFolder="target"
targetPath=$ABSOLUTE_PATH$targetFolder
echo "${Cyan}Moving ZIP from ${Blue}$tempTargetFilePath${Cyan} to ${Blue}$deployPath${NONE}"
cp $tempTargetFilePath $deployPath

# Unzip the deploying archive
echo "${Cyan}Unzip ${Blue}$deployZipFilePath${NONE}"
unzip -o $deployZipFilePath -d $deployPath 1>/dev/null

# Execute the deployment
deployExecutableZipFile="deploy.sh"
deployExecutableZipFilePath=$deployPath$deployExecutableZipFile
echo "${Yellow}"
. $deployExecutableZipFilePath
echo "${NONE}"

# Remove the files
filesToDelete="*"
rm $deployPath$filesToDelete

echo "${On_IGreen}${BIBlack}Update finished.${NONE}"
echo "${Cyan}Now you can restart the launcher.${NONE}"

