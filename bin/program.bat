@echo off
@echo Running UP Software

if "%~1" == "" (
    @echo Error: No valid firmware file provided.
    set ERRORLEVEL=1
    goto ERROR
)

REM Set the up directory
set UPDIR=%PROGRAMFILES(X86)%\ASIX\UP

REM : Display the date and time
DATE /T && TIME /T

set SERIAL=%TESTERDIR%\serial.sn
set FILE="%~1"
@echo SERIAL DATA: 
type %SERIAL%
REM @echo.
@echo FIRMWARE=%1
REM @echo SERIAL FILE=%SERIAL%

REM Run the software
"%UPDIR%"\UP /q /q1 /part PIC16F627 /p %FILE% /sn

:ERROR
if not %ERRORLEVEL%==0 (
    @echo Programming Error!, ERRORLEVEL=%ERRORLEVEL%
    @echo.
) else (
    @echo Programming succesful.
    @echo.
)

