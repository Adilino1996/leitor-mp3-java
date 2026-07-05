@echo off
setlocal

REM ====== CONFIGURACAO ======
REM Muda este caminho para onde tens o JavaFX SDK:
set JAVAFX=C:\javafx-sdk\lib
REM ==========================

if not exist "%JAVAFX%" (
    echo ERRO: Nao encontrei o JavaFX em %JAVAFX%
    echo Edita este ficheiro e poe o caminho certo na linha "set JAVAFX="
    pause
    exit /b 1
)

if exist bin rmdir /s /q bin
mkdir bin

echo ========================================
echo  A compilar o projeto...
echo ========================================
javac -encoding UTF-8 --module-path "%JAVAFX%" --add-modules javafx.controls,javafx.media -d bin src\model\*.java src\ui\*.java
if errorlevel 1 (
    echo ERRO: falha ao compilar o projeto.
    pause
    exit /b 1
)

echo.
echo ========================================
echo  A compilar os testes...
echo ========================================
javac -encoding UTF-8 --module-path "%JAVAFX%" --add-modules javafx.media -cp bin -d bin tests\PlaylistTest.java tests\PlayerTest.java tests\TestRunner.java
if errorlevel 1 (
    echo ERRO: falha ao compilar os testes.
    pause
    exit /b 1
)

echo.
echo ========================================
echo  A correr os testes...
echo ========================================
java --module-path "%JAVAFX%" --add-modules javafx.media -cp bin TestRunner
if errorlevel 1 (
    echo ERRO: algum teste falhou.
    pause
    exit /b 1
)

echo.
echo ========================================
echo  BUILD OK - Todos os testes passaram!
echo ========================================
echo.
echo Para correr a aplicacao:
echo   java --module-path "%JAVAFX%" --add-modules javafx.controls,javafx.media -cp bin ui.JanelaPrincipal
echo.
pause
