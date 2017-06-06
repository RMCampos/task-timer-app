@ECHO OFF

:: Create the compiled class folder if needed
IF NOT EXIST bin (
	ECHO "Creating bin folder"
	MD bin
)

:: The source folder
CD Contador\src\

:: Update HERE with your javac path!
"C:\Program Files (x86)\Java\jdk1.7.0_80\bin\javac.exe" -d ..\..\bin\ controller\*.java
"C:\Program Files (x86)\Java\jdk1.7.0_80\bin\javac.exe" -d ..\..\bin\ data\*.java
"C:\Program Files (x86)\Java\jdk1.7.0_80\bin\javac.exe" -d ..\..\bin\ model\*.java
"C:\Program Files (x86)\Java\jdk1.7.0_80\bin\javac.exe" -d ..\..\bin\ utils\*.java
"C:\Program Files (x86)\Java\jdk1.7.0_80\bin\javac.exe" -d ..\..\bin\ view\*.java
MD ..\..\bin\images\
COPY images\* ..\..\bin\images\

:: Enter in the binary folder
CD ..\..\bin\

:: Creathe the jar executable file
"C:\Program Files (x86)\Java\jdk1.7.0_80\bin\jar.exe" cmf ..\manifest.mf ..\Contador.jar controller\* data\* model\* utils\* view\* images\*

CD ..

PAUSE