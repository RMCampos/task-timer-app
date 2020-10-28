@ECHO OFF

SET "JAVA_PATH="
SET "VERSION=2.0.0"

IF "%JAVA_PATH%" == "" (
    SET "JAVA_PATH=java"
)

%JAVA_PATH% -jar target\task-timer-app-"%VERSION%".jar