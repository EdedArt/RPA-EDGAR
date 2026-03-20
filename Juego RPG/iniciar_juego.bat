@echo off
title Servidor RPG Java (Decorator)
color 0b

echo Cerrando ejecuciones anteriores del Servidor...
taskkill /F /IM java.exe /T 2>nul
ping 127.0.0.1 -n 2 > nul

echo =======================================================
echo          COMPILANDO EL JUEGO BACKEND (JAVA)
echo          El proyecto tiene multiples archivos.
echo =======================================================
javac -encoding UTF-8 *.java
if %errorlevel% neq 0 (
    echo.
    echo HUBO UN ERROR AL COMPILAR EL CODIGO JAVA
    pause
    exit /b %errorlevel%
)

echo.
echo =======================================================
echo          INICIANDO NUEVO SERVIDOR WEB RPG (HTTP)
echo =======================================================
echo El servidor Java controlara toda la Web Interface...
echo Abriendo navegador en http://localhost:8081...
start http://localhost:8081

java RPGWebServer
pause
