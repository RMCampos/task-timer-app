@ECHO OFF

IF NOT EXIST bin (
	ECHO "Creating bin folder"
	MD bin
)

CD src\

javac -d ..\bin\ -cp .;..\lib\* utils\*.java
javac -d ..\bin\ -cp .;..\lib\* model\*.java
javac -d ..\bin\ -cp .;..\lib\* data\*.java
javac -d ..\bin\ -cp .;..\lib\* database\*.java
javac -d ..\bin\ -cp .;..\lib\* dao\*.java
javac -d ..\bin\ -cp .;..\lib\* view\*.java
javac -d ..\bin\ -cp .;..\lib\* controller\*.java

::javac -d ..\bin\ -cp ..\lib\* contador\controller\Programa.java

CD ..\bin\

jar cmf MANIFEST.txt Contador.jar utils\* model\* data\* database\* dao\* images\* view\* controller\*

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