@echo off
setlocal

cd /d "%~dp0\.."

if exist bin rmdir /s /q bin
mkdir bin

echo A compilar o backend...
javac -encoding UTF-8 -d bin src\model\NoMusica.java src\model\PlaylistDupla.java src\model\PlayerService.java
if errorlevel 1 exit /b 1

echo A compilar os testes...
javac -encoding UTF-8 -cp bin -d bin tests\PlaylistTest.java tests\PlayerTest.java tests\TestRunner.java
if errorlevel 1 exit /b 1

echo.
java -cp bin TestRunner
