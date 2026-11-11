@echo off
cd /d "%~dp0"

rem ============================================================
rem  JDK 17 is REQUIRED.
rem  The system JAVA_HOME points to JDK 25, which breaks Lombok
rem  (getters/setters are never generated -> "cannot find symbol")
rem  and is not supported by Spring Boot 2.7.x.
rem ============================================================
set "JAVA17=C:\Program Files\Microsoft\jdk-17.0.14.7-hotspot"

if not exist "%JAVA17%\bin\java.exe" (
    echo [ERROR] JDK 17 not found at:
    echo         %JAVA17%
    echo Edit JAVA17 in this file to point at your JDK 17 folder.
    pause
    exit /b 1
)

set "PATH=%JAVA17%\bin;%PATH%"
set "JAR=target\yu-picture-backend-0.0.1-SNAPSHOT.jar"

if not exist "%JAR%" (
    echo [ERROR] jar not found: %JAR%
    echo Build it first, with JDK 17:
    echo     set "JAVA_HOME=%JAVA17%"
    echo     mvn -DskipTests package
    pause
    exit /b 1
)

echo JDK      : %JAVA17%
echo JAR      : %JAR%
echo Endpoint : http://localhost:8123/api/
echo API docs : http://localhost:8123/api/doc.html
echo.

java -jar "%JAR%"

echo.
echo [stopped] press any key to exit
pause >nul
