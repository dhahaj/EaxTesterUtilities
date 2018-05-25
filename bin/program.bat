@echo off

@echo.
@echo Running UP programming software
@echo.

setlocal 
REM set UPDIR=%PROGRAMFILES(X86)%\ASIX\UP
REM set FILE=%USERPROFILE%\Desktop\MicroChip Dev\EAX500_591.hex

if "%~1"=="" (
	@echo No path to firmware provided. Stopping.
	set %ERRORLEVEL%=1
	goto ERROR
) else (
	@echo Firmware="%CD%\%~1"
)

REM set FILE=%1

REM @echo DIR="%UPDIR%"
REM @echo FILE="firmware\%FILE%"
REM @echo.

REM CD "%UPDIR%"

REM UP /part PIC16F627 /erase /q1
if "%~2"=="" (
	up.exe /q /q1 /part PIC16F627 /p "%CD%\%~1"
) else (
	@echo Serial Number=%2
	up.exe /q /q1 /part PIC16F627 /p "%CD%\%~1" /sn %2
)

if not %ERRORLEVEL%==0 (
	goto ERROR
) else (
	@echo Programming succesful.
	@echo ERROR=%ERRORLEVEL%
	GOTO:EOF
)

:ERROR
@echo Programming Error!, ERRORLEVEL=%ERRORLEVEL%
REM pause

REM up.exe [{/ask | /q | /q1}] [{/e data_mem_file.hex | [/noe]}] [{/p | [/pdiff] | [/o]} file.hex | file.ppr] [/eeonly] [/part part_name] [/erase] [/w[nd] up_window_class] [/cfg] [/devid] [/blank] [/verify file] [/read file] [/s programmer_SN] [/progname name] [/noboot] [/boot] [/code] [/getpartrev] [/sn serial_number]

 
REM /ask - Ask. To be used with /p. If the parameter is used, the program always prompts the user whether to program the part, even if this was disabled in the Settings of the program. The confirmation dialog also shows selected part type.

REM /q /quiet - Quiet mode. In this mode the program does not require any user intervention, but rather silently returns to an error code instead of displaying a dialog.

REM /q1 - Quiet mode 1. It works same like the /q parameter, but shows the Status form, which is closed after programming regardless of errors.

REM /e file - EEPROM file. Name of a file containing data memory data. If the name contains spaces, it is necessary to enclose it by quotes. This parameter can be used together with /o or /p parameter only.

REM /noe - No EEPROM. Causes the program to skip data memory programming and all operations with data memory. If used with the MSP430 devices, the program skip all operations with the information memory.

REM /p file - Program. Programs given file to code memory. If the name contains spaces, it is necessary to enclose it by quotes. (device is erased, code, data, cfg (and ID) memory is programmed and verified.)

REM /pdiff file - Program differentially. Programs given file. If the name contains spaces, it is necessary to enclose it by quotes.

REM /o file - Open. File with given name will be opened. Optional parameter. If the name contains spaces, it is necessary to enclose it by quotes.

REM /eeonly - The programmer will do the selected operation for data memory only, with MSP430 for Information memory only.

REM /part name - Selects the specified part in the UP.

REM /erase - The part will be erased.

REM /wnd class name - Select another window class name. Using this parameter you can open more than one instance of program UP. Each instance must have unique window class name.

REM /cfg - If this parameter is used together with /p parameter, only configuration memory is programmed. It's useful for example for AVR devices programming, because the user can configure the chip for faster oscillator first and then to program it much faster.

REM /devid - If this parameter is used together with /p parameter, only the Device ID of the chip is checked.

REM /blank - Program will check if the chip is blank, it will return an error code in accordance with the result.

REM /verify file - Does the part verification.

REM /read file - Reads the part and saves the read content to the file.

REM /s programmer_SN - This parameter allows to select the programmer in accordance with its serial number. The serial number can be entered as it is displayed by UP or as it is printed on the programmer, e.g.016709 or A6016709.

REM /progname name - This parameter allows to select the programmer type in accordance with its name, e.g. PRESTO or FORTE.

REM /noboot - Causes the program to skip the chosen operation with MSP430 boot memory.

REM /boot - The programmer will do the selected operation for MSP430 boot memory only.

REM /code - The programmer will do the selected operation with code or main memory of the chip only.

REM /getpartrev - Only reads the revision of the device and returns revision + 0x10000 as the error code.

REM /sn serial_number - Using this parameter it is possible to enter the value of the serial number, which is written to the address in accordance with the serial numbers settings. In the settings, the serial numbers have to be configured as Computed, Manual. The number is entered as hexadecimal, e.g. 1234ABCD.

