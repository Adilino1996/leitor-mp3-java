@echo off
setlocal

cd /d "%~dp0\.."

if exist bin rmdir /s /q bin
mkdir bin

echo Compilando projeto...
javac -encoding UTF-8 -d bin src\model\NoMusica.java src\model\PlaylistDupla.java src\model\PlayerService.java src\ui\MenuConsole.java src\ui\LeitorMP3.java
if errorlevel 1 exit /b 1

echo Compilando testes...
javac -encoding UTF-8 -cp bin -d bin tests\PlaylistTest.java tests\PlayerTest.java tests\TestRunner.java
if errorlevel 1 exit /b 1

echo.
java -cp bin TestRunner
