@echo off
set folder=impro-photo
set folderPath=../%folder%
set backup_exclude_list=backup_%folder%_exclude.lst
set version_separator=-

:InputVersion
set version=
set /p version="Entrer la version pour l'archive %folder% : "

:InputSnapshot
set isSnapshot=
set /p isSnapshot="Version SNAPSHOT ? [y/n] => No = repackage : "
IF /I NOT "%isSnapshot%" == "n" (
	set version=%version%-SNAPSHOT
)

:SetVersion
IF NOT "%version%" == "" (
    call mvn versions:set -DnewVersion=%version%

    IF /I "%isSnapshot%" == "n" (
    	call mvn package
    )

	set version=%version_separator%%version%
	goto ZipFile
)

:ZipFile
set zip_file=%folderPath%%version%.zip
if exist %zip_file% (
    del %zip_file%
)
"C:\\Program Files\\7-Zip\\7z.exe" a -tzip %zip_file% %folderPath% -x@%backup_exclude_list%
if errorlevel 1  pause
