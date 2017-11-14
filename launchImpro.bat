@echo off
MODE CON COLS=999 LINES=999

rem ---------------------------------------------
rem ---------------------------------------------
rem GET LAST JAR
rem ---------------------------------------------
setlocal enableextensions disabledelayedexpansion

set lastJar=
for /f "delims=" %%a in ('dir /b /o-d "target\improPhoto-*.jar" 2^>nul') do (
    if not defined lastJar set "lastJar=%%a"
)

echo Launch JAR : %lastJar%
rem ---------------------------------------------
rem ---------------------------------------------

java -Xdebug -jar target\%lastJar%
pause
