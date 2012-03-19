@echo off
echo Building...
del bin\game\*.class /Q
del bin\engine\*.class /Q
del bin\*.jar /Q
javac -classpath src\ -d bin src\engine\*.java src\game\*.java
cd bin
jar cmf ..\MANIFEST.mf skeleton.jar game\*.class engine\*.class
cd ..
echo done. bye.
