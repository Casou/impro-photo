@echo off
MODE CON COLS=999 LINES=999

java -fullversion

PATH %PATH%;%JAVA_HOME%\bin\
for /f tokens^=2-5^ delims^=.-_^" %%j in ('java -fullversion 2^>^&1') do set "jver=%%j%%k%%l%%m"

set version=%jver:~0,2%
if %version% LSS 18 (
    color FC
    echo.
    echo.
    echo.
    echo Impossible de lancer l'application
    echo La version installee de Java est trop ancienne
    echo.
    echo.
    echo Mettre a jour Java : version 1.8+
    pause
    exit 4
)

rem ---------------------------------------------
rem ---------------------------------------------
rem GET LAST JAR
rem ---------------------------------------------
setlocal enableextensions disabledelayedexpansion

set lastJar=
for /f "delims=" %%a in ('dir /b /o-d "target\improPhoto-*.jar" 2^>nul') do (
    if not defined lastJar set "lastJar=%%a"
)

echo.
echo Launch JAR : %lastJar%
echo.
rem ---------------------------------------------
rem ---------------------------------------------

java -Xdebug -Dport=8888 -jar target\%lastJar%
pause
