@ECHO OFF

IF NOT EXIST bin (
	ECHO "Creating bin folder"
	MD bin
)

CD src\

"C:\Program Files (x86)\Java\jdk1.8.0_121\bin\javac.exe" -d ..\bin\ -cp .;..\lib\* utils\*.java
"C:\Program Files (x86)\Java\jdk1.8.0_121\bin\javac.exe" -d ..\bin\ -cp .;..\lib\* model\*.java
"C:\Program Files (x86)\Java\jdk1.8.0_121\bin\javac.exe" -d ..\bin\ -cp .;..\lib\* data\*.java
"C:\Program Files (x86)\Java\jdk1.8.0_121\bin\javac.exe" -d ..\bin\ -cp .;..\lib\* view\*.java
"C:\Program Files (x86)\Java\jdk1.8.0_121\bin\javac.exe" -d ..\bin\ -cp .;..\lib\* controller\*.java

::javac -d ..\bin\ -cp ..\lib\* contador\controller\Programa.java

CD ..\bin\

"C:\Program Files (x86)\Java\jdk1.8.0_121\bin\jar.exe" cmf MANIFEST.txt Contador.jar utils\* model\* data\* database\* dao\* images\* view\* controller\*

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