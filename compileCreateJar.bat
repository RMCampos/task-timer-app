@ECHO OFF

IF NOT EXIST bin (
	ECHO "Creating bin folder"
	MD bin
)

CD src\

javac -d ..\bin\ contador\*.java

CD ..\bin\

jar -cfm Contador.jar MANIFEST.MF contador\*

CD ..

IF EXIST bin\Contador.jar (
	MOVE bin\Contador.jar
	ECHO "Done"
) ELSE (
	ECHO "Error"
)