@ECHO OFF

IF NOT EXIST bin (
	ECHO "Creating bin folder"
	MD bin
)

CD src\

javac -d ..\bin\ -cp ..\lib\* contador\*.java

CD ..\bin\

jar cmf MANIFEST.txt Contador.jar contador\*

CD ..



IF EXIST bin\Contador.jar (
	IF EXIST Contador.jar (
		DEL Contador.jar
	)
	MOVE bin\Contador.jar
	ECHO "Done"
) ELSE (
	ECHO "Error"
)

PAUSE