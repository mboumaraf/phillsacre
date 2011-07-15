@ECHO OFF

SET dir=%~dp0

REM Set the tools.dir property to be the 'cdrtools' directory under the directory of this batch file.
REM The property %~dp0 should contain this value, including the trailing '\'.
start "" javaw "-Dtools.dir=%dir%cdrtools" "-Dapplication.log.dir=%dir%logs" -jar ${project.artifactId}.one-jar.jar

REM Cleanup...
SET dir=