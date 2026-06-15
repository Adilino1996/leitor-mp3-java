@echo off
setlocal

if exist bin rmdir /s /q bin
if exist dist rmdir /s /q dist

mkdir bin
mkdir dist

echo Compilando projeto...
javac -encoding UTF-8 -d bin src\model\NoMusica.java src\model\PlaylistDupla.java src\model\PlayerService.java src\ui\MenuConsole.java src\ui\LeitorMP3.java
if errorlevel 1 (
    echo ERRO: falha ao compilar o projeto.
    exit /b 1
)

echo Compilando testes...
javac -encoding UTF-8 -cp bin -d bin tests\PlaylistTest.java tests\PlayerTest.java tests\TestRunner.java
if errorlevel 1 (
    echo ERRO: falha ao compilar os testes.
    exit /b 1
)

echo Gerando dist\leitor-mp3.jar...
jar --create --file dist\leitor-mp3.jar --main-class ui.LeitorMP3 -C bin model -C bin ui
if errorlevel 1 (
    echo ERRO: falha ao gerar o JAR.
    exit /b 1
)

echo Executando testes...
java -cp bin TestRunner
if errorlevel 1 (
    echo ERRO: algum teste falhou.
    exit /b 1
)

echo.
echo Projeto pronto para teste.
echo Para executar: java -jar dist\leitor-mp3.jar
