@echo off
echo Building...
cd /d %~dp0 
rmdir /S /Q bin
md bin
javac -classpath src -d bin src\engine\*.java src\game\*.java
echo done. bye.
