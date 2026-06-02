@echo off
REM Compilation et exécution de l'application RaPizz

setlocal enabledelayedexpansion

cd /d "%~dp0"

REM Créer le répertoire bin
if not exist bin mkdir bin

echo ========================================
echo Compilation de l'application RaPizz...
echo ========================================

javac -d bin -cp "mysql-connector-j-9.7.0.jar" ^
  src\rapizz\*.java ^
  src\rapizz\model\*.java ^
  src\rapizz\dao\*.java ^
  src\rapizz\ui\*.java ^
  src\rapizz\util\*.java

if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Compilation echouée!
    pause
    exit /b 1
)

echo.
echo Compilation reussie!
echo.
echo Choisissez une action:
echo 1 - Test de connexion
echo 2 - Lancer l'application
echo.
set /p choice="Votre choix (1 ou 2): "

if "%choice%"=="1" (
    echo.
    echo ========================================
    echo Test de connexion...
    echo ========================================
    java -cp "bin;mysql-connector-j-9.7.0.jar" rapizz.TestConnection
) else if "%choice%"=="2" (
    echo.
    echo ========================================
    echo Lancement de l'application...
    echo ========================================
    java -cp "bin;mysql-connector-j-9.7.0.jar" rapizz.AppMain
) else (
    echo Choix invalide!
)

pause
